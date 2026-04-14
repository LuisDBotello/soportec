import { useCallback, useEffect, useMemo, useState } from 'react'

const API_URL = import.meta.env.VITE_API_URL
const SESSION_USER_KEY = 'soportec.auth.user'
const SOLICITANTE_NIVEL_ID = 3

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

function SolicitanteIncidenciasPage() {
  const [incidencias, setIncidencias] = useState([])
  const [selected, setSelected] = useState(null)
  const [isLoading, setIsLoading] = useState(false)
  const [isLoadingHistory, setIsLoadingHistory] = useState(false)
  const [isLiberando, setIsLiberando] = useState(false)
  const [liberarComentario, setLiberarComentario] = useState('')
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

  const isSolicitante = Number(sessionUser?.nivel?.id_nivel || 0) === SOLICITANTE_NIVEL_ID
  const solicitanteId = Number(sessionUser?.idUsuario || 0)

  const loadIncidencias = useCallback(async () => {
    if (!solicitanteId) {
      return
    }
    setIsLoading(true)
    setError('')
    setSuccess('')
    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias/solicitadas?solicitanteId=${encodeURIComponent(solicitanteId)}`)
      const payload = await response.json().catch(() => null)
      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudieron cargar tus incidencias.'))
      }
      setIncidencias(Array.isArray(payload) ? payload : [])
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar incidencias.')
    } finally {
      setIsLoading(false)
    }
  }, [solicitanteId])

  useEffect(() => {
    if (isSolicitante) {
      loadIncidencias()
    }
  }, [isSolicitante, loadIncidencias])

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
      setLiberarComentario('')
    } catch (requestError) {
      setError(requestError.message || 'Error al cargar historial.')
    } finally {
      setIsLoadingHistory(false)
    }
  }

  const closeDialog = () => {
    if (isLiberando) {
      return
    }
    setSelected(null)
  }

  const handleLiberar = async () => {
    if (!selected?.folio) {
      return
    }
    if (liberarComentario.trim().length > 255) {
      setError('El comentario no debe exceder 255 caracteres.')
      return
    }

    setIsLiberando(true)
    setError('')
    setSuccess('')
    try {
      const response = await fetch(`${API_URL}/ordenes/incidencias/${selected.folio}/liberar`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          solicitanteId,
          comentario: liberarComentario.trim() || null
        })
      })
      const payload = await response.json().catch(() => null)
      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudo liberar la incidencia.'))
      }

      setSuccess('Incidencia marcada como LIBERADO.')
      await openHistory(selected.folio)
      await loadIncidencias()
    } catch (requestError) {
      setError(requestError.message || 'Error al liberar incidencia.')
    } finally {
      setIsLiberando(false)
    }
  }

  if (!sessionUser) {
    return (
      <main className="simple-page">
        <h1>Sesion no iniciada</h1>
        <p>Debes iniciar sesion para consultar tus incidencias.</p>
        <p><a href="#/login">Ir a Login</a></p>
      </main>
    )
  }

  if (!isSolicitante) {
    return (
      <main className="simple-page">
        <h1>Acceso restringido</h1>
        <p>Este modulo esta disponible solo para solicitantes.</p>
      </main>
    )
  }

  return (
    <main className="users-page">
      <section className="users-header">
        <h1>Mis Incidencias</h1>
        <p>Consulta las incidencias que solicitaste y su historial de atencion.</p>
      </section>

      <section className="users-card">
        <div className="asset-actions panel-actions">
          <button type="button" className="secondary" onClick={loadIncidencias} disabled={isLoading}>
            {isLoading ? 'Actualizando...' : 'Actualizar'}
          </button>
        </div>

        {isLoading && <p>Cargando incidencias...</p>}
        {!isLoading && incidencias.length === 0 && <p>No has generado incidencias.</p>}

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
                  <th>Tecnico</th>
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
                    <td>{incidencia.encargado || 'Sin asignar'}</td>
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
        <div className="dialog-overlay" role="dialog" aria-modal="true" aria-labelledby="solicitud-title">
          <section className="dialog-card">
            <h2 id="solicitud-title">Historia de incidencia #{selected.folio}</h2>
            {isLoadingHistory && <p>Cargando historia...</p>}

            {!isLoadingHistory && (
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
                        <dt>Tecnico asignado</dt>
                        <dd>{selected.encargado || 'Sin asignar'}</dd>
                      </div>
                    </dl>
                    <div className="incidencia-descripcion">
                      <h4>Descripcion</h4>
                      <p>{selected.descripcion || '-'}</p>
                    </div>
                  </section>

                  <section className="incidencia-details">
                    <h3>Actualizar estatus a LIBERADO</h3>
                    <label className="form-field">
                      <span>Comentario (opcional)</span>
                      <textarea
                        rows={3}
                        maxLength={255}
                        value={liberarComentario}
                        onChange={(event) => setLiberarComentario(event.target.value)}
                        placeholder="Comentario de liberacion."
                      />
                    </label>
                    <div className="dialog-actions">
                      <button type="button" className="secondary" onClick={closeDialog} disabled={isLiberando}>
                        Cerrar
                      </button>
                      <button type="button" onClick={handleLiberar} disabled={isLiberando || selected.estatus === 'LIBERADO'}>
                        {isLiberando ? 'Liberando...' : selected.estatus === 'LIBERADO' ? 'Ya liberada' : 'Marcar LIBERADO'}
                      </button>
                    </div>
                  </section>
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
            )}
          </section>
        </div>
      )}
    </main>
  )
}

export default SolicitanteIncidenciasPage
