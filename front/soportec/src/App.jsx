import { useEffect, useMemo, useState } from 'react'
import './App.css'
import Home from './pages/Home'
import UsuariosCrud from './pages/usuariosCrud'
import Login from './pages/Login'
import CrearActivoPage from './pages/CrearActivoPage'
import Bienvenida from './pages/Bienvenida'
import IncidenciaPage from './pages/IncidenciaPage'
import IncidenciasPanelPage from './pages/IncidenciasPanelPage'
import TecnicoIncidenciasPage from './pages/TecnicoIncidenciasPage'
import SolicitanteIncidenciasPage from './pages/SolicitanteIncidenciasPage'

const API_URL = import.meta.env.VITE_API_URL;

const ROUTES = {
  home: '/',
  usuarios: '/usuarios',
  login: '/login',
  bienvenida: '/bienvenida',
  activos: '/activos/nuevo',
  incidencias: '/incidencias/nueva',
  incidenciasPanel: '/incidencias/panel',
  incidenciasTecnico: '/incidencias/tecnico',
  incidenciasSolicitante: '/incidencias/solicitante',
  init: '/init'
}

const NAV_ITEMS = [
  { to: ROUTES.home, label: 'Inicio' },
  { to: ROUTES.usuarios, label: 'Usuarios CRUD' },
  { to: ROUTES.login, label: 'Login' },
  { to: ROUTES.bienvenida, label: 'Bienvenida' },
  { to: ROUTES.activos, label: 'Registrar Activo' },
  { to: ROUTES.incidencias, label: 'Generar Incidencia' },
  { to: ROUTES.incidenciasPanel, label: 'Panel Incidencias' },
  { to: ROUTES.incidenciasTecnico, label: 'Panel Tecnico' },
  { to: ROUTES.incidenciasSolicitante, label: 'Mis Incidencias' }
]

function normalizeHash(hash) {
  if (!hash || hash === '#') {
    return ROUTES.login
  }

  const path = hash.replace(/^#/, '') || ROUTES.home
  return path.startsWith('/') ? path : `/${path}`
}

function App() {
  const [path, setPath] = useState(() => normalizeHash(window.location.hash))

  useEffect(() => {
    if (!window.location.hash) {
      window.location.hash = ROUTES.home
    }

    const handleHashChange = () => {
      setPath(normalizeHash(window.location.hash))
    }

    window.addEventListener('hashchange', handleHashChange)
    return () => window.removeEventListener('hashchange', handleHashChange)
  }, [])

  const page = useMemo(() => {
    if (path === ROUTES.home) {
      return <Home />
    }

    if (path === ROUTES.usuarios) {
      return <UsuariosCrud />
    }

    if (path === ROUTES.login) {
      return <Login />
    }

    if (path === ROUTES.bienvenida) {
      return <Bienvenida />
    }

    if (path === ROUTES.activos) {
      return <CrearActivoPage />
    }

    if (path === ROUTES.incidencias) {
      return <IncidenciaPage />
    }

    if (path === ROUTES.incidenciasPanel) {
      return <IncidenciasPanelPage />
    }

    if (path === ROUTES.incidenciasTecnico) {
      return <TecnicoIncidenciasPage />
    }

    if (path === ROUTES.incidenciasSolicitante) {
      return <SolicitanteIncidenciasPage />
    }


    return (
      <section className="simple-page">
        <h1>Pagina no encontrada</h1>
        <p>La ruta <code>{path}</code> no existe.</p>
      </section>
    )
  }, [path])

  return (
    <div className="app-shell">
      <header className="app-header">
        <h1>Soportec</h1>
        <nav className="app-nav" aria-label="Navegacion principal">
          {NAV_ITEMS.map((item) => (
            <a
              key={item.to}
              href={`#${item.to}`}
              className={path === item.to ? 'active' : ''}
            >
              {item.label}
            </a>
          ))}
        </nav>
      </header>
      {page}
    </div>
  )
}

export default App
