import SelectField from '../fields/SelectField'
import InputField from '../fields/InputField'

function buildModeloResumen(modeloRaw) {
  if (!modeloRaw) {
    return ''
  }

  const parts = []

  const push = (value, label = '') => {
    if (value === undefined || value === null || String(value).trim() === '') {
      return
    }
    parts.push(label ? `${label}: ${value}` : String(value))
  }

  push(modeloRaw.nucleos, 'Cores')
  push(modeloRaw.hilos, 'Hilos')
  push(modeloRaw.frecuenciaBaseGhz, 'GHz base')
  push(modeloRaw.frecuenciaBoostGhz, 'GHz boost')
  push(modeloRaw.capacidadGb, 'Capacidad GB')
  push(modeloRaw.tipo, 'Tipo RAM')
  push(modeloRaw.tipoMemoria, 'Tipo')
  push(modeloRaw.socket, 'Socket')
  push(modeloRaw.chipset, 'Chipset')
  push(modeloRaw.factorForma, 'Factor forma')
  push(modeloRaw.maxMemoriaGb, 'Max memoria GB')
  push(modeloRaw.ranurasRam, 'Ranuras RAM')
  if (modeloRaw.soporteM2 !== undefined && modeloRaw.soporteM2 !== null) {
    push(modeloRaw.soporteM2 ? 'Si' : 'No', 'Soporta M.2')
  }
  push(modeloRaw.soporteSata, 'Puertos SATA')
  push(modeloRaw.interfaz, 'Interfaz')
  push(modeloRaw.tipoInterfaz, 'Tipo interfaz')
  push(modeloRaw.tipoConexion, 'Tipo conexion')
  push(modeloRaw.velocidadMaxima, 'Velocidad maxima')
  if (modeloRaw.soportaWifi !== undefined && modeloRaw.soportaWifi !== null) {
    push(modeloRaw.soportaWifi ? 'Si' : 'No', 'Soporta WiFi')
  }
  push(modeloRaw.frecuenciaWifi, 'Frecuencia WiFi')
  if (modeloRaw.soportaVlan !== undefined && modeloRaw.soportaVlan !== null) {
    push(modeloRaw.soportaVlan ? 'Si' : 'No', 'Soporta VLAN')
  }
  push(modeloRaw.velocidadLecturaMbps ?? modeloRaw.velocidadLectura, 'Lectura MB/s')
  push(modeloRaw.velocidadEscrituraMbps ?? modeloRaw.velocidadEscritura, 'Escritura MB/s')
  push(modeloRaw.velocidadRpm, 'RPM')
  push(modeloRaw.velocidadMhz, 'MHz')
  push(modeloRaw.estandar, 'Estandar')
  push(modeloRaw.arquitectura)

  return parts.join(' | ')
}

function formatFechaCompra(value) {
  if (!value) {
    return 'Sin fecha de compra'
  }

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return String(value)
  }

  return date.toLocaleString('es-MX')
}

function getMarcaId(raw) {
  return String(
    raw?.marcaCpu?.idMarcaCpu
      ?? raw?.marcaRam?.idMarcaRam
      ?? raw?.marcaDisco?.idMarcaDisco
      ?? raw?.marcaMb?.idMarcaMb
      ?? raw?.marcaNic?.idMarcaNic
      ?? raw?.idMarca
      ?? ''
  )
}

function getModeloId(raw) {
  return String(
    raw?.idModeloCpu
      ?? raw?.idModeloRam
      ?? raw?.idModeloDisco
      ?? raw?.idModeloMb
      ?? raw?.idModeloNic
      ?? raw?.idModelo
      ?? ''
  )
}

function ComponentesEquipo({
  componentes,
  componentesExtra,
  modelCatalogs,
  loading,
  errors,
  onChange,
  onMarcaChange,
  onExtraAdd,
  onExtraChange,
  onExtraMarcaChange,
  onExtraRemove,
  stockState,
  stockFilters,
  onStockModeChange,
  onStockDisponibleChange,
  onStockMarcaFilterChange,
  onStockModeloFilterChange
}) {
  const definitions = [
    { key: 'cpu', label: 'CPU' },
    { key: 'ram', label: 'RAM' },
    { key: 'motherboard', label: 'Motherboard' },
    { key: 'disco', label: 'Disco' },
    { key: 'nic', label: 'NIC' }
  ]

  return (
    <section className="asset-section">
      <h2>Componentes</h2>
      <p className="asset-section-help">Esta seccion solo aplica a tipo de activo ESCRITORIO.</p>

      <div className="componentes-list">
        {definitions.map((definition) => {
          const componentKey = definition.key
          const componentState = componentes[componentKey]
          const componentCatalog = modelCatalogs[componentKey]
          const modelosByMarca = componentCatalog.modelosByMarca || {}
          const modelosOptions = componentState.marcaId
            ? modelosByMarca[componentState.marcaId] || componentCatalog.modelos
            : []
          const selectedModelo = modelosOptions.find((item) => item.value === componentState.modeloId)
          const stock = stockState[componentKey]
          const filters = stockFilters[componentKey]
          const isDisponible = stock.mode === 'disponible'
          const allowsExtra = componentKey === 'ram' || componentKey === 'disco'
          const extras = allowsExtra ? componentesExtra[componentKey] : []

          const filteredOptions = !isDisponible
            ? stock.options
            : stock.options.filter((option) => {
                const rawModelo = option?.raw?.modeloCpu
                  ?? option?.raw?.modeloRam
                  ?? option?.raw?.modeloDisco
                  ?? option?.raw?.modeloMb
                  ?? option?.raw?.modeloNic

                const optionMarcaId = getMarcaId(rawModelo)
                const optionModeloId = getModeloId(rawModelo)

                const matchMarca = !filters.marcaId || optionMarcaId === filters.marcaId
                const matchModelo = !filters.modeloId || optionModeloId === filters.modeloId
                return matchMarca && matchModelo
              })

          const stockSelected = isDisponible
            ? stock.options.find((item) => item.value === stock.selectedId)
            : null

          const stockSelectedRawModelo = stockSelected?.raw?.modeloCpu
            ?? stockSelected?.raw?.modeloRam
            ?? stockSelected?.raw?.modeloDisco
            ?? stockSelected?.raw?.modeloMb
            ?? stockSelected?.raw?.modeloNic

          const stockResumen = buildModeloResumen(stockSelectedRawModelo)
          const stockFechaCompra = formatFechaCompra(stockSelected?.raw?.fechaCompra)
          const modeloResumen = buildModeloResumen(selectedModelo?.raw)

          return (
            <article key={componentKey} className="component-card">
              <h3>{definition.label}</h3>

              <div className="asset-grid">
                <SelectField
                  name={`${componentKey}.modo`}
                  label={`Origen de ${definition.label}`}
                  value={stock.mode}
                  onChange={(event) => onStockModeChange(componentKey, event.target.value)}
                  options={[
                    { value: 'nuevo', label: `${definition.label} nuevo` },
                    { value: 'disponible', label: `Asignar ${definition.label} disponible` }
                  ]}
                  required
                  error={errors[`${componentKey}.modo`]}
                />

                {isDisponible && (
                  <SelectField
                    name={`${componentKey}.filtroMarca`}
                    label="Filtrar por marca"
                    value={filters.marcaId}
                    onChange={(event) => onStockMarcaFilterChange(componentKey, event.target.value)}
                    options={componentCatalog.marcas}
                    placeholder="Todas las marcas"
                  />
                )}

                {isDisponible && (
                  <SelectField
                    name={`${componentKey}.filtroModelo`}
                    label="Filtrar por modelo"
                    value={filters.modeloId}
                    onChange={(event) => onStockModeloFilterChange(componentKey, event.target.value)}
                    options={filters.modelos}
                    loading={filters.loadingModelos}
                    disabled={!filters.marcaId}
                    placeholder={filters.marcaId ? 'Todos los modelos' : 'Primero selecciona marca'}
                  />
                )}

                {isDisponible && (
                  <p className="cpu-counter">
                    {definition.label} disponibles: {filteredOptions.length} de {stock.options.length}
                  </p>
                )}

                {isDisponible && (
                  <SelectField
                    name={`${componentKey}.disponibleId`}
                    label={`${definition.label} disponible`}
                    value={stock.selectedId}
                    onChange={(event) => onStockDisponibleChange(componentKey, event.target.value)}
                    options={filteredOptions}
                    loading={stock.loading}
                    required
                    error={errors[`${componentKey}.disponibleId`]}
                    placeholder={`Selecciona un ${definition.label} de stock`}
                  />
                )}
              </div>

              {isDisponible && stockSelected && (
                <>
                  <p className="modelo-inline-resumen" title={`Fecha de compra: ${stockFechaCompra}`}>
                    Fecha de compra: {stockFechaCompra}
                  </p>
                  <p className="modelo-inline-resumen" title={stockResumen || 'Sin especificaciones'}>
                    {stockResumen || 'Sin especificaciones del modelo'}
                  </p>
                </>
              )}

              {!isDisponible && (
                <div className="asset-grid compact">
                  <SelectField
                    name={`${componentKey}.marcaId`}
                    label="Marca"
                    value={componentState.marcaId}
                    onChange={(event) => onMarcaChange(componentKey, event.target.value)}
                    options={componentCatalog.marcas}
                    loading={loading[componentKey]?.marcas}
                    required
                    placeholder="Selecciona una marca"
                    error={errors[`${componentKey}.marcaId`]}
                  />

                  <SelectField
                    name={`${componentKey}.modeloId`}
                    label="Modelo"
                    value={componentState.modeloId}
                    onChange={(event) => onChange(componentKey, 'modeloId', event.target.value)}
                    options={modelosOptions}
                    loading={loading[componentKey]?.modelos}
                    disabled={!componentState.marcaId}
                    required
                    placeholder={componentState.marcaId ? 'Selecciona un modelo' : 'Primero selecciona marca'}
                    error={errors[`${componentKey}.modeloId`]}
                  />

                  <InputField
                    name={`${componentKey}.numeroSerie`}
                    label="Numero de serie"
                    value={componentState.numeroSerie}
                    onChange={(event) => onChange(componentKey, 'numeroSerie', event.target.value)}
                    required
                    maxLength={120}
                    error={errors[`${componentKey}.numeroSerie`]}
                  />

                  <InputField
                    name={`${componentKey}.fechaCompra`}
                    label={`Fecha de compra ${definition.label}`}
                    type="date"
                    value={componentState.fechaCompra || ''}
                    onChange={(event) => onChange(componentKey, 'fechaCompra', event.target.value)}
                    required
                    error={errors[`${componentKey}.fechaCompra`]}
                  />
                </div>
              )}

              {!isDisponible && selectedModelo?.specs && (
                <details className="modelo-details">
                  <summary>Ver especificaciones</summary>
                  <p>{selectedModelo.specs}</p>
                </details>
              )}

              {!isDisponible && modeloResumen && (
                <p className="modelo-inline-resumen" title={modeloResumen}>
                  {modeloResumen}
                </p>
              )}

              {allowsExtra && (
                <div className="extra-componentes-wrap">
                  {extras.map((extra, index) => {
                    const extraModelosOptions = extra.marcaId
                      ? modelosByMarca[extra.marcaId] || []
                      : []
                    const extraSelectedModelo = extraModelosOptions.find((item) => item.value === extra.modeloId)
                    const extraResumen = buildModeloResumen(extraSelectedModelo?.raw)
                    const cardNumber = index + 2

                    return (
                      <article
                        key={`${componentKey}-extra-${index}`}
                        className="component-card extra-component-card"
                      >
                        <div className="extra-card-header">
                          <h4>{definition.label} adicional #{cardNumber}</h4>
                          <button
                            type="button"
                            className="extra-remove-btn"
                            onClick={() => onExtraRemove(componentKey, index)}
                          >
                            Quitar
                          </button>
                        </div>
                        <div className="asset-grid compact">
                          <SelectField
                            name={`${componentKey}.extra.${index}.marcaId`}
                            label="Marca"
                            value={extra.marcaId}
                            onChange={(event) => onExtraMarcaChange(componentKey, index, event.target.value)}
                            options={componentCatalog.marcas}
                            loading={loading[componentKey]?.marcas}
                            required
                            placeholder="Selecciona una marca"
                            error={errors[`${componentKey}.extra.${index}.marcaId`]}
                          />

                          <SelectField
                            name={`${componentKey}.extra.${index}.modeloId`}
                            label="Modelo"
                            value={extra.modeloId}
                            onChange={(event) => onExtraChange(componentKey, index, 'modeloId', event.target.value)}
                            options={extraModelosOptions}
                            loading={loading[componentKey]?.modelos}
                            disabled={!extra.marcaId}
                            required
                            placeholder={extra.marcaId ? 'Selecciona un modelo' : 'Primero selecciona marca'}
                            error={errors[`${componentKey}.extra.${index}.modeloId`]}
                          />

                          <InputField
                            name={`${componentKey}.extra.${index}.numeroSerie`}
                            label="Numero de serie"
                            value={extra.numeroSerie}
                            onChange={(event) => onExtraChange(componentKey, index, 'numeroSerie', event.target.value)}
                            required
                            maxLength={120}
                            error={errors[`${componentKey}.extra.${index}.numeroSerie`]}
                          />

                          <InputField
                            name={`${componentKey}.extra.${index}.fechaCompra`}
                            label={`Fecha de compra ${definition.label}`}
                            type="date"
                            value={extra.fechaCompra || ''}
                            onChange={(event) => onExtraChange(componentKey, index, 'fechaCompra', event.target.value)}
                            required
                            error={errors[`${componentKey}.extra.${index}.fechaCompra`]}
                          />
                        </div>

                        {extraResumen && (
                          <p className="modelo-inline-resumen" title={extraResumen}>
                            {extraResumen}
                          </p>
                        )}
                      </article>
                    )
                  })}

                  <button
                    type="button"
                    className="extra-add-card"
                    onClick={() => onExtraAdd(componentKey)}
                    aria-label={`Agregar ${definition.label} adicional`}
                    title={`Agregar ${definition.label} adicional`}
                  >
                    <span className="extra-add-plus"> Adicional + </span>
                  </button>
                </div>
              )}
            </article>
          )
        })}
      </div>
    </section>
  )
}

export default ComponentesEquipo
