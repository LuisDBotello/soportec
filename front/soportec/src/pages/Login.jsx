import { useState } from 'react'
import BrandPanel from '../components/BrandPanel'
import LoginForm from '../components/LoginForm'

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

  const handleSubmit = (event) => {
    event.preventDefault()
    setIsLoading(true)
    setError('')

    window.setTimeout(() => {
      if (!values.username.trim() || !values.password.trim()) {
        setError('Captura usuario y contrasena para continuar.')
      }
      setIsLoading(false)
    }, 500)
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
