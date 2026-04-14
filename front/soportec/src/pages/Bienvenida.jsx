const SESSION_USER_KEY = 'soportec.auth.user'

function getRoleConfig(nivelId) {
  if (nivelId === 1) {
    return {
      title: 'Panel de Jefe de Taller',
      description: 'Clasifica y asigna tickets a tecnicos para su atencion.',
      actions: [
        'Revisar tickets nuevos pendientes de clasificacion.',
        'Asignar ticket al tecnico disponible segun prioridad.',
        'Monitorear estatus de resolucion por tecnico.'
      ]
    }
  }

  if (nivelId === 2) {
    return {
      title: 'Panel de Tecnico',
      description: 'Gestiona tickets asignados y documenta soluciones aplicadas.',
      actions: [
        'Consultar tickets asignados por el jefe de taller.',
        'Actualizar avance y registrar evidencias tecnicas.',
        'Marcar ticket como resuelto y enviar cierre.'
      ]
    }
  }

  if (nivelId === 3) {
    return {
      title: 'Panel de Encargado de Area',
      description: 'Genera tickets para que el jefe de taller los clasifique y asigne.',
      actions: [
        'Crear ticket con detalle del incidente o requerimiento.',
        'Adjuntar informacion de contexto y prioridad.',
        'Dar seguimiento al ticket hasta su resolucion.'
      ]
    }
  }

  return {
    title: 'Panel de Usuario',
    description: 'No se reconocio el nivel de privilegio del usuario.',
    actions: ['Contacta al administrador para validar tu nivel de acceso.']
  }
}

function Bienvenida() {
  const raw = window.sessionStorage.getItem(SESSION_USER_KEY)
  let user = null

  try {
    user = raw ? JSON.parse(raw) : null
  } catch (error) {
    user = null
  }

  if (!user) {
    return (
      <main className="simple-page">
        <h1>Sesion no iniciada</h1>
        <p>Necesitas autenticarte para entrar a la bienvenida.</p>
        <p><a href="#/login">Ir a Login</a></p>
      </main>
    )
  }

  const nivelId = Number(user?.nivel?.id_nivel || 0)
  const nivelNombre = user?.nivel?.nombre || 'Sin nivel'
  const roleConfig = getRoleConfig(nivelId)

  return (
    <main className="welcome-page">
      <section className="welcome-card">
        <p className="welcome-kicker">Acceso validado</p>
        <h1>{roleConfig.title}</h1>
        <p className="welcome-lead">
          Bienvenido <strong>{user.nombre || user.username}</strong>. Tu nivel es{' '}
          <strong>{nivelNombre}</strong> ({nivelId}).
        </p>
        <p className="welcome-description">{roleConfig.description}</p>
      </section>

      <section className="welcome-card">
        <h2>Que puedes hacer ahora</h2>
        <ul className="welcome-list">
          {roleConfig.actions.map((item) => (
            <li key={item}>{item}</li>
          ))}
        </ul>
        <div className="welcome-actions">
          <a href="#/" className="welcome-link">Ir a inicio</a>
          <a href="#/activos/nuevo" className="welcome-link secondary">Registrar activo</a>
          <a href="#/incidencias/nueva" className="welcome-link secondary">Generar incidencia</a>
          <a href="#/incidencias/panel" className="welcome-link secondary">Panel de incidencias</a>
          <a href="#/incidencias/tecnico" className="welcome-link secondary">Panel tecnico</a>
          <a href="#/incidencias/solicitante" className="welcome-link secondary">Mis incidencias</a>
        </div>
      </section>
    </main>
  )
}

export default Bienvenida
