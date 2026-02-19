function LoginField({ id, label, type = 'text', value, onChange, placeholder, autoComplete }) {
  return (
    <label className="login-field" htmlFor={id}>
      <span>{label}</span>
      <input
        id={id}
        name={id}
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        autoComplete={autoComplete}
        required
      />
    </label>
  )
}

export default LoginField
