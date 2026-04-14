function Home() {
  return (
    <main className="simple-page">
      <h2>Inicio</h2>
      <p>
        Bienvenido al portal Soportec. Desde aqui puedes navegar a los modulos principales.
      </p>
      <section className="home-links" aria-label="Accesos rapidos">
        <a className="home-link-card" href="#/usuarios">
          <h3>Gestion de usuarios</h3>
          <p>Alta, edicion y eliminacion de usuarios desde el CRUD.</p>
        </a>
        <a className="home-link-card" href="#/activos/nuevo">
          <h3>Registrar activo</h3>
          <p>Formulario dinamico para alta de activos con componentes.</p>
        </a>
        <a className="home-link-card" href="#/incidencias/nueva">
          <h3>Generar incidencia</h3>
          <p>Reporta falla de un activo por espacio y crea una orden.</p>
        </a>
        <a className="home-link-card" href="#/incidencias/panel">
          <h3>Panel de incidencias</h3>
          <p>Vista cronologica para seguimiento del encargado de taller.</p>
        </a>
        <a className="home-link-card" href="#/incidencias/tecnico">
          <h3>Panel tecnico</h3>
          <p>Consulta incidencias asignadas y registra eventos de atencion.</p>
        </a>
        <a className="home-link-card" href="#/incidencias/solicitante">
          <h3>Mis incidencias</h3>
          <p>Consulta el historial de tus solicitudes y libera la orden.</p>
        </a>
        <a className="home-link-card" href="#/login">
          <h3>Acceso al sistema</h3>
          <p>Pantalla base de inicio de sesion para el portal.</p>
        </a>
      </section>
    </main>
  )
}

export default Home
