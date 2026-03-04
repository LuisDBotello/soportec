function SelectField({
  id,
  name,
  label,
  value,
  onChange,
  options,
  placeholder = 'Selecciona una opcion',
  required = false,
  disabled = false,
  loading = false,
  error
}) {
  return (
    <label className="form-field" htmlFor={id || name}>
      <span>
        {label}
        {required ? ' *' : ''}
      </span>
      <select
        id={id || name}
        name={name}
        value={value || ''}
        onChange={onChange}
        required={required}
        disabled={disabled || loading}
        aria-invalid={Boolean(error)}
        aria-describedby={error ? `${name}-error` : undefined}
      >
        <option value="">{loading ? 'Cargando...' : placeholder}</option>

        {(options || []).map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
      {error && (
        <small id={`${name}-error`} className="field-error">
          {error}
        </small>
      )}
    </label>
  )
}

export default SelectField
