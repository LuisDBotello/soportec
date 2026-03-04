function InputField({
  id,
  name,
  label,
  value,
  onChange,
  error,
  type = 'text',
  required = false,
  placeholder = '',
  disabled = false,
  min,
  max,
  step,
  maxLength
}) {
  return (
    <label className="form-field" htmlFor={id || name}>
      <span>
        {label}
        {required ? ' *' : ''}
      </span>
      <input
        id={id || name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        required={required}
        placeholder={placeholder}
        disabled={disabled}
        min={min}
        max={max}
        step={step}
        maxLength={maxLength}
        aria-invalid={Boolean(error)}
        aria-describedby={error ? `${name}-error` : undefined}
      />
      {error && (
        <small id={`${name}-error`} className="field-error">
          {error}
        </small>
      )}
    </label>
  )
}

export default InputField
