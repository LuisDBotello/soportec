import InputField from '../fields/InputField'
import SelectField from '../fields/SelectField'

function DatosGenerales({
  form,
  errors,
  options,
  loading,
  isEscritorio,
  onChange
}) {
  return (
    <section className="asset-section">
      <h2>Datos Generales</h2>
      <div className="asset-grid">
        <SelectField
          name="categoriaId"
          label="Categoria"
          value={form.categoriaId}
          onChange={onChange}
          options={options.categorias}
          required
          loading={loading.categorias}
          error={errors.categoriaId}
          placeholder="Selecciona una categoria"
        />

        <SelectField
          name="tipoActivoId"
          label="Tipo de activo"
          value={form.tipoActivoId}
          onChange={onChange}
          options={options.tiposActivo}
          required
          loading={loading.tiposActivo}
          disabled={!form.categoriaId}
          error={errors.tipoActivoId}
          placeholder={form.categoriaId ? 'Selecciona un tipo' : 'Primero selecciona categoria'}
        />

        <InputField
          name="fechaCompra"
          label={isEscritorio ? 'Fecha de armado' : 'Fecha de compra'}
          type="datetime-local"
          value={form.fechaCompra}
          onChange={onChange}
          required
          error={errors.fechaCompra}
        />

        <SelectField
          name="estadoId"
          label="Estado"
          value={form.estadoId}
          onChange={onChange}
          options={options.estados}
          required
          loading={loading.estados}
          error={errors.estadoId}
          placeholder="Selecciona un estado"
        />

        {!isEscritorio && (
          <SelectField
            name="marcaGeneralId"
            label="Marca"
            value={form.marcaGeneral}
            onChange={onChange}
            options={options.marcasActivo}
            required
            loading={loading.marcasActivo}
            error={errors.marcaGeneral}
            placeholder="Selecciona una marca"
          />
        )}

        {!isEscritorio && (
          <SelectField
            name="modeloGeneralId"
            label="Modelo"
            value={form.modeloGeneral}
            onChange={onChange}
            options={options.modelosActivo}
            required
            loading={loading.modelosActivo}
            disabled={!form.marcaGeneral}
            error={errors.modeloGeneral}
            placeholder={form.marcaGeneral ? 'Selecciona un modelo' : 'Primero selecciona marca'}
          />
        )}

        {!isEscritorio && (
          <InputField
            name="numeroSerieGeneral"
            label="Numero de serie general"
            value={form.numeroSerieGeneral}
            onChange={onChange}
            required
            maxLength={120}
            error={errors.numeroSerieGeneral}
          />
        )}
      </div>
    </section>
  )
}

export default DatosGenerales
