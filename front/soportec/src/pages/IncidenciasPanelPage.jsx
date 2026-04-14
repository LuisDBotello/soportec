import { useCallback, useEffect, useMemo, useState } from 'react'

const API_URL = import.meta.env.VITE_API_URL
const SESSION_USER_KEY = 'soportec.auth.user'
const JEFE_TALLER_NIVEL_ID = 1

const PRIORIDAD_OPTIONS = [
  { value: 1, label: 'Alta' },
  { value: 2, label: 'Media' },
  { value: 3, label: 'Baja' }
]

const TIPO_OPTIONS = [
  { value: 1, label: 'Hardware' },
  { value: 2, label: 'Software' },
  { value: 3, label: 'Redes' },
  { value: 4, label: 'Telefonia' }
]

function parseResponseError(payload, fallback) {
  if (!payload) {
    return fallback
  }

  if (typeof payload === 'string') {
    return payload
  }

  return payload.message || payload.error || fallback
}

function formatDate(value) {
  if (!value) {
    return '-'
  }

  const parsed = new Date(`${value}T00:00:00`)
  if (Number.isNaN(parsed.getTime())) {
    return value
  }

  return parsed.toLocaleDateString('es-MX', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

function toChronological(items) {
  return [...items].sort((a, b) => {
    const dateA = a?.fechaCreacion ? Date.parse(`${a.fechaCreacion}T00:00:00`) : 0
    const dateB = b?.fechaCreacion ? Date.parse(`${b.fechaCreacion}T00:00:00`) : 0
    if (dateA !== dateB) {
      return dateA - dateB
    }
    return Number(a?.folio || 0) - Number(b?.folio || 0)
  })
}

function hasPendingClassification(incidencia) {
  return !incidencia?.prioridad || !incidencia?.tipo || !incidencia?.tiempoEstimado || !incidencia?.encargadoId
}

function IncidenciasPanelPage() {
  const [incidencias, setIncidencias] = useState([])
  const [tecnicos, setTecnicos] = useState([])
  const [isLoading, setIsLoading] = useState(false)
  const [isSavingDialog, setIsSavingDialog] = useState(false)
  const [selectedIncidencia, setSelectedIncidencia] = useState(null)
  const [dialogForm, setDialogForm] = useState({
    prioridad: '',
    tipo: '',
    tiempoEstimado: '',
    tecnicoId: ''
  })
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

  const isJefeTaller = Number(sessionUser?.nivel?.id_nivel || 0) === JEFE_TALLER_NIVEL_ID

  const loadIncidencias = useCallback(async () => {
    setIsLoading(true)
    setError('')
    setSuccess('')

    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias/cronologico`)
      const payload = await response.json().catch(() => null)

      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudieron cargar las incidencias.'))
      }

      const list = Array.isArray(payload) ? payload : []
      setIncidencias(toChronological(list))
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar incidencias.')
    } finally {
      setIsLoading(false)
    }
  }, [])

  const loadTecnicos = useCallback(async () => {
    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias/tecnicos`)
      const payload = await response.json().catch(() => null)
      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudieron cargar los tecnicos.'))
      }
      setTecnicos(Array.isArray(payload) ? payload : [])
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar tecnicos.')
    }
  }, [])

  useEffect(() => {
    if (isJefeTaller) {
      loadIncidencias()
      loadTecnicos()
    }
  }, [isJefeTaller, loadIncidencias, loadTecnicos])

  const openDialog = (incidencia) => {
    setError('')
    setSuccess('')
    setSelectedIncidencia(incidencia)
    setDialogForm({
      prioridad: incidencia?.prioridad ? String(incidencia.prioridad) : '',
      tipo: incidencia?.tipo ? String(incidencia.tipo) : '',
      tiempoEstimado: incidencia?.tiempoEstimado ? String(incidencia.tiempoEstimado) : '',
      tecnicoId: incidencia?.encargadoId ? String(incidencia.encargadoId) : ''
    })
  }

  const closeDialog = (force = false) => {
    if (isSavingDialog && !force) {
      return
    }
    setSelectedIncidencia(null)
    setDialogForm({ prioridad: '', tipo: '', tiempoEstimado: '', tecnicoId: '' })
  }

  const handleDialogChange = (event) => {
    const { name, value } = event.target
    setError('')
    setSuccess('')
    setDialogForm((prev) => ({ ...prev, [name]: value }))
  }

  const handleSaveDialog = async (event) => {
    event.preventDefault()
    if (!selectedIncidencia?.folio) {
      return
    }

    const prioridad = Number(dialogForm.prioridad)
    const tipo = Number(dialogForm.tipo)
    const tiempoEstimado = Number(dialogForm.tiempoEstimado)
    const tecnicoId = Number(dialogForm.tecnicoId)

    if (!prioridad || !tipo || !tiempoEstimado || !tecnicoId) {
      setError('Completa prioridad, tipo, tiempo estimado y tecnico para actualizar.')
      return
    }
    if (!Number.isInteger(tiempoEstimado) || tiempoEstimado < 1 || tiempoEstimado > 1000) {
      setError('El tiempo estimado debe ser un entero entre 1 y 1000 horas.')
      return
    }

    setIsSavingDialog(true)
    setError('')
    setSuccess('')
    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias/${selectedIncidencia.folio}/clasificacion`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          prioridad,
          tipo,
          tiempoEstimado,
          tecnicoId
        })
      })

      const payload = await response.json().catch(() => null)
      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudo actualizar la clasificacion.'))
      }

      setIncidencias((prev) =>
        prev.map((item) =>
          item.folio === selectedIncidencia.folio
            ? {
                ...item,
                prioridad,
                tipo,
                tiempoEstimado,
                encargadoId: tecnicoId,
                encargado: tecnicos.find((t) => Number(t.idUsuario) === tecnicoId)?.nombreCompleto || item.encargado
              }
            : item
        )
      )

      setSuccess(`Incidencia ${selectedIncidencia.folio} actualizada correctamente.`)
      closeDialog(true)
    } catch (requestError) {
      setError(requestError.message || 'Error al actualizar clasificacion.')
    } finally {
      setIsSavingDialog(false)
    }
  }

  if (!sessionUser) {
    return (
      <main className="simple-page">
        <h1>Sesion no iniciada</h1>
        <p>Debes iniciar sesion para consultar el panel de incidencias.</p>
        <p><a href="#/login">Ir a Login</a></p>
      </main>
    )
  }

  if (!isJefeTaller) {
    return (
      <main className="simple-page">
        <h1>Acceso restringido</h1>
        <p>Este modulo esta disponible solo para Jefe o Encargado de taller.</p>
      </main>
    )
  }

  return (
    <main className="users-page">
      <section className="users-header">
        <h1>Panel de Incidencias</h1>
        <p>Haz clic en una incidencia para abrir el dialogo de clasificacion.</p>
      </section>

      <section className="users-card">
        <div className="asset-actions panel-actions">
          <button type="button" className="secondary" onClick={loadIncidencias} disabled={isLoading}>
            {isLoading ? 'Actualizando...' : 'Actualizar panel'}
          </button>
        </div>

        {isLoading && <p>Cargando incidencias...</p>}
        {!isLoading && incidencias.length === 0 && <p>No hay incidencias registradas.</p>}

        {!isLoading && incidencias.length > 0 && (
          <div className="users-table-wrap">
            <table className="users-table">
              <thead>
                <tr>
                  <th>Fecha</th>
                  <th>Folio</th>
                  <th>Estatus</th>
                  <th>Tecnico asignado</th>
                  <th>Revision</th>
                </tr>
              </thead>
              <tbody>
                {incidencias.map((incidencia) => (
                  <tr
                    key={incidencia.folio}
                    className={`panel-row-clickable${hasPendingClassification(incidencia) ? ' incidencia-pendiente' : ''}`}
                    onClick={() => openDialog(incidencia)}
                  >
                    <td>{formatDate(incidencia.fechaCreacion)}</td>
                    <td>{incidencia.folio ?? '-'}</td>
                    <td>{incidencia.estatus || '-'}</td>
                    <td>{incidencia.encargado || 'Sin asignar'}</td>
                    <td>{hasPendingClassification(incidencia) ? 'Pendiente' : 'Revisada'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>

      {error && <p className="users-message error">{error}</p>}
      {success && <p className="users-message success">{success}</p>}

      {selectedIncidencia && (
        <div className="dialog-overlay" role="dialog" aria-modal="true" aria-labelledby="clasificar-title">
          <section className="dialog-card">
            <h2 id="clasificar-title">Clasificar incidencia #{selectedIncidencia.folio}</h2>
            <p className="dialog-subtitle">
              Define prioridad, tipo de problema y tiempo estimado en horas.
            </p>

            <section className="incidencia-details">
              <h3>Detalle de incidencia</h3>
              <dl className="incidencia-detail-grid">
                <div>
                  <dt>Fecha</dt>
                  <dd>{formatDate(selectedIncidencia.fechaCreacion)}</dd>
                </div>
                <div>
                  <dt>Estatus</dt>
                  <dd>{selectedIncidencia.estatus || '-'}</dd>
                </div>
                <div>
                  <dt>Solicitante</dt>
                  <dd>{selectedIncidencia.solicitante || '-'}</dd>
                </div>
                <div>
                  <dt>Activo</dt>
                  <dd>{selectedIncidencia.activoEtiqueta || '-'}</dd>
                </div>
                <div>
                  <dt>Ubicacion</dt>
                  <dd>{`${selectedIncidencia.edificio || '-'} / ${selectedIncidencia.espacio || '-'}`}</dd>
                </div>
                <div>
                  <dt>Prioridad actual</dt>
                  <dd>{PRIORIDAD_OPTIONS.find((option) => option.value === Number(selectedIncidencia.prioridad))?.label || 'Sin clasificar'}</dd>
                </div>
                <div>
                  <dt>Tipo actual</dt>
                  <dd>{TIPO_OPTIONS.find((option) => option.value === Number(selectedIncidencia.tipo))?.label || 'Sin clasificar'}</dd>
                </div>
                <div>
                  <dt>Tiempo actual</dt>
                  <dd>{selectedIncidencia.tiempoEstimado ? `${selectedIncidencia.tiempoEstimado} h` : 'Sin estimar'}</dd>
                </div>
                <div>
                  <dt>Tecnico actual</dt>
                  <dd>{selectedIncidencia.encargado || 'Sin asignar'}</dd>
                </div>
              </dl>
              <div className="incidencia-descripcion">
                <h4>Descripcion</h4>
                <p>{selectedIncidencia.descripcion || '-'}</p>
              </div>
            </section>

            <form className="dialog-form" onSubmit={handleSaveDialog}>
              <label className="form-field">
                <span>Prioridad</span>
                <select
                  name="prioridad"
                  value={dialogForm.prioridad}
                  onChange={handleDialogChange}
                  required
                >
                  <option value="">Seleccionar prioridad</option>
                  {PRIORIDAD_OPTIONS.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
              </label>

              <label className="form-field">
                <span>Tipo de problema</span>
                <select
                  name="tipo"
                  value={dialogForm.tipo}
                  onChange={handleDialogChange}
                  required
                >
                  <option value="">Seleccionar tipo</option>
                  {TIPO_OPTIONS.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
              </label>

              <label className="form-field">
                <span>Tiempo estimado (horas)</span>
                <input
                  type="number"
                  name="tiempoEstimado"
                  min="1"
                  max="1000"
                  step="1"
                  value={dialogForm.tiempoEstimado}
                  onChange={handleDialogChange}
                  required
                />
              </label>

              <label className="form-field">
                <span>Tecnico asignado</span>
                <select
                  name="tecnicoId"
                  value={dialogForm.tecnicoId}
                  onChange={handleDialogChange}
                  required
                >
                  <option value="">Seleccionar tecnico</option>
                  {tecnicos.map((tecnico) => (
                    <option key={tecnico.idUsuario} value={tecnico.idUsuario}>
                      {tecnico.nombreCompleto || tecnico.username || `Tecnico ${tecnico.idUsuario}`}
                    </option>
                  ))}
                </select>
              </label>

              <div className="dialog-actions">
                <button type="button" className="secondary" onClick={closeDialog} disabled={isSavingDialog}>
                  Cancelar
                </button>
                <button type="submit" disabled={isSavingDialog}>
                  {isSavingDialog ? 'Actualizando...' : 'Actualizar clasificacion'}
                </button>
              </div>
            </form>
          </section>
        </div>
      )}
    </main>
  )
}

export default IncidenciasPanelPage
