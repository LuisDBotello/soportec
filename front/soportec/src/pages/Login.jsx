import { useState } from 'react'
import BrandPanel from '../components/BrandPanel'
import LoginForm from '../components/LoginForm'

const LOGIN_URL = `${String(import.meta.env.VITE_API_URL || '').replace(/\/api\/?$/, '')}/login`
const SESSION_USER_KEY = 'soportec.auth.user'

const INITIAL_VALUES = {
  username: '',
  password: ''
}

function Login() {
  const [values, setValues] = useState(INITIAL_VALUES)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')

  const handleChange = (event) => {
    const { name, value } = event.target
    setValues((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setIsLoading(true)
    setError('')

    if (!values.username.trim() || !values.password.trim()) {
      setError('Captura usuario y contrasena para continuar.')
      setIsLoading(false)
      return
    }

    try {
      const response = await fetch(LOGIN_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: values.username.trim(),
          password: values.password
        })
      })

      const data = await response.json().catch(() => ({}))
      if (!response.ok) {
        throw new Error(data?.message || 'No se pudo validar el acceso.')
      }

      if (!data?.user?.nivel?.id_nivel) {
        throw new Error('Respuesta de login incompleta: no se encontro el nivel de usuario.')
      }

      window.sessionStorage.setItem(SESSION_USER_KEY, JSON.stringify(data.user))
      window.location.hash = '/bienvenida'
    } catch (err) {
      setError(err.message || 'Error de autenticacion.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <main className="login-page">
      <BrandPanel />
      <LoginForm
        values={values}
        onChange={handleChange}
        onSubmit={handleSubmit}
        isLoading={isLoading}
        error={error}
      />
    </main>
  )
}

export default Login
