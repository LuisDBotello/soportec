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
        <a className="home-link-card" href="#/login">
          <h3>Acceso al sistema</h3>
          <p>Pantalla base de inicio de sesion para el portal.</p>
        </a>
      </section>
    </main>
  )
}

export default Home
