import { useCallback, useEffect, useMemo, useState } from 'react'
import DatosGenerales from './DatosGenerales'
import ComponentesEquipo from './ComponentesEquipo'
import UbicacionSection from './UbicacionSection'

const API_URL = import.meta.env.VITE_API_URL

const EMPTY_FORM = {
  categoriaId: '',
  tipoActivoId: '',
  fechaCompra: '',
  estadoId: '',
  numeroSerieGeneral: '',
  edificioId: '',
  espacioId: ''
}

function getTodayDateInput() {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function createEmptyComponentes() {
  const today = getTodayDateInput()
  return {
    cpu: { marcaId: '', modeloId: '', numeroSerie: '', fechaCompra: today },
    ram: { marcaId: '', modeloId: '', numeroSerie: '', fechaCompra: today },
    motherboard: { marcaId: '', modeloId: '', numeroSerie: '', fechaCompra: today },
    disco: { marcaId: '', modeloId: '', numeroSerie: '', fechaCompra: today },
    nic: { marcaId: '', modeloId: '', numeroSerie: '', fechaCompra: today }
  }
}

const COMPONENT_DEFINITIONS = [
  { key: 'cpu', payloadType: 'CPU', endpoint: 'cpu', disponibleField: 'cpuDisponibleId' },
  { key: 'ram', payloadType: 'RAM', endpoint: 'ram', disponibleField: 'ramDisponibleId' },
  { key: 'motherboard', payloadType: 'MOTHERBOARD', endpoint: 'motherboard', disponibleField: 'motherboardDisponibleId' },
  { key: 'disco', payloadType: 'DISCO', endpoint: 'disco', disponibleField: 'discoDisponibleId' },
  { key: 'nic', payloadType: 'NIC', endpoint: 'nic', disponibleField: 'nicDisponibleId' }
]

const EMPTY_STOCK_STATE = {
  cpu: { mode: 'nuevo', selectedId: '', options: [], loading: false },
  ram: { mode: 'nuevo', selectedId: '', options: [], loading: false },
  motherboard: { mode: 'nuevo', selectedId: '', options: [], loading: false },
  disco: { mode: 'nuevo', selectedId: '', options: [], loading: false },
  nic: { mode: 'nuevo', selectedId: '', options: [], loading: false }
}

const EMPTY_STOCK_FILTERS = {
  cpu: { marcaId: '', modeloId: '', modelos: [], loadingModelos: false },
  ram: { marcaId: '', modeloId: '', modelos: [], loadingModelos: false },
  motherboard: { marcaId: '', modeloId: '', modelos: [], loadingModelos: false },
  disco: { marcaId: '', modeloId: '', modelos: [], loadingModelos: false },
  nic: { marcaId: '', modeloId: '', modelos: [], loadingModelos: false }
}

const EMPTY_LOADING_COMPONENTS = {
  cpu: { marcas: false, modelos: false },
  ram: { marcas: false, modelos: false },
  motherboard: { marcas: false, modelos: false },
  disco: { marcas: false, modelos: false },
  nic: { marcas: false, modelos: false }
}

const EMPTY_MODEL_CATALOGS = {
  cpu: { marcas: [], modelos: [] },
  ram: { marcas: [], modelos: [] },
  motherboard: { marcas: [], modelos: [] },
  disco: { marcas: [], modelos: [] },
  nic: { marcas: [], modelos: [] }
}

function parseResponseError(payload, fallback) {
  if (!payload) {
    return fallback
  }

  if (typeof payload === 'string') {
    return payload
  }

  return payload.message || payload.error || fallback
}

function extractId(item) {
  const candidates = [
    item?.id,
    item?.value,
    item?.idCategoriaActivo,
    item?.idCategoria,
    item?.idTipoActivo,
    item?.idEstado,
    item?.idEdificio,
    item?.idEspacio,
    item?.idMarcaCpu,
    item?.idModeloCpu,
    item?.idMarcaRam,
    item?.idModeloRam,
    item?.idMarcaDisco,
    item?.idModeloDisco,
    item?.idMarcaMb,
    item?.idModeloMb,
    item?.idMarcaMotherboard,
    item?.idModeloMotherboard,
    item?.idMarcaNic,
    item?.idModeloNic,
    item?.idMarca,
    item?.idModelo,
    item?.idUbicacion
  ]

  return candidates.find((value) => value !== undefined && value !== null)
}

function extractLabel(item) {
  const candidates = [
    item?.label,
    item?.nombre,
    item?.name,
    item?.descripcion,
    item?.modelo,
    item?.codigo
  ]

  return candidates.find((value) => Boolean(value)) || 'Sin nombre'
}

function extractSpecs(item) {
  const candidates = [
    item?.especificaciones,
    item?.specs,
    item?.detalle,
    item?.descripcion,
    item?.caracteristicas
  ]

  return candidates.find((value) => Boolean(value)) || ''
}

function toSelectOptions(items) {
  return (Array.isArray(items) ? items : [])
    .map((item) => {
      const id = extractId(item)
      if (id === undefined || id === null) {
        return null
      }

      return {
        value: String(id),
        label: String(extractLabel(item)),
        raw: item,
        specs: extractSpecs(item)
      }
    })
    .filter(Boolean)
}

function toCategoriaOptions(items) {
  return (Array.isArray(items) ? items : [])
    .map((item) => {
      const id = item?.idCategoriaActivo ?? item?.idCategoria ?? item?.id ?? item?.value
      const nombre = item?.nombre ?? item?.label ?? item?.name

      if (id === undefined || id === null || !nombre) {
        return null
      }

      return {
        value: String(id),
        label: String(nombre),
        raw: item,
        specs: ''
      }
    })
    .filter(Boolean)
}

function toEstadoOptions(items) {
  return (Array.isArray(items) ? items : [])
    .map((item) => {
      const id = item?.idEdoActivo ?? item?.idEstado ?? item?.id ?? item?.value
      const nombre = item?.nombre ?? item?.label ?? item?.name

      if (id === undefined || id === null || !nombre) {
        return null
      }

      return {
        value: String(id),
        label: String(nombre).replaceAll('_', ' '),
        raw: item,
        specs: ''
      }
    })
    .filter(Boolean)
}

function toDisponibleOptions(items, componentKey) {
  const modelFieldByKey = {
    cpu: 'modeloCpu',
    ram: 'modeloRam',
    motherboard: 'modeloMb',
    disco: 'modeloDisco',
    nic: 'modeloNic'
  }

  const modelField = modelFieldByKey[componentKey]

  return (Array.isArray(items) ? items : [])
    .filter((item) =>
      item?.idProcesador !== undefined && item?.idProcesador !== null
      || item?.idRam !== undefined && item?.idRam !== null
      || item?.idMotherboard !== undefined && item?.idMotherboard !== null
      || item?.idDisco !== undefined && item?.idDisco !== null
      || item?.idNic !== undefined && item?.idNic !== null
    )
    .map((item) => {
      const modelo = item?.[modelField]?.nombre || 'Modelo'
      const serie = item?.numeroSerie || 'Sin serie'

      return {
        value: String(item?.idProcesador ?? item?.idRam ?? item?.idMotherboard ?? item?.idDisco ?? item?.idNic ?? ''),
        label: `${modelo} | S/N: ${serie}`,
        raw: item,
        specs: ''
      }
    }).filter((item) => item.value !== '')
}

function isDesktopType(tipoOption) {
  return (tipoOption?.label || '').trim().toUpperCase() === 'ESCRITORIO'
}

function formatApiError(error, fallback) {
  if (error instanceof Error && error.message) {
    return error.message
  }

  return fallback
}

function CrearActivo() {
  const [form, setForm] = useState(EMPTY_FORM)
  const [componentes, setComponentes] = useState(createEmptyComponentes)
  const [stockState, setStockState] = useState(EMPTY_STOCK_STATE)
  const [stockFilters, setStockFilters] = useState(EMPTY_STOCK_FILTERS)
  const [componentCatalogsLoaded, setComponentCatalogsLoaded] = useState(false)

  const [options, setOptions] = useState({
    categorias: [],
    tiposActivo: [],
    estados: [],
    edificios: [],
    espacios: []
  })

  const [componentCatalogs, setComponentCatalogs] = useState(EMPTY_MODEL_CATALOGS)

  const [loading, setLoading] = useState({
    categorias: false,
    tiposActivo: false,
    estados: false,
    edificios: false,
    espacios: false,
    submit: false
  })
  const [loadingComponents, setLoadingComponents] = useState(EMPTY_LOADING_COMPONENTS)

  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [errors, setErrors] = useState({})

  const tipoSeleccionado = useMemo(
    () => options.tiposActivo.find((tipo) => tipo.value === form.tipoActivoId),
    [options.tiposActivo, form.tipoActivoId]
  )

  const isEscritorio = useMemo(() => isDesktopType(tipoSeleccionado), [tipoSeleccionado])

  const fetchJson = useCallback(async (path, fallbackMessage) => {
    const response = await fetch(`${API_URL}${path}`)
    let payload = null

    try {
      payload = await response.json()
    } catch {
      payload = null
    }

    if (!response.ok) {
      throw new Error(parseResponseError(payload, fallbackMessage))
    }

    return payload
  }, [])

  const loadInitialCatalogs = useCallback(async () => {
    setLoading((prev) => ({ ...prev, categorias: true, estados: true, edificios: true }))
    setError('')

    const [categoriasResult, estadosResult, edificiosResult] = await Promise.allSettled([
      fetchJson('/categoria-activo', 'No se pudieron cargar las categorias.'),
      fetchJson('/estados-activo', 'No se pudieron cargar los estados de activo.'),
      fetchJson('/edificios', 'No se pudieron cargar los edificios.')
    ])

    setOptions((prev) => ({
      ...prev,
      categorias: categoriasResult.status === 'fulfilled' ? toCategoriaOptions(categoriasResult.value) : [],
      estados: estadosResult.status === 'fulfilled' ? toEstadoOptions(estadosResult.value) : [],
      edificios: edificiosResult.status === 'fulfilled' ? toSelectOptions(edificiosResult.value) : []
    }))

    const firstFailure = [categoriasResult, estadosResult, edificiosResult].find(
      (result) => result.status === 'rejected'
    )
    if (firstFailure) {
      setError(formatApiError(firstFailure.reason, 'Error al cargar catalogos iniciales.'))
    }

    setLoading((prev) => ({ ...prev, categorias: false, estados: false, edificios: false }))
  }, [fetchJson])

  const loadTiposByCategoria = useCallback(async (categoriaId) => {
    if (!categoriaId) {
      setOptions((prev) => ({ ...prev, tiposActivo: [] }))
      return
    }

    setLoading((prev) => ({ ...prev, tiposActivo: true }))
    setError('')

    try {
      const tipos = await fetchJson(
        `/tipos-activo?categoria-activo=${encodeURIComponent(categoriaId)}`,
        'No se pudieron cargar los tipos de activo.'
      )

      setOptions((prev) => ({
        ...prev,
        tiposActivo: toSelectOptions(tipos)
      }))
    } catch (requestError) {
      setError(formatApiError(requestError, 'Error al cargar tipos de activo.'))
    } finally {
      setLoading((prev) => ({ ...prev, tiposActivo: false }))
    }
  }, [fetchJson])

  const loadEspaciosByEdificio = useCallback(async (edificioId) => {
    if (!edificioId) {
      setOptions((prev) => ({ ...prev, espacios: [] }))
      return
    }

    setLoading((prev) => ({ ...prev, espacios: true }))
    setError('')

    try {
      const espacios = await fetchJson(
        `/espacios?edificio=${encodeURIComponent(edificioId)}`,
        'No se pudieron cargar los espacios del edificio.'
      )

      setOptions((prev) => ({
        ...prev,
        espacios: toSelectOptions(espacios)
      }))
    } catch (requestError) {
      setError(formatApiError(requestError, 'Error al cargar espacios.'))
    } finally {
      setLoading((prev) => ({ ...prev, espacios: false }))
    }
  }, [fetchJson])

  const loadComponentBrands = useCallback(async () => {
    setError('')

    await Promise.all(
      COMPONENT_DEFINITIONS.map(async (definition) => {
        setLoadingComponents((prev) => ({
          ...prev,
          [definition.key]: { ...prev[definition.key], marcas: true }
        }))

        try {
          const marcas = await fetchJson(
            `/${definition.endpoint}/marcas`,
            `No se pudieron cargar las marcas de ${definition.payloadType}.`
          )

          setComponentCatalogs((prev) => ({
            ...prev,
            [definition.key]: {
              ...prev[definition.key],
              marcas: toSelectOptions(marcas)
            }
          }))
        } catch (requestError) {
          setError(formatApiError(requestError, 'Error al cargar marcas de componentes.'))
        } finally {
          setLoadingComponents((prev) => ({
            ...prev,
            [definition.key]: { ...prev[definition.key], marcas: false }
          }))
        }
      })
    )
  }, [fetchJson])

  const loadDisponiblesByComponent = useCallback(async () => {
    setError('')

    await Promise.all(
      COMPONENT_DEFINITIONS.map(async (definition) => {
        setStockState((prev) => ({
          ...prev,
          [definition.key]: { ...prev[definition.key], loading: true }
        }))

        try {
          const disponibles = await fetchJson(
            `/${definition.endpoint}/disponibles`,
            `No se pudieron cargar disponibles de ${definition.payloadType}.`
          )

          setStockState((prev) => ({
            ...prev,
            [definition.key]: {
              ...prev[definition.key],
              options: toDisponibleOptions(disponibles, definition.key)
            }
          }))
        } catch (requestError) {
          setError(formatApiError(requestError, `Error al cargar disponibles de ${definition.payloadType}.`))
        } finally {
          setStockState((prev) => ({
            ...prev,
            [definition.key]: { ...prev[definition.key], loading: false }
          }))
        }
      })
    )
  }, [fetchJson])

  const loadComponentModels = useCallback(async (componentKey, endpoint, marcaId) => {
    if (!marcaId) {
      setComponentCatalogs((prev) => ({
        ...prev,
        [componentKey]: { ...prev[componentKey], modelos: [] }
      }))
      return
    }

    setLoadingComponents((prev) => ({
      ...prev,
      [componentKey]: { ...prev[componentKey], modelos: true }
    }))
    setError('')

    try {
      const modelos = await fetchJson(
        `/${endpoint}/modelos?marca=${encodeURIComponent(marcaId)}`,
        `No se pudieron cargar los modelos para ${componentKey}.`
      )

      setComponentCatalogs((prev) => ({
        ...prev,
        [componentKey]: {
          ...prev[componentKey],
          modelos: toSelectOptions(modelos)
        }
      }))
    } catch (requestError) {
      setError(formatApiError(requestError, 'Error al cargar modelos de componentes.'))
    } finally {
      setLoadingComponents((prev) => ({
        ...prev,
        [componentKey]: { ...prev[componentKey], modelos: false }
      }))
    }
  }, [fetchJson])

  useEffect(() => {
    loadInitialCatalogs()
  }, [loadInitialCatalogs])

  useEffect(() => {
    loadTiposByCategoria(form.categoriaId)
  }, [form.categoriaId, loadTiposByCategoria])

  useEffect(() => {
    loadEspaciosByEdificio(form.edificioId)
  }, [form.edificioId, loadEspaciosByEdificio])

  useEffect(() => {
    if (isEscritorio) {
      if (componentCatalogsLoaded) {
        return
      }
      loadComponentBrands()
      loadDisponiblesByComponent()
      setComponentCatalogsLoaded(true)
      return
    }

    setComponentCatalogsLoaded(false)
    setStockState(EMPTY_STOCK_STATE)
    setStockFilters(EMPTY_STOCK_FILTERS)
  }, [isEscritorio, componentCatalogsLoaded, loadComponentBrands, loadDisponiblesByComponent])

  const handleGeneralChange = (event) => {
    const { name, value } = event.target

    setErrors((prev) => {
      if (!prev[name]) {
        return prev
      }

      const next = { ...prev }
      delete next[name]
      return next
    })

    if (name === 'categoriaId') {
      setForm((prev) => ({
        ...prev,
        categoriaId: value,
        tipoActivoId: ''
      }))
      setOptions((prev) => ({ ...prev, tiposActivo: [] }))
      return
    }

    if (name === 'edificioId') {
      setForm((prev) => ({
        ...prev,
        edificioId: value,
        espacioId: ''
      }))
      setOptions((prev) => ({ ...prev, espacios: [] }))
      return
    }

    setForm((prev) => ({ ...prev, [name]: value }))
  }

  const handleComponentChange = (componentKey, field, value) => {
    setComponentes((prev) => ({
      ...prev,
      [componentKey]: {
        ...prev[componentKey],
        [field]: value
      }
    }))

    const errorKey = `${componentKey}.${field}`
    setErrors((prev) => {
      if (!prev[errorKey]) {
        return prev
      }

      const next = { ...prev }
      delete next[errorKey]
      return next
    })
  }

  const handleMarcaChange = (componentKey, marcaId) => {
    const definition = COMPONENT_DEFINITIONS.find((item) => item.key === componentKey)
    if (!definition) {
      return
    }

    setComponentes((prev) => ({
      ...prev,
      [componentKey]: {
        ...prev[componentKey],
        marcaId,
        modeloId: ''
      }
    }))

    setComponentCatalogs((prev) => ({
      ...prev,
      [componentKey]: {
        ...prev[componentKey],
        modelos: []
      }
    }))

    setErrors((prev) => {
      const next = { ...prev }
      delete next[`${componentKey}.marcaId`]
      delete next[`${componentKey}.modeloId`]
      return next
    })

    loadComponentModels(componentKey, definition.endpoint, marcaId)
  }

  const handleStockModeChange = (componentKey, mode) => {
    setStockState((prev) => ({
      ...prev,
      [componentKey]: {
        ...prev[componentKey],
        mode,
        selectedId: mode === 'disponible' ? prev[componentKey].selectedId : ''
      }
    }))

    setStockFilters((prev) => ({
      ...prev,
      [componentKey]: EMPTY_STOCK_FILTERS[componentKey]
    }))

    setErrors((prev) => {
      const next = { ...prev }
      delete next[`${componentKey}.modo`]
      delete next[`${componentKey}.disponibleId`]
      delete next[`${componentKey}.marcaId`]
      delete next[`${componentKey}.modeloId`]
      delete next[`${componentKey}.numeroSerie`]
      delete next[`${componentKey}.fechaCompra`]
      return next
    })
  }

  const handleStockDisponibleChange = (componentKey, selectedId) => {
    setStockState((prev) => ({
      ...prev,
      [componentKey]: { ...prev[componentKey], selectedId }
    }))

    setErrors((prev) => {
      const next = { ...prev }
      delete next[`${componentKey}.disponibleId`]
      return next
    })
  }

  const handleStockMarcaFilterChange = async (componentKey, marcaId) => {
    const definition = COMPONENT_DEFINITIONS.find((item) => item.key === componentKey)
    if (!definition) {
      return
    }

    setStockState((prev) => ({
      ...prev,
      [componentKey]: { ...prev[componentKey], selectedId: '' }
    }))

    setStockFilters((prev) => ({
      ...prev,
      [componentKey]: {
        ...prev[componentKey],
        marcaId,
        modeloId: '',
        modelos: [],
        loadingModelos: Boolean(marcaId)
      }
    }))

    if (!marcaId) {
      return
    }

    try {
      const modelos = await fetchJson(
        `/${definition.endpoint}/modelos?marca=${encodeURIComponent(marcaId)}`,
        `No se pudieron cargar los modelos de ${definition.payloadType} para filtro.`
      )

      setStockFilters((prev) => ({
        ...prev,
        [componentKey]: {
          ...prev[componentKey],
          modelos: toSelectOptions(modelos),
          loadingModelos: false
        }
      }))
    } catch (requestError) {
      setStockFilters((prev) => ({
        ...prev,
        [componentKey]: { ...prev[componentKey], loadingModelos: false }
      }))
      setError(formatApiError(requestError, `Error al cargar modelos de ${definition.payloadType} para filtro.`))
    }
  }

  const handleStockModeloFilterChange = (componentKey, modeloId) => {
    setStockState((prev) => ({
      ...prev,
      [componentKey]: { ...prev[componentKey], selectedId: '' }
    }))

    setStockFilters((prev) => ({
      ...prev,
      [componentKey]: { ...prev[componentKey], modeloId }
    }))
  }

  const validate = () => {
    const nextErrors = {}

    if (!form.categoriaId) {
      nextErrors.categoriaId = 'La categoria es obligatoria.'
    }

    if (!form.tipoActivoId) {
      nextErrors.tipoActivoId = 'El tipo de activo es obligatorio.'
    }

    if (!form.fechaCompra) {
      nextErrors.fechaCompra = 'La fecha de compra es obligatoria.'
    }

    if (!form.estadoId) {
      nextErrors.estadoId = 'El estado es obligatorio.'
    }

    if (isEscritorio) {
      COMPONENT_DEFINITIONS.forEach((definition) => {
        const value = componentes[definition.key]
        if (stockState[definition.key].mode === 'disponible') {
          if (!stockState[definition.key].selectedId) {
            nextErrors[`${definition.key}.disponibleId`] = `Selecciona un ${definition.payloadType} disponible.`
          }
          return
        }

        if (!value.marcaId) {
          nextErrors[`${definition.key}.marcaId`] = 'La marca es obligatoria.'
        }

        if (!value.modeloId) {
          nextErrors[`${definition.key}.modeloId`] = 'El modelo es obligatorio.'
        }

        if (!value.numeroSerie.trim()) {
          nextErrors[`${definition.key}.numeroSerie`] = 'El numero de serie es obligatorio.'
        }

        if (!value.fechaCompra) {
          nextErrors[`${definition.key}.fechaCompra`] = `La fecha de compra de ${definition.payloadType} es obligatoria.`
        }
      })
    } else if (!form.numeroSerieGeneral.trim()) {
      nextErrors.numeroSerieGeneral = 'El numero de serie general es obligatorio para este tipo de activo.'
    }

    setErrors(nextErrors)
    return Object.keys(nextErrors).length === 0
  }

  const buildPayload = () => {
    const payload = {
      categoriaId: Number(form.categoriaId),
      tipoActivoId: Number(form.tipoActivoId),
      fechaCompra: form.fechaCompra,
      estadoId: Number(form.estadoId)
    }

    if (form.espacioId) {
      payload.ubicacionId = Number(form.espacioId)
    }

    if (isEscritorio) {
      payload.componentes = COMPONENT_DEFINITIONS
        .filter((definition) => stockState[definition.key].mode !== 'disponible')
        .map((definition) => ({
          tipo: definition.payloadType,
          modeloId: Number(componentes[definition.key].modeloId),
          numeroSerie: componentes[definition.key].numeroSerie.trim(),
          fechaCompra: componentes[definition.key].fechaCompra
        }))

      COMPONENT_DEFINITIONS
        .filter((definition) => stockState[definition.key].mode === 'disponible')
        .forEach((definition) => {
          payload[definition.disponibleField] = Number(stockState[definition.key].selectedId)
        })
    } else {
      payload.componentes = []
      payload.numeroSerie = form.numeroSerieGeneral.trim()
    }

    return payload
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setSuccess('')
    setError('')

    if (!validate()) {
      return
    }

    setLoading((prev) => ({ ...prev, submit: true }))

    try {
      const response = await fetch(`${API_URL}/api/activos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(buildPayload())
      })

      let payload = null
      try {
        payload = await response.json()
      } catch {
        payload = null
      }

      if (!response.ok) {
        throw new Error(parseResponseError(payload, 'No se pudo registrar el activo.'))
      }

      setSuccess('Activo registrado correctamente.')
      setForm(EMPTY_FORM)
      setComponentes(createEmptyComponentes())
      setStockState(EMPTY_STOCK_STATE)
      setStockFilters(EMPTY_STOCK_FILTERS)
      setOptions((prev) => ({ ...prev, tiposActivo: [], espacios: [] }))
      setErrors({})
    } catch (requestError) {
      setError(formatApiError(requestError, 'Error al registrar el activo.'))
    } finally {
      setLoading((prev) => ({ ...prev, submit: false }))
    }
  }

  return (
    <main className="asset-page">
      <section className="asset-header">
        <h1>Registro de Activo</h1>
        <p>Captura un activo con secciones dinamicas segun el tipo seleccionado.</p>
      </section>

      <form className="asset-form" onSubmit={handleSubmit}>
        <DatosGenerales
          form={form}
          errors={errors}
          options={{
            categorias: options.categorias,
            tiposActivo: options.tiposActivo,
            estados: options.estados
          }}
          loading={{
            categorias: loading.categorias,
            tiposActivo: loading.tiposActivo,
            estados: loading.estados
          }}
          isEscritorio={isEscritorio}
          onChange={handleGeneralChange}
        />

        {isEscritorio && (
          <ComponentesEquipo
            componentes={componentes}
            modelCatalogs={componentCatalogs}
            loading={loadingComponents}
            errors={errors}
            onChange={handleComponentChange}
            onMarcaChange={handleMarcaChange}
            stockState={stockState}
            stockFilters={stockFilters}
            onStockModeChange={handleStockModeChange}
            onStockDisponibleChange={handleStockDisponibleChange}
            onStockMarcaFilterChange={handleStockMarcaFilterChange}
            onStockModeloFilterChange={handleStockModeloFilterChange}
          />
        )}

        <UbicacionSection
          form={form}
          options={{
            edificios: options.edificios,
            espacios: options.espacios
          }}
          loading={{
            edificios: loading.edificios,
            espacios: loading.espacios
          }}
          errors={errors}
          onChange={handleGeneralChange}
        />

        <div className="asset-actions">
          <button type="submit" disabled={loading.submit}>
            {loading.submit ? 'Guardando activo...' : 'Registrar activo'}
          </button>
        </div>
      </form>

      {error && <p className="users-message error">{error}</p>}
      {success && <p className="users-message success">{success}</p>}
    </main>
  )
}

export default CrearActivo
