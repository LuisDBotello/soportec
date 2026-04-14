import { useCallback, useEffect, useMemo, useState } from 'react'
import DatosGenerales from './DatosGenerales'
import ComponentesEquipo from './ComponentesEquipo'
import UbicacionSection from './UbicacionSection'
import InputField from '../fields/InputField'
import SelectField from '../fields/SelectField'

const API_URL = import.meta.env.VITE_API_URL
const DESKTOP_PACKAGES_STORAGE_KEY = 'soportec.desktop.packages.v1'

const EMPTY_FORM = {
  categoriaId: '',
  tipoActivoId: '',
  fechaCompra: '',
  estadoId: '',
  marcaGeneral: '',
  modeloGeneral: '',
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

function createEmptyExtraComponentes() {
  return {
    ram: [],
    disco: []
  }
}

function createEmptyComponenteNuevo() {
  return {
    marcaId: '',
    modeloId: '',
    numeroSerie: '',
    fechaCompra: getTodayDateInput()
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
  cpu: { marcas: [], modelos: [], modelosByMarca: {} },
  ram: { marcas: [], modelos: [], modelosByMarca: {} },
  motherboard: { marcas: [], modelos: [], modelosByMarca: {} },
  disco: { marcas: [], modelos: [], modelosByMarca: {} },
  nic: { marcas: [], modelos: [], modelosByMarca: {} }
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
    item?.idSoftware,
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
    item?.idMarcaActivo,
    item?.idModeloActivo,
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

function normalizeDateToken(value) {
  if (!value) {
    return 'SINFECHA'
  }

  return String(value).replaceAll(/[^0-9]/g, '').slice(0, 12) || 'SINFECHA'
}

function buildGenericSerial(prefix, dateToken, equipoIndex, componentIndex = 1) {
  return `${prefix}-${dateToken}-EQ${String(equipoIndex + 1).padStart(3, '0')}-C${String(componentIndex).padStart(2, '0')}`
}

function loadDesktopPackages() {
  try {
    const raw = localStorage.getItem(DESKTOP_PACKAGES_STORAGE_KEY)
    if (!raw) {
      return []
    }
    const parsed = JSON.parse(raw)
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
}

function persistDesktopPackages(packages) {
  localStorage.setItem(DESKTOP_PACKAGES_STORAGE_KEY, JSON.stringify(packages))
}

function CrearActivo() {
  const [form, setForm] = useState(EMPTY_FORM)
  const [componentes, setComponentes] = useState(createEmptyComponentes)
  const [componentesExtra, setComponentesExtra] = useState(createEmptyExtraComponentes)
  const [stockState, setStockState] = useState(EMPTY_STOCK_STATE)
  const [stockFilters, setStockFilters] = useState(EMPTY_STOCK_FILTERS)
  const [componentCatalogsLoaded, setComponentCatalogsLoaded] = useState(false)

  const [options, setOptions] = useState({
    categorias: [],
    tiposActivo: [],
    estados: [],
    edificios: [],
    espacios: [],
    marcasActivo: [],
    modelosActivo: [],
    software: []
  })

  const [componentCatalogs, setComponentCatalogs] = useState(EMPTY_MODEL_CATALOGS)

  const [loading, setLoading] = useState({
    categorias: false,
    tiposActivo: false,
    estados: false,
    edificios: false,
    espacios: false,
    marcasActivo: false,
    modelosActivo: false,
    software: false,
    submit: false
  })
  const [loadingComponents, setLoadingComponents] = useState(EMPTY_LOADING_COMPONENTS)

  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [errors, setErrors] = useState({})
  const [desktopPackages, setDesktopPackages] = useState([])
  const [selectedDesktopPackageId, setSelectedDesktopPackageId] = useState('')
  const [desktopPackageName, setDesktopPackageName] = useState('')
  const [bulkDesktopCount, setBulkDesktopCount] = useState('1')
  const [selectedSoftwareIds, setSelectedSoftwareIds] = useState([])
  const [softwareSearchTerm, setSoftwareSearchTerm] = useState('')

  const tipoSeleccionado = useMemo(
    () => options.tiposActivo.find((tipo) => tipo.value === form.tipoActivoId),
    [options.tiposActivo, form.tipoActivoId]
  )

  const isEscritorio = useMemo(() => isDesktopType(tipoSeleccionado), [tipoSeleccionado])
  const filteredSoftwareOptions = useMemo(() => {
    const term = softwareSearchTerm.trim().toLowerCase()
    if (!term) {
      return options.software
    }
    return options.software.filter((option) =>
      String(option.label || '').toLowerCase().includes(term)
    )
  }, [options.software, softwareSearchTerm])
  const selectedSoftwareOptions = useMemo(() => {
    const selectedIds = new Set(selectedSoftwareIds.map((id) => String(id)))
    return options.software.filter((option) => selectedIds.has(String(option.value)))
  }, [options.software, selectedSoftwareIds])
  const availableSoftwareOptions = useMemo(() => {
    const selectedIds = new Set(selectedSoftwareIds.map((id) => String(id)))
    return filteredSoftwareOptions.filter((option) => !selectedIds.has(String(option.value)))
  }, [filteredSoftwareOptions, selectedSoftwareIds])

  useEffect(() => {
    setDesktopPackages(loadDesktopPackages())
  }, [])

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

  const loadMarcasActivoByTipo = useCallback(async (tipoActivoId) => {
    if (!tipoActivoId) {
      setOptions((prev) => ({ ...prev, marcasActivo: [], modelosActivo: [] }))
      return
    }

    setLoading((prev) => ({ ...prev, marcasActivo: true }))
    setError('')

    try {
      const marcas = await fetchJson(
        `/activos/marcas?tipoActivo=${encodeURIComponent(tipoActivoId)}`,
        'No se pudieron cargar las marcas del tipo de activo.'
      )

      setOptions((prev) => ({
        ...prev,
        marcasActivo: toSelectOptions(marcas),
        modelosActivo: []
      }))
    } catch (requestError) {
      setError(formatApiError(requestError, 'Error al cargar marcas de activo.'))
    } finally {
      setLoading((prev) => ({ ...prev, marcasActivo: false }))
    }
  }, [fetchJson])

  const loadModelosActivoByMarca = useCallback(async (marcaActivoId) => {
    if (!marcaActivoId) {
      setOptions((prev) => ({ ...prev, modelosActivo: [] }))
      return
    }

    setLoading((prev) => ({ ...prev, modelosActivo: true }))
    setError('')

    try {
      const modelos = await fetchJson(
        `/activos/modelos?marca=${encodeURIComponent(marcaActivoId)}`,
        'No se pudieron cargar los modelos de la marca.'
      )

      setOptions((prev) => ({
        ...prev,
        modelosActivo: toSelectOptions(modelos)
      }))
    } catch (requestError) {
      setError(formatApiError(requestError, 'Error al cargar modelos de activo.'))
    } finally {
      setLoading((prev) => ({ ...prev, modelosActivo: false }))
    }
  }, [fetchJson])

  const loadSoftwareCatalog = useCallback(async () => {
    setLoading((prev) => ({ ...prev, software: true }))
    setError('')

    try {
      const software = await fetchJson('/software', 'No se pudo cargar el catalogo de software.')

      setOptions((prev) => ({
        ...prev,
        software: toSelectOptions(software).map((option) => ({
          ...option,
          label: option.raw?.vers
            ? `${option.label} (${option.raw.vers})`
            : option.label
        }))
      }))
    } catch (requestError) {
      setError(formatApiError(requestError, 'Error al cargar software.'))
    } finally {
      setLoading((prev) => ({ ...prev, software: false }))
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

      const modelosOptions = toSelectOptions(modelos)

      setComponentCatalogs((prev) => ({
        ...prev,
        [componentKey]: {
          ...prev[componentKey],
          modelos: modelosOptions,
          modelosByMarca: {
            ...prev[componentKey].modelosByMarca,
            [marcaId]: modelosOptions
          }
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
    if (!form.tipoActivoId || isEscritorio) {
      setOptions((prev) => ({ ...prev, marcasActivo: [], modelosActivo: [] }))
      return
    }

    loadMarcasActivoByTipo(form.tipoActivoId)
  }, [form.tipoActivoId, isEscritorio, loadMarcasActivoByTipo])

  useEffect(() => {
    if (isEscritorio) {
      if (componentCatalogsLoaded) {
        return
      }
      loadComponentBrands()
      loadDisponiblesByComponent()
      loadSoftwareCatalog()
      setComponentCatalogsLoaded(true)
      return
    }

    setComponentCatalogsLoaded(false)
    setStockState(EMPTY_STOCK_STATE)
    setStockFilters(EMPTY_STOCK_FILTERS)
    setSelectedSoftwareIds([])
    setSoftwareSearchTerm('')
    setOptions((prev) => ({ ...prev, software: [] }))
  }, [isEscritorio, componentCatalogsLoaded, loadComponentBrands, loadDisponiblesByComponent, loadSoftwareCatalog])

  const handleAgregarSoftware = (softwareId) => {
    setSelectedSoftwareIds((prev) => {
      if (!softwareId || prev.some((id) => String(id) === String(softwareId))) {
        return prev
      }
      return [...prev, softwareId]
    })
  }

  const handleQuitarSoftware = (softwareId) => {
    setSelectedSoftwareIds((prev) => prev.filter((id) => String(id) !== String(softwareId)))
  }

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
        tipoActivoId: '',
        marcaGeneral: '',
        modeloGeneral: ''
      }))
      setOptions((prev) => ({ ...prev, tiposActivo: [], marcasActivo: [], modelosActivo: [] }))
      return
    }

    if (name === 'tipoActivoId') {
      setForm((prev) => ({
        ...prev,
        tipoActivoId: value,
        marcaGeneral: '',
        modeloGeneral: ''
      }))
      setOptions((prev) => ({ ...prev, marcasActivo: [], modelosActivo: [] }))

      setErrors((prev) => {
        const next = { ...prev }
        delete next.marcaGeneral
        delete next.modeloGeneral
        return next
      })
      return
    }

    if (name === 'marcaGeneralId') {
      setForm((prev) => ({
        ...prev,
        marcaGeneral: value,
        modeloGeneral: ''
      }))
      setOptions((prev) => ({ ...prev, modelosActivo: [] }))
      loadModelosActivoByMarca(value)

      setErrors((prev) => {
        const next = { ...prev }
        delete next.marcaGeneral
        delete next.modeloGeneral
        return next
      })
      return
    }

    if (name === 'modeloGeneralId') {
      setForm((prev) => ({ ...prev, modeloGeneral: value }))

      setErrors((prev) => {
        const next = { ...prev }
        delete next.modeloGeneral
        return next
      })
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

  const handleAgregarComponenteExtra = (componentKey) => {
    if (componentKey !== 'ram' && componentKey !== 'disco') {
      return
    }

    setComponentesExtra((prev) => ({
      ...prev,
      [componentKey]: [...prev[componentKey], createEmptyComponenteNuevo()]
    }))
  }

  const handleComponenteExtraChange = (componentKey, index, field, value) => {
    setComponentesExtra((prev) => ({
      ...prev,
      [componentKey]: prev[componentKey].map((item, itemIndex) =>
        itemIndex === index
          ? { ...item, [field]: value }
          : item
      )
    }))

    const errorKey = `${componentKey}.extra.${index}.${field}`
    setErrors((prev) => {
      if (!prev[errorKey]) {
        return prev
      }

      const next = { ...prev }
      delete next[errorKey]
      return next
    })
  }

  const handleComponenteExtraMarcaChange = (componentKey, index, marcaId) => {
    const definition = COMPONENT_DEFINITIONS.find((item) => item.key === componentKey)
    if (!definition) {
      return
    }

    setComponentesExtra((prev) => ({
      ...prev,
      [componentKey]: prev[componentKey].map((item, itemIndex) =>
        itemIndex === index
          ? { ...item, marcaId, modeloId: '' }
          : item
      )
    }))

    setErrors((prev) => {
      const next = { ...prev }
      delete next[`${componentKey}.extra.${index}.marcaId`]
      delete next[`${componentKey}.extra.${index}.modeloId`]
      return next
    })

    loadComponentModels(componentKey, definition.endpoint, marcaId)
  }

  const handleQuitarComponenteExtra = (componentKey, index) => {
    if (componentKey !== 'ram' && componentKey !== 'disco') {
      return
    }

    setComponentesExtra((prev) => ({
      ...prev,
      [componentKey]: prev[componentKey].filter((_, itemIndex) => itemIndex !== index)
    }))

    setErrors((prev) => {
      const next = { ...prev }
      ;['marcaId', 'modeloId', 'numeroSerie', 'fechaCompra'].forEach((field) => {
        delete next[`${componentKey}.extra.${index}.${field}`]
      })
      return next
    })
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

      ;['ram', 'disco'].forEach((componentKey) => {
        componentesExtra[componentKey].forEach((item, index) => {
          const payloadType = componentKey === 'ram' ? 'RAM' : 'DISCO'

          if (!item.marcaId) {
            nextErrors[`${componentKey}.extra.${index}.marcaId`] = `La marca de ${payloadType} adicional es obligatoria.`
          }

          if (!item.modeloId) {
            nextErrors[`${componentKey}.extra.${index}.modeloId`] = `El modelo de ${payloadType} adicional es obligatorio.`
          }

          if (!item.numeroSerie.trim()) {
            nextErrors[`${componentKey}.extra.${index}.numeroSerie`] = `El numero de serie de ${payloadType} adicional es obligatorio.`
          }

          if (!item.fechaCompra) {
            nextErrors[`${componentKey}.extra.${index}.fechaCompra`] = `La fecha de compra de ${payloadType} adicional es obligatoria.`
          }
        })
      })
    } else {
      if (!form.marcaGeneral) {
        nextErrors.marcaGeneral = 'La marca es obligatoria para este tipo de activo.'
      }

      if (!form.modeloGeneral) {
        nextErrors.modeloGeneral = 'El modelo es obligatorio para este tipo de activo.'
      }

      if (!form.numeroSerieGeneral.trim()) {
        nextErrors.numeroSerieGeneral = 'El numero de serie general es obligatorio para este tipo de activo.'
      }
    }

    setErrors(nextErrors)
    return Object.keys(nextErrors).length === 0
  }

  const validateDesktopBatch = () => {
    if (!isEscritorio) {
      return 'La creacion en lote aplica solo a tipo ESCRITORIO.'
    }

    if (!form.categoriaId || !form.tipoActivoId || !form.fechaCompra || !form.estadoId) {
      return 'Completa categoria, tipo de activo, fecha de armado y estado.'
    }

    for (const definition of COMPONENT_DEFINITIONS) {
      const componentKey = definition.key
      if (stockState[componentKey].mode === 'disponible') {
        if (!stockState[componentKey].selectedId) {
          return `Selecciona un ${definition.payloadType} disponible.`
        }
        continue
      }

      const value = componentes[componentKey]
      if (!value.marcaId || !value.modeloId || !value.fechaCompra) {
        return `Completa marca, modelo y fecha de compra para ${definition.payloadType}.`
      }
    }

    for (const componentKey of ['ram', 'disco']) {
      for (const item of componentesExtra[componentKey]) {
        if (!item.marcaId || !item.modeloId || !item.fechaCompra) {
          return `Completa marca, modelo y fecha en componentes adicionales de ${componentKey.toUpperCase()}.`
        }
      }
    }

    return ''
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
      payload.softwareIds = selectedSoftwareIds.map((id) => Number(id))

      payload.componentes = COMPONENT_DEFINITIONS
        .filter((definition) => stockState[definition.key].mode !== 'disponible')
        .map((definition) => ({
          tipo: definition.payloadType,
          modeloId: Number(componentes[definition.key].modeloId),
          numeroSerie: componentes[definition.key].numeroSerie.trim(),
          fechaCompra: componentes[definition.key].fechaCompra
        }))

      ;['ram', 'disco'].forEach((componentKey) => {
        const payloadType = componentKey === 'ram' ? 'RAM' : 'DISCO'
        const extras = componentesExtra[componentKey].map((item) => ({
          tipo: payloadType,
          modeloId: Number(item.modeloId),
          numeroSerie: item.numeroSerie.trim(),
          fechaCompra: item.fechaCompra
        }))
        payload.componentes.push(...extras)
      })

      COMPONENT_DEFINITIONS
        .filter((definition) => stockState[definition.key].mode === 'disponible')
        .forEach((definition) => {
          payload[definition.disponibleField] = Number(stockState[definition.key].selectedId)
        })
    } else {
      const marcaSeleccionada = options.marcasActivo.find((item) => item.value === form.marcaGeneral)
      const modeloSeleccionado = options.modelosActivo.find((item) => item.value === form.modeloGeneral)

      payload.componentes = []
      payload.marcaActivoId = Number(form.marcaGeneral)
      payload.modeloActivoId = Number(form.modeloGeneral)
      payload.marca = marcaSeleccionada?.label || ''
      payload.modelo = modeloSeleccionado?.label || ''
      payload.numeroSerie = form.numeroSerieGeneral.trim()
    }

    return payload
  }

  const buildDesktopPayloadWithGenericSerials = (equipoIndex) => {
    const dateToken = normalizeDateToken(form.fechaCompra)
    const payload = {
      categoriaId: Number(form.categoriaId),
      tipoActivoId: Number(form.tipoActivoId),
      fechaCompra: form.fechaCompra,
      estadoId: Number(form.estadoId),
      componentes: [],
      softwareIds: selectedSoftwareIds.map((id) => Number(id))
    }

    if (form.espacioId) {
      payload.ubicacionId = Number(form.espacioId)
    }

    COMPONENT_DEFINITIONS
      .filter((definition) => stockState[definition.key].mode !== 'disponible')
      .forEach((definition) => {
        payload.componentes.push({
          tipo: definition.payloadType,
          modeloId: Number(componentes[definition.key].modeloId),
          numeroSerie: buildGenericSerial(definition.payloadType, dateToken, equipoIndex, 1),
          fechaCompra: componentes[definition.key].fechaCompra
        })
      })

    ;['ram', 'disco'].forEach((componentKey) => {
      const payloadType = componentKey === 'ram' ? 'RAM' : 'DISCO'
      componentesExtra[componentKey].forEach((item, extraIndex) => {
        payload.componentes.push({
          tipo: payloadType,
          modeloId: Number(item.modeloId),
          numeroSerie: buildGenericSerial(payloadType, dateToken, equipoIndex, extraIndex + 2),
          fechaCompra: item.fechaCompra
        })
      })
    })

    COMPONENT_DEFINITIONS
      .filter((definition) => stockState[definition.key].mode === 'disponible')
      .forEach((definition) => {
        payload[definition.disponibleField] = Number(stockState[definition.key].selectedId)
      })

    return payload
  }

  const handleGuardarDesktopPackage = () => {
    if (!isEscritorio) {
      setError('Los paquetes solo se pueden guardar para tipo ESCRITORIO.')
      return
    }

    const name = desktopPackageName.trim()
    if (!name) {
      setError('Ingresa un nombre para el paquete.')
      return
    }

    const now = new Date().toISOString()
    const pack = {
      id: `pkg-${Date.now()}`,
      name,
      createdAt: now,
      snapshot: {
        componentes,
        componentesExtra,
        stockState,
        selectedSoftwareIds
      }
    }

    const next = [pack, ...desktopPackages.filter((item) => item.name !== name)]
    setDesktopPackages(next)
    persistDesktopPackages(next)
    setSelectedDesktopPackageId(pack.id)
    setSuccess(`Paquete "${name}" guardado.`)
    setError('')
  }

  const handleAplicarDesktopPackage = () => {
    const selected = desktopPackages.find((item) => item.id === selectedDesktopPackageId)
    if (!selected) {
      setError('Selecciona un paquete para cargar.')
      return
    }

    const snapshot = selected.snapshot
    setComponentes(snapshot.componentes || createEmptyComponentes())
    setComponentesExtra(snapshot.componentesExtra || createEmptyExtraComponentes())
    setStockState(snapshot.stockState || EMPTY_STOCK_STATE)
    setSelectedSoftwareIds(snapshot.selectedSoftwareIds || [])
    setSuccess(`Paquete "${selected.name}" cargado.`)
    setError('')
  }

  const handleCrearLoteEscritorio = async () => {
    setError('')
    setSuccess('')

    const validationError = validateDesktopBatch()
    if (validationError) {
      setError(validationError)
      return
    }

    const count = Number(bulkDesktopCount)
    if (!Number.isInteger(count) || count < 1 || count > 200) {
      setError('La cantidad debe ser un entero entre 1 y 200.')
      return
    }

    setLoading((prev) => ({ ...prev, submit: true }))
    let ok = 0
    const errorsBatch = []

    try {
      for (let index = 0; index < count; index += 1) {
        const payload = buildDesktopPayloadWithGenericSerials(index)
        const response = await fetch(`${API_URL}/activos`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        })

        let responsePayload = null
        try {
          responsePayload = await response.json()
        } catch {
          responsePayload = null
        }

        if (!response.ok) {
          errorsBatch.push(`#${index + 1}: ${parseResponseError(responsePayload, 'No se pudo registrar el activo.')}`)
          continue
        }
        ok += 1
      }

      if (errorsBatch.length > 0) {
        setError(`Lote parcial. Exitos: ${ok}, errores: ${errorsBatch.length}. ${errorsBatch[0]}`)
      } else {
        setSuccess(`Lote creado correctamente. Equipos registrados: ${ok}.`)
      }
    } catch (requestError) {
      setError(formatApiError(requestError, 'Error al registrar lote de activos.'))
    } finally {
      setLoading((prev) => ({ ...prev, submit: false }))
    }
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
      const response = await fetch(`${API_URL}/activos`, {
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
      setComponentesExtra(createEmptyExtraComponentes())
      setStockState(EMPTY_STOCK_STATE)
      setStockFilters(EMPTY_STOCK_FILTERS)
      setSelectedSoftwareIds([])
      setSoftwareSearchTerm('')
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
            estados: options.estados,
            marcasActivo: options.marcasActivo,
            modelosActivo: options.modelosActivo
          }}
          loading={{
            categorias: loading.categorias,
            tiposActivo: loading.tiposActivo,
            estados: loading.estados,
            marcasActivo: loading.marcasActivo,
            modelosActivo: loading.modelosActivo
          }}
          isEscritorio={isEscritorio}
          onChange={handleGeneralChange}
        />

        {isEscritorio && (
          <ComponentesEquipo
            componentes={componentes}
            componentesExtra={componentesExtra}
            modelCatalogs={componentCatalogs}
            loading={loadingComponents}
            errors={errors}
            onChange={handleComponentChange}
            onMarcaChange={handleMarcaChange}
            onExtraAdd={handleAgregarComponenteExtra}
            onExtraChange={handleComponenteExtraChange}
            onExtraMarcaChange={handleComponenteExtraMarcaChange}
            onExtraRemove={handleQuitarComponenteExtra}
            stockState={stockState}
            stockFilters={stockFilters}
            onStockModeChange={handleStockModeChange}
            onStockDisponibleChange={handleStockDisponibleChange}
            onStockMarcaFilterChange={handleStockMarcaFilterChange}
            onStockModeloFilterChange={handleStockModeloFilterChange}
          />
        )}

        {isEscritorio && (
          <section className="asset-section">
            <h2>Software</h2>
            <p className="asset-section-help">
              Selecciona uno o varios programas de la tabla SOFTWARE para asociarlos al activo.
            </p>
            <div className="asset-grid">
              <InputField
                name="softwareSearchTerm"
                label="Buscar programa"
                value={softwareSearchTerm}
                onChange={(event) => setSoftwareSearchTerm(event.target.value)}
                placeholder="Ej. Adobe, Linux, Office..."
              />
            </div>

            <div className="software-search-table-wrap">
              <table className="software-search-table">
                <thead>
                  <tr>
                    <th>Nombre</th>
                    <th>Version</th>
                    <th>Accion</th>
                  </tr>
                </thead>
                <tbody>
                  {!loading.software && availableSoftwareOptions.length === 0 && (
                    <tr>
                      <td colSpan={3}>
                        {softwareSearchTerm.trim()
                          ? 'Sin resultados para la busqueda actual.'
                          : 'No hay software disponible para agregar.'}
                      </td>
                    </tr>
                  )}
                  {loading.software && (
                    <tr>
                      <td colSpan={3}>Cargando catalogo de software...</td>
                    </tr>
                  )}
                  {!loading.software && availableSoftwareOptions.map((software) => (
                    <tr key={software.value}>
                      <td>{software.raw?.nombre || software.label}</td>
                      <td>{software.raw?.vers || '-'}</td>
                      <td>
                        <button
                          type="button"
                          className="software-add-btn"
                          onClick={() => handleAgregarSoftware(software.value)}
                        >
                          Agregar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="software-selected-list">
              <h3>Programas seleccionados ({selectedSoftwareOptions.length})</h3>
              {selectedSoftwareOptions.length === 0 && (
                <p className="asset-section-help">Aun no has agregado programas.</p>
              )}
              {selectedSoftwareOptions.length > 0 && (
                <ul>
                  {selectedSoftwareOptions.map((software) => (
                    <li key={software.value}>
                      <span>{software.label}</span>
                      <button
                        type="button"
                        className="extra-remove-btn"
                        onClick={() => handleQuitarSoftware(software.value)}
                      >
                        Quitar
                      </button>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          </section>
        )}

        {isEscritorio && (
          <section className="asset-section">
            <h2>Paquetes de Escritorio</h2>
            <p className="asset-section-help">Guarda y reutiliza configuraciones para alta masiva.</p>
            <div className="asset-grid compact">
              <InputField
                name="desktopPackageName"
                label="Nombre del paquete"
                value={desktopPackageName}
                onChange={(event) => setDesktopPackageName(event.target.value)}
                maxLength={80}
                placeholder="Ej. Oficina Dell i5"
              />

              <SelectField
                name="selectedDesktopPackageId"
                label="Paquete guardado"
                value={selectedDesktopPackageId}
                onChange={(event) => setSelectedDesktopPackageId(event.target.value)}
                options={desktopPackages.map((item) => ({ value: item.id, label: item.name }))}
                placeholder="Selecciona un paquete"
              />

              <InputField
                name="bulkDesktopCount"
                label="Cantidad de escritorios"
                type="number"
                value={bulkDesktopCount}
                onChange={(event) => setBulkDesktopCount(event.target.value)}
                min={1}
                max={200}
                required
              />
            </div>

            <div className="asset-actions desktop-package-actions">
              <button type="button" className="desktop-package-btn save" onClick={handleGuardarDesktopPackage}>
                Guardar paquete
              </button>
              <button type="button" className="desktop-package-btn load" onClick={handleAplicarDesktopPackage}>
                Cargar paquete
              </button>
              <button
                type="button"
                className="desktop-package-btn create"
                onClick={handleCrearLoteEscritorio}
                disabled={loading.submit}
              >
                {loading.submit ? 'Creando lote...' : 'Crear lote de escritorios'}
              </button>
            </div>
          </section>
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
