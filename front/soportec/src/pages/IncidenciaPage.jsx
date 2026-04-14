import { useCallback, useEffect, useMemo, useState } from 'react'
import SelectField from '../components/fields/SelectField'

const API_URL = import.meta.env.VITE_API_URL
const SESSION_USER_KEY = 'soportec.auth.user'

const INITIAL_FORM = {
  edificioId: '',
  espacioId: '',
  activoId: '',
  descripcion: ''
}

function parseResponseError(payload, fallback) {
  if (!payload) {
    return fallback
  }

  if (typeof payload === 'string') {
    return payload
  }

  return payload.message || payload.error || fallback
}

function toSelectOptions(items, idKeys, labelBuilder) {
  return (Array.isArray(items) ? items : [])
    .map((item) => {
      const id = idKeys.map((key) => item?.[key]).find((value) => value !== undefined && value !== null)
      if (id === undefined || id === null) {
        return null
      }

      return {
        value: String(id),
        label: labelBuilder(item),
        raw: item
      }
    })
    .filter(Boolean)
}

function IncidenciaPage() {
  const [form, setForm] = useState(INITIAL_FORM)
  const [options, setOptions] = useState({
    edificios: [],
    espacios: [],
    activos: []
  })
  const [loading, setLoading] = useState({
    edificios: false,
    espacios: false,
    activos: false,
    submit: false
  })
  const [errors, setErrors] = useState({})
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const sessionUser = useMemo(() => {
    const raw = window.sessionStorage.getItem(SESSION_USER_KEY)
    if (!raw) {
      return null
    }

    try {
      return JSON.parse(raw)
    } catch {
      return null
    }
  }, [])

  const isEncargadoArea = Number(sessionUser?.nivel?.id_nivel || 0) === 3

  const fetchJson = useCallback(async (path, fallbackMessage) => {
    const response = await fetch(`${API_URL}${path}`)
    const payload = await response.json().catch(() => null)

    if (!response.ok) {
      throw new Error(parseResponseError(payload, fallbackMessage))
    }

    return payload
  }, [])

  const loadEdificios = useCallback(async () => {
    setLoading((prev) => ({ ...prev, edificios: true }))
    setError('')
    try {
      const edificios = await fetchJson('/edificios', 'No se pudieron cargar los edificios.')
      setOptions((prev) => ({
        ...prev,
        edificios: toSelectOptions(edificios, ['idEdificio', 'id_edificio', 'id'], (item) => String(item?.nombre || 'Sin nombre'))
      }))
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar edificios.')
    } finally {
      setLoading((prev) => ({ ...prev, edificios: false }))
    }
  }, [fetchJson])

  const loadEspacios = useCallback(async (edificioId) => {
    if (!edificioId) {
      setOptions((prev) => ({ ...prev, espacios: [] }))
      return
    }

    setLoading((prev) => ({ ...prev, espacios: true }))
    setError('')
    try {
      const espacios = await fetchJson(
        `/espacios?edificio=${encodeURIComponent(edificioId)}`,
        'No se pudieron cargar los espacios.'
      )

      setOptions((prev) => ({
        ...prev,
        espacios: toSelectOptions(espacios, ['idEspacio', 'id_espacio', 'id'], (item) => String(item?.nombre || 'Sin nombre'))
      }))
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar espacios.')
    } finally {
      setLoading((prev) => ({ ...prev, espacios: false }))
    }
  }, [fetchJson])

  const loadActivosPorEspacio = useCallback(async (espacioId) => {
    if (!espacioId) {
      setOptions((prev) => ({ ...prev, activos: [] }))
      return
    }

    setLoading((prev) => ({ ...prev, activos: true }))
    setError('')
    try {
      const activos = await fetchJson(
        `/activos/por-espacio?espacioId=${encodeURIComponent(espacioId)}`,
        'No se pudieron cargar los activos del espacio.'
      )

      setOptions((prev) => ({
        ...prev,
        activos: toSelectOptions(activos, ['idActivo', 'id_activo', 'id'], (item) => {
          const marca = item?.marca || 'Sin marca'
          const modelo = item?.modelo || 'Sin modelo'
          const serie = item?.numeroSerie || 'Sin serie'
          return `${marca} ${modelo} | S/N: ${serie}`
        })
      }))
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar activos por espacio.')
    } finally {
      setLoading((prev) => ({ ...prev, activos: false }))
    }
  }, [fetchJson])

  useEffect(() => {
    loadEdificios()
  }, [loadEdificios])

  useEffect(() => {
    loadEspacios(form.edificioId)
  }, [form.edificioId, loadEspacios])

  useEffect(() => {
    loadActivosPorEspacio(form.espacioId)
  }, [form.espacioId, loadActivosPorEspacio])

  const handleChange = (event) => {
    const { name, value } = event.target
    setError('')
    setSuccess('')

    setErrors((prev) => {
      if (!prev[name]) {
        return prev
      }
      const next = { ...prev }
      delete next[name]
      return next
    })

    if (name === 'edificioId') {
      setForm((prev) => ({
        ...prev,
        edificioId: value,
        espacioId: '',
        activoId: ''
      }))
      setOptions((prev) => ({ ...prev, espacios: [], activos: [] }))
      return
    }

    if (name === 'espacioId') {
      setForm((prev) => ({
        ...prev,
        espacioId: value,
        activoId: ''
      }))
      setOptions((prev) => ({ ...prev, activos: [] }))
      return
    }

    setForm((prev) => ({ ...prev, [name]: value }))
  }

  const validate = () => {
    const nextErrors = {}
    if (!form.edificioId) {
      nextErrors.edificioId = 'Selecciona un edificio.'
    }
    if (!form.espacioId) {
      nextErrors.espacioId = 'Selecciona un espacio.'
    }
    if (!form.activoId) {
      nextErrors.activoId = 'Selecciona el activo que fallo.'
    }
    if (!form.descripcion.trim()) {
      nextErrors.descripcion = 'Describe la incidencia.'
    }
    if (form.descripcion.trim().length > 255) {
      nextErrors.descripcion = 'La descripcion no debe exceder 255 caracteres.'
    }

    setErrors(nextErrors)
    return Object.keys(nextErrors).length === 0
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setSuccess('')

    if (!validate()) {
      return
    }

    setLoading((prev) => ({ ...prev, submit: true }))
    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          usuarioId: sessionUser?.idUsuario ? Number(sessionUser.idUsuario) : null,
          espacioId: Number(form.espacioId),
          activoId: Number(form.activoId),
          prioridad: null,
          descripcion: form.descripcion.trim()
        })
      })

      const payload = await response.json().catch(() => null)
      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudo crear la incidencia.'))
      }

      setSuccess(`Incidencia creada correctamente. Folio: ${payload?.folio ?? 'N/D'}`)
      setForm(INITIAL_FORM)
      setOptions((prev) => ({ ...prev, espacios: [], activos: [] }))
      setErrors({})
    } catch (requestError) {
      setError(requestError.message || 'Error al crear incidencia.')
    } finally {
      setLoading((prev) => ({ ...prev, submit: false }))
    }
  }

  if (!sessionUser) {
    return (
      <main className="simple-page">
        <h1>Sesion no iniciada</h1>
        <p>Debes iniciar sesion para crear incidencias.</p>
        <p><a href="#/login">Ir a Login</a></p>
      </main>
    )
  }

  if (!isEncargadoArea) {
    return (
      <main className="simple-page">
        <h1>Acceso restringido</h1>
        <p>Este modulo esta disponible solo para Responsable de area.</p>
      </main>
    )
  }

  return (
    <main className="asset-page">
      <section className="asset-header">
        <h1>Generar Incidencia</h1>
        <p>Selecciona ubicacion, activo fallado y describe el incidente para crear la orden.</p>
      </section>

      <form className="asset-form" onSubmit={handleSubmit}>
        <section className="asset-section">
          <h2>Datos de Incidencia</h2>
          <div className="asset-grid">
            <SelectField
              name="edificioId"
              label="Edificio"
              value={form.edificioId}
              onChange={handleChange}
              options={options.edificios}
              loading={loading.edificios}
              error={errors.edificioId}
              required
              placeholder="Selecciona un edificio"
            />

            <SelectField
              name="espacioId"
              label="Espacio"
              value={form.espacioId}
              onChange={handleChange}
              options={options.espacios}
              loading={loading.espacios}
              disabled={!form.edificioId}
              error={errors.espacioId}
              required
              placeholder={form.edificioId ? 'Selecciona un espacio' : 'Primero selecciona edificio'}
            />

            <SelectField
              name="activoId"
              label="Activo con falla"
              value={form.activoId}
              onChange={handleChange}
              options={options.activos}
              loading={loading.activos}
              disabled={!form.espacioId}
              error={errors.activoId}
              required
              placeholder={form.espacioId ? 'Selecciona un activo' : 'Primero selecciona espacio'}
            />
          </div>

          <label className="form-field">
            <span>Descripcion de la falla *</span>
            <textarea
              name="descripcion"
              value={form.descripcion}
              onChange={handleChange}
              rows={5}
              maxLength={255}
              required
              placeholder="Describe que fallo, sintomas observados y contexto."
              aria-invalid={Boolean(errors.descripcion)}
              aria-describedby={errors.descripcion ? 'descripcion-error' : undefined}
            />
            {errors.descripcion && (
              <small id="descripcion-error" className="field-error">
                {errors.descripcion}
              </small>
            )}
          </label>
        </section>

        <div className="asset-actions">
          <button type="submit" disabled={loading.submit}>
            {loading.submit ? 'Creando incidencia...' : 'Generar incidencia'}
          </button>
        </div>
      </form>

      {error && <p className="users-message error">{error}</p>}
      {success && <p className="users-message success">{success}</p>}
    </main>
  )
}

export default IncidenciaPage
