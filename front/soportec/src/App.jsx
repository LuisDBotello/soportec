import { useEffect, useMemo, useState } from 'react'
import './App.css'
import Home from './pages/Home'
import UsuariosCrud from './pages/usuariosCrud'
import Login from './pages/Login'

const API_URL = import.meta.env.VITE_API_URL;

const ROUTES = {
  home: '/',
  usuarios: '/usuarios',
  login: '/login',
  init: '/init'
}

const NAV_ITEMS = [
  { to: ROUTES.home, label: 'Inicio' },
  { to: ROUTES.usuarios, label: 'Usuarios CRUD' },
  { to: ROUTES.login, label: 'Login' }
]

function normalizeHash(hash) {
  if (!hash || hash === '#') {
    return ROUTES.home
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
