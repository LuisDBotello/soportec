import { useEffect, useMemo, useState } from 'react'

const API_URL = 'http://localhost:8080/api/users'

const EMPTY_FORM = {
  nombre: '',
  apellidoP: '',
  apellidoM: '',
  correo: '',
  username: '',
  departamentoId: '',
  nivel: ''
}

function UserCrud() {
  const [users, setUsers] = useState([])
  const [form, setForm] = useState(EMPTY_FORM)
  const [editingId, setEditingId] = useState(null)
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const title = useMemo(() => (editingId ? 'Editar usuario' : 'Nuevo usuario'), [editingId])

  useEffect(() => {
    fetchUsers()
  }, [])

  const fetchUsers = async () => {
    try {
      setLoading(true)
      setError('')
      const response = await fetch(API_URL)
      if (!response.ok) {
        throw new Error('No se pudo obtener la lista de usuarios.')
      }
      const data = await response.json()
      setUsers(Array.isArray(data) ? data : [])
    } catch (err) {
      setError(err.message || 'Error al cargar usuarios.')
    } finally {
      setLoading(false)
    }
  }

  const handleChange = (event) => {
    const { name, type, value, checked } = event.target
    setForm((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }))
  }

  const resetForm = () => {
    setForm(EMPTY_FORM)
    setEditingId(null)
    setSuccess('')
    setError('')
  }

  const buildPayload = () => {
    const payload = {
      nombre: form.nombre.trim(),
      apellidoP: form.apellidoP.trim(),
      apellidoM: form.apellidoM.trim() || null,
      correo: form.correo.trim() || null,
      username: form.username.trim() || null,
      passwordHash: form.passwordHash.trim() || null,
      esJefeDepto: Boolean(form.esJefeDepto),
      nivel: form.nivel.trim()
    }

    if (form.departamentoId) {
      payload.departamento = { idDepartamento: Number(form.departamentoId) }
    }

    return payload
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setSaving(true)
    setError('')
    setSuccess('')

    const method = editingId ? 'PUT' : 'POST'
    const endpoint = editingId ? `${API_URL}/${editingId}` : API_URL

    try {
      const response = await fetch(endpoint, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(buildPayload())
      })

      if (!response.ok) {
        throw new Error(editingId ? 'No se pudo actualizar el usuario.' : 'No se pudo crear el usuario.')
      }

      await fetchUsers()
      resetForm()
      setSuccess(editingId ? 'Usuario actualizado correctamente.' : 'Usuario creado correctamente.')
    } catch (err) {
      setError(err.message || 'No se pudo guardar el usuario.')
    } finally {
      setSaving(false)
    }
  }

  const handleEdit = (user) => {
    setEditingId(user.idUsuario)
    setForm({
      nombre: user.nombre || '',
      apellidoP: user.apellidoP || '',
      apellidoM: user.apellidoM || '',
      correo: user.correo || '',
      username: user.username || '',
      departamentoId: String(user.departamento?.idDepartamento || ''),
      nivel: user.nivel || ''
    })
    setError('')
    setSuccess('')
  }

  const handleDelete = async (idUsuario) => {
    const confirmed = window.confirm('Deseas eliminar este usuario?')
    if (!confirmed) {
      return
    }

    try {
      setError('')
      setSuccess('')
      const response = await fetch(`${API_URL}/${idUsuario}`, { method: 'DELETE' })
      if (!response.ok) {
        throw new Error('No se pudo eliminar el usuario.')
      }

      if (editingId === idUsuario) {
        resetForm()
      }

      await fetchUsers()
      setSuccess('Usuario eliminado correctamente.')
    } catch (err) {
      setError(err.message || 'Error al eliminar usuario.')
    }
  }

  return (
    <main className="users-page">
      <section className="users-header">
        <h1>Soportec | CRUD de Usuarios</h1>
        <p>Gestiona cuentas de usuarios integradas con <code>/api/users</code>.</p>
      </section>

      <section className="users-grid">
        <article className="users-card">
          <h2>{title}</h2>
          <form className="users-form" onSubmit={handleSubmit}>
            <label>
              Nombre
              <input name="nombre" value={form.nombre} onChange={handleChange} required maxLength={120} />
            </label>
            <label>
              Apellido paterno
              <input name="apellidoP" value={form.apellidoP} onChange={handleChange} required maxLength={50} />
            </label>
            <label>
              Apellido materno
              <input name="apellidoM" value={form.apellidoM} onChange={handleChange} maxLength={50} />
            </label>
            <label>
              Correo
              <input name="correo" type="email" value={form.correo} onChange={handleChange} maxLength={100} />
            </label>
            <label>
              Username
              <input name="username" value={form.username} onChange={handleChange} maxLength={50} />
            </label>
            
            <label>
              ID departamento
              <input name="departamentoId" type="number" min="1" value={form.departamentoId} onChange={handleChange} required />
            </label>
            <label>
              Nivel
              <input name="nivel" value={form.nivel} onChange={handleChange} required maxLength={10} />
            </label>

            <div className="users-actions">
              <button type="submit" disabled={saving}>{saving ? 'Guardando...' : editingId ? 'Actualizar' : 'Crear'}</button>
              <button type="button" className="secondary" onClick={resetForm}>Limpiar</button>
            </div>
          </form>
        </article>

        <article className="users-card">
          <h2>Usuarios</h2>
          {loading && <p>Cargando usuarios...</p>}
          {!loading && users.length === 0 && <p>No hay usuarios registrados.</p>}
          {!loading && users.length > 0 && (
            <div className="users-table-wrap">
              <table className="users-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Username</th>
                    <th>Depto</th>
                    <th>Nivel</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((user) => (
                    <tr key={user.idUsuario}>
                      <td>{user.idUsuario}</td>
                      <td>{`${user.nombre || ''} ${user.apellidoP || ''}`.trim()}</td>
                      <td>{user.username || '-'}</td>
                      <td>{user.departamento?.idDepartamento || '-'}</td>
                      <td>{user.nivel || '-'}</td>
                      <td className="users-row-actions">
                        <button type="button" className="secondary" onClick={() => handleEdit(user)}>Editar</button>
                        <button type="button" className="danger" onClick={() => handleDelete(user.idUsuario)}>Eliminar</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </article>
      </section>

      {error && <p className="users-message error">{error}</p>}
      {success && <p className="users-message success">{success}</p>}
    </main>
  )
}

export default UserCrud
