import SelectField from '../fields/SelectField'

function UbicacionSection({ form, options, loading, errors, onChange }) {
  return (
    <section className="asset-section">
      <h2>Ubicacion (opcional)</h2>
      <div className="asset-grid">
        <SelectField
          name="edificioId"
          label="Edificio"
          value={form.edificioId}
          onChange={onChange}
          options={options.edificios}
          loading={loading.edificios}
          error={errors.edificioId}
          placeholder="Selecciona un edificio"
        />

        <SelectField
          name="espacioId"
          label="Espacio"
          value={form.espacioId}
          onChange={onChange}
          options={options.espacios}
          loading={loading.espacios}
          disabled={!form.edificioId}
          error={errors.espacioId}
          placeholder={form.edificioId ? 'Selecciona un espacio' : 'Primero selecciona edificio'}
        />
      </div>
    </section>
  )
}

export default UbicacionSection
