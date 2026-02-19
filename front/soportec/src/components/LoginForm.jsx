import LoginField from './LoginField'
import LoginHelp from './LoginHelp'

function LoginForm({ values, onChange, onSubmit, isLoading, error }) {
  return (
    <section className="login-card" aria-label="Formulario de inicio de sesion">
      <p className="login-eyebrow">Acceso</p>
      <h2>Iniciar sesion</h2>
      <p className="login-copy">Usa tu cuenta institucional para entrar al sistema Soportec.</p>

      <form onSubmit={onSubmit} className="login-form">
        <LoginField
          id="username"
          label="Usuario"
          value={values.username}
          onChange={onChange}
          placeholder="ej. luis.diaz"
          autoComplete="username"
        />

        <LoginField
          id="password"
          label="Contrasena"
          type="password"
          value={values.password}
          onChange={onChange}
          placeholder="Ingresa tu contraseña"
          autoComplete="current-password"
        />

        {error && <p className="login-error">{error}</p>}

        <button type="submit" className="login-button" disabled={isLoading}>
          {isLoading ? 'Validando...' : 'Entrar al portal'}
        </button>
      </form>

      <LoginHelp />
    </section>
  )
}

export default LoginForm
