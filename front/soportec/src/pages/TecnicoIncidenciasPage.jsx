import { useCallback, useEffect, useMemo, useState } from 'react'

const API_URL = import.meta.env.VITE_API_URL
const SESSION_USER_KEY = 'soportec.auth.user'
const TECNICO_NIVEL_ID = 2
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

function prioridadLabel(value) {
  return PRIORIDAD_OPTIONS.find((option) => option.value === Number(value))?.label || '-'
}

function tipoLabel(value) {
  return TIPO_OPTIONS.find((option) => option.value === Number(value))?.label || '-'
}

function TecnicoIncidenciasPage() {
  const [incidencias, setIncidencias] = useState([])
  const [estados, setEstados] = useState([])
  const [selected, setSelected] = useState(null)
  const [isLoading, setIsLoading] = useState(false)
  const [isLoadingHistory, setIsLoadingHistory] = useState(false)
  const [isSaving, setIsSaving] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [form, setForm] = useState({
    estadoId: '',
    fecha: new Date().toISOString().slice(0, 10),
    comentario: ''
  })

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

  const isTecnico = Number(sessionUser?.nivel?.id_nivel || 0) === TECNICO_NIVEL_ID
  const tecnicoId = Number(sessionUser?.idUsuario || 0)
  const estadosDisponibles = useMemo(
    () => estados.filter((estado) => Number(estado?.id) !== 1 && Number(estado?.id) !== 5),
    [estados]
  )

  const loadIncidencias = useCallback(async () => {
    if (!tecnicoId) {
      return
    }
    setIsLoading(true)
    setError('')
    setSuccess('')
    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias/asignadas?tecnicoId=${encodeURIComponent(tecnicoId)}`)
      const payload = await response.json().catch(() => null)
      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudieron cargar las incidencias asignadas.'))
      }
      setIncidencias(Array.isArray(payload) ? payload : [])
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar incidencias asignadas.')
    } finally {
      setIsLoading(false)
    }
  }, [tecnicoId])

  const loadEstados = useCallback(async () => {
    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias/estados-evento`)
      const payload = await response.json().catch(() => null)
      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudieron cargar los estados.'))
      }
      setEstados(Array.isArray(payload) ? payload : [])
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar estados.')
    }
  }, [])

  useEffect(() => {
    if (isTecnico) {
      loadIncidencias()
      loadEstados()
    }
  }, [isTecnico, loadIncidencias, loadEstados])

  const openHistory = async (folio) => {
    setError('')
    setSuccess('')
    setIsLoadingHistory(true)
    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias/${folio}/historial`)
      const payload = await response.json().catch(() => null)
      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudo cargar la historia de la incidencia.'))
      }
      setSelected(payload)
      setForm((prev) => ({ ...prev, comentario: '' }))
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar historial.')
    } finally {
      setIsLoadingHistory(false)
    }
  }

  const closeHistory = () => {
    if (isSaving) {
      return
    }
    setSelected(null)
  }

  const handleChange = (event) => {
    const { name, value } = event.target
    setError('')
    setSuccess('')
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    if (!selected?.folio) {
      return
    }
    if (!form.estadoId) {
      setError('Selecciona un estado para registrar el evento.')
      return
    }
    if (form.comentario.trim().length > 255) {
      setError('El comentario no debe exceder 255 caracteres.')
      return
    }

    setIsSaving(true)
    setError('')
    setSuccess('')
    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias/${selected.folio}/historial`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          estadoId: Number(form.estadoId),
          fecha: form.fecha || null,
          comentario: form.comentario.trim() || null,
          idTecnico: tecnicoId
        })
      })
      const payload = await response.json().catch(() => null)
      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudo registrar el evento.'))
      }

      setSuccess('Evento registrado correctamente.')
      await openHistory(selected.folio)
      await loadIncidencias()
    } catch (requestError) {
      setError(requestError.message || 'Error al registrar evento.')
    } finally {
      setIsSaving(false)
    }
  }

  if (!sessionUser) {
    return (
      <main className="simple-page">
        <h1>Sesion no iniciada</h1>
        <p>Debes iniciar sesion para consultar incidencias asignadas.</p>
        <p><a href="#/login">Ir a Login</a></p>
      </main>
    )
  }

  if (!isTecnico) {
    return (
      <main className="simple-page">
        <h1>Acceso restringido</h1>
        <p>Este modulo esta disponible solo para Tecnico.</p>
      </main>
    )
  }

  return (
    <main className="users-page">
      <section className="users-header">
        <h1>Incidencias Asignadas</h1>
        <p>Haz clic en una incidencia para ver su historia y registrar eventos de avance.</p>
      </section>

      <section className="users-card">
        <div className="asset-actions panel-actions">
          <button type="button" className="secondary" onClick={loadIncidencias} disabled={isLoading}>
            {isLoading ? 'Actualizando...' : 'Actualizar'}
          </button>
        </div>

        {isLoading && <p>Cargando incidencias...</p>}
        {!isLoading && incidencias.length === 0 && <p>No tienes incidencias asignadas.</p>}

        {!isLoading && incidencias.length > 0 && (
          <div className="users-table-wrap">
            <table className="users-table">
              <thead>
                <tr>
                  <th>Folio</th>
                  <th>Fecha</th>
                  <th>Estatus</th>
                  <th>Prioridad</th>
                  <th>Tipo</th>
                  <th>Tiempo</th>
                </tr>
              </thead>
              <tbody>
                {incidencias.map((incidencia) => (
                  <tr
                    key={incidencia.folio}
                    className="panel-row-clickable"
                    onClick={() => openHistory(incidencia.folio)}
                  >
                    <td>{incidencia.folio}</td>
                    <td>{formatDate(incidencia.fechaCreacion)}</td>
                    <td>{incidencia.estatus || '-'}</td>
                    <td>{prioridadLabel(incidencia.prioridad)}</td>
                    <td>{tipoLabel(incidencia.tipo)}</td>
                    <td>{incidencia.tiempoEstimado ? `${incidencia.tiempoEstimado} h` : '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>

      {error && <p className="users-message error">{error}</p>}
      {success && <p className="users-message success">{success}</p>}

      {selected && (
        <div className="dialog-overlay" role="dialog" aria-modal="true" aria-labelledby="historia-title">
          <section className="dialog-card">
            <h2 id="historia-title">Historia de incidencia #{selected.folio}</h2>
            {isLoadingHistory && <p>Cargando historia...</p>}

            {!isLoadingHistory && (
              <>
                <div className="tecnico-dialog-grid">
                  <div className="tecnico-left-column">
                    <section className="incidencia-details">
                      <dl className="incidencia-detail-grid">
                        <div>
                          <dt>Estatus</dt>
                          <dd>{selected.estatus || '-'}</dd>
                        </div>
                        <div>
                          <dt>Fecha</dt>
                          <dd>{formatDate(selected.fechaCreacion)}</dd>
                        </div>
                        <div>
                          <dt>Prioridad</dt>
                          <dd>{prioridadLabel(selected.prioridad)}</dd>
                        </div>
                        <div>
                          <dt>Tipo</dt>
                          <dd>{tipoLabel(selected.tipo)}</dd>
                        </div>
                        <div>
                          <dt>Tiempo estimado</dt>
                          <dd>{selected.tiempoEstimado ? `${selected.tiempoEstimado} h` : '-'}</dd>
                        </div>
                        <div>
                          <dt>Solicitante</dt>
                          <dd>{selected.solicitante || '-'}</dd>
                        </div>
                      </dl>
                      <div className="incidencia-descripcion">
                        <h4>Descripcion</h4>
                        <p>{selected.descripcion || '-'}</p>
                      </div>
                    </section>

                    <form className="dialog-form" onSubmit={handleSubmit}>
                      <h3>Registrar nuevo evento</h3>

                      <label className="form-field">
                        <span>Estado</span>
                      <select name="estadoId" value={form.estadoId} onChange={handleChange} required>
                        <option value="">Seleccionar estado</option>
                        {estadosDisponibles.map((estado) => (
                          <option key={estado.id} value={estado.id}>
                            {estado.nombre}
                          </option>
                        ))}
                      </select>
                      </label>

                      <label className="form-field">
                        <span>Fecha</span>
                        <input type="date" name="fecha" value={form.fecha} onChange={handleChange} />
                      </label>

                      <label className="form-field">
                        <span>Comentario</span>
                        <textarea
                          name="comentario"
                          value={form.comentario}
                          onChange={handleChange}
                          rows={3}
                          maxLength={255}
                          placeholder="Describe avance, diagnostico o solucion aplicada."
                        />
                      </label>

                      <div className="dialog-actions">
                        <button type="button" className="secondary" onClick={closeHistory} disabled={isSaving}>
                          Cerrar
                        </button>
                        <button type="submit" disabled={isSaving}>
                          {isSaving ? 'Guardando...' : 'Agregar entrada'}
                        </button>
                      </div>
                    </form>
                  </div>

                  <section className="tecnico-history">
                    <h3>Eventos registrados</h3>
                    {Array.isArray(selected.historial) && selected.historial.length > 0 ? (
                      <ul className="tecnico-history-list">
                        {selected.historial.map((item) => (
                          <li key={`${item.idEvento}-${item.fecha || ''}`}>
                            <p><strong>{item.estado || '-'}</strong> | {formatDate(item.fecha)}</p>
                            <p>{item.comentario || 'Sin comentario'}</p>
                          </li>
                        ))}
                      </ul>
                    ) : (
                      <p>No hay eventos registrados para esta incidencia.</p>
                    )}
                  </section>
                </div>
              </>
            )}
          </section>
        </div>
      )}
    </main>
  )
}

export default TecnicoIncidenciasPage
