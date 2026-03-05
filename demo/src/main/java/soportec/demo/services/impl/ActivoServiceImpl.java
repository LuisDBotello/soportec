package soportec.demo.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import soportec.demo.dto.requests.DtoActivoCreateReq;
import soportec.demo.models.Activo;
import soportec.demo.models.Cpu;
import soportec.demo.models.CpuModelo;
import soportec.demo.models.Disco;
import soportec.demo.models.DiscoModelo;
import soportec.demo.models.Espacio;
import soportec.demo.models.EstadoActivo;
import soportec.demo.models.MarcaActivo;
import soportec.demo.models.Motherboard;
import soportec.demo.models.MotherboardModelo;
import soportec.demo.models.ModeloActivo;
import soportec.demo.models.Nic;
import soportec.demo.models.NicModelo;
import soportec.demo.models.Ram;
import soportec.demo.models.RamModelo;
import soportec.demo.models.TipoActivo;
import soportec.demo.repositories.ActivoRepository;
import soportec.demo.services.service.ActivoService;

@Service
public class ActivoServiceImpl implements ActivoService {

    private final ActivoRepository repository;
    private final TipoActivoServiceImpl tipoActivoService;
    private final EstadoActivoServiceImpl estadoActivoService;
    private final EspacioServiceImpl espacioService;
    private final CpuServiceImpl cpuService;
    private final RamServiceImpl ramService;
    private final DiscoServiceImpl discoService;
    private final MotherboardServiceImpl motherboardService;
    private final NicServiceImpl nicService;
    private final CpuModeloServiceImpl cpuModeloService;
    private final RamModeloServiceImpl ramModeloService;
    private final DiscoModeloServiceImpl discoModeloService;
    private final MotherboardModeloServiceImpl motherboardModeloService;
    private final NicModeloServiceImpl nicModeloService;
    private final MarcaActivoServiceImpl marcaActivoService;
    private final ModeloActivoServiceImpl modeloActivoService;

    public ActivoServiceImpl(
            ActivoRepository repository,
            TipoActivoServiceImpl tipoActivoService,
            EstadoActivoServiceImpl estadoActivoService,
            EspacioServiceImpl espacioService,
            CpuServiceImpl cpuService,
            RamServiceImpl ramService,
            DiscoServiceImpl discoService,
            MotherboardServiceImpl motherboardService,
            NicServiceImpl nicService,
            CpuModeloServiceImpl cpuModeloService,
            RamModeloServiceImpl ramModeloService,
            DiscoModeloServiceImpl discoModeloService,
            MotherboardModeloServiceImpl motherboardModeloService,
            NicModeloServiceImpl nicModeloService,
            MarcaActivoServiceImpl marcaActivoService,
            ModeloActivoServiceImpl modeloActivoService) {
        this.repository = repository;
        this.tipoActivoService = tipoActivoService;
        this.estadoActivoService = estadoActivoService;
        this.espacioService = espacioService;
        this.cpuService = cpuService;
        this.ramService = ramService;
        this.discoService = discoService;
        this.motherboardService = motherboardService;
        this.nicService = nicService;
        this.cpuModeloService = cpuModeloService;
        this.ramModeloService = ramModeloService;
        this.discoModeloService = discoModeloService;
        this.motherboardModeloService = motherboardModeloService;
        this.nicModeloService = nicModeloService;
        this.marcaActivoService = marcaActivoService;
        this.modeloActivoService = modeloActivoService;
    }

    @Transactional
    public ResponseEntity<?> createActivo(DtoActivoCreateReq request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("Se requiere el payload del activo.");
        }
        if (request.getTipoActivoId() == null) {
            return ResponseEntity.badRequest().body("tipoActivoId es obligatorio.");
        }
        if (request.getEstadoId() == null) {
            return ResponseEntity.badRequest().body("estadoId es obligatorio.");
        }
        if (request.getFechaCompra() == null || request.getFechaCompra().isBlank()) {
            return ResponseEntity.badRequest().body("fechaCompra es obligatoria.");
        }

        final TipoActivo tipoActivo = tipoActivoService.findById(request.getTipoActivoId())
                .orElse(null);
        if (tipoActivo == null) {
            return ResponseEntity.badRequest().body("El tipoActivoId no existe.");
        }

        if (request.getCategoriaId() != null
                && tipoActivo.getCategoriaActivo() != null
                && !request.getCategoriaId().equals(tipoActivo.getCategoriaActivo().getIdCategoriaActivo())) {
            return ResponseEntity.badRequest().body("categoriaId no coincide con tipoActivoId.");
        }

        final EstadoActivo estadoActivo = estadoActivoService.findById(request.getEstadoId())
                .orElse(null);
        if (estadoActivo == null) {
            return ResponseEntity.badRequest().body("El estadoId no existe.");
        }

        final Espacio espacio = request.getUbicacionId() == null
                ? null
                : espacioService.findById(request.getUbicacionId()).orElse(null);
        if (request.getUbicacionId() != null && espacio == null) {
            return ResponseEntity.badRequest().body("El ubicacionId no existe.");
        }

        final LocalDate fechaCompraActivo;
        try {
            fechaCompraActivo = parseLocalDateFlexible(request.getFechaCompra(), "fechaCompra");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        final boolean esEscritorio = tipoActivo.getNombre() != null
                && "ESCRITORIO".equalsIgnoreCase(tipoActivo.getNombre().trim());

        MarcaActivo marcaCatalogo = null;
        ModeloActivo modeloCatalogo = null;

        if (!esEscritorio) {
            if (request.getMarcaActivoId() == null) {
                return ResponseEntity.badRequest().body("marcaActivoId es obligatorio para activos no escritorio.");
            }
            if (request.getModeloActivoId() == null) {
                return ResponseEntity.badRequest().body("modeloActivoId es obligatorio para activos no escritorio.");
            }
            if (request.getNumeroSerie() == null || request.getNumeroSerie().isBlank()) {
                return ResponseEntity.badRequest().body("numeroSerie es obligatorio para activos no escritorio.");
            }

            marcaCatalogo = marcaActivoService.findById(request.getMarcaActivoId()).orElse(null);
            if (marcaCatalogo == null) {
                return ResponseEntity.badRequest().body("marcaActivoId no existe.");
            }

            modeloCatalogo = modeloActivoService.findById(request.getModeloActivoId()).orElse(null);
            if (modeloCatalogo == null) {
                return ResponseEntity.badRequest().body("modeloActivoId no existe.");
            }

            if (marcaCatalogo.getTipoActivo() == null
                    || !request.getTipoActivoId().equals(marcaCatalogo.getTipoActivo().getIdTipoActivo())) {
                return ResponseEntity.badRequest().body("La marca no pertenece al tipo de activo seleccionado.");
            }

            if (modeloCatalogo.getMarcaActivo() == null
                    || !request.getMarcaActivoId().equals(modeloCatalogo.getMarcaActivo().getIdMarcaActivo())) {
                return ResponseEntity.badRequest().body("El modelo no pertenece a la marca seleccionada.");
            }
        }

        Activo activo = new Activo();
        activo.setMarca(esEscritorio ? "GENERICA" : marcaCatalogo.getNombre());
        activo.setModelo(esEscritorio ? tipoActivo.getNombre() : modeloCatalogo.getNombre());
        activo.setMarcaActivo(esEscritorio ? null : marcaCatalogo);
        activo.setModeloActivo(esEscritorio ? null : modeloCatalogo);
        activo.setNumeroSerie(esEscritorio ? "ACT-" + System.currentTimeMillis() : request.getNumeroSerie().trim());
        activo.setFechaCompra(fechaCompraActivo);
        activo.setTipoActivo(tipoActivo);
        activo.setEstado(estadoActivo);
        activo.setEspacio(espacio);

        final Activo activoGuardado;
        try {
            activoGuardado = repository.save(activo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el activo: " + e.getMessage());
        }

        if (!esEscritorio) {
            return ResponseEntity.ok().body("Activo creado exitosamente");
        }

        List<DtoActivoCreateReq.ComponenteReq> componentes = request.getComponentes() == null
                ? Collections.emptyList()
                : request.getComponentes();

        Map<String, List<DtoActivoCreateReq.ComponenteReq>> componentesByTipo = componentes.stream()
                .filter(item -> item != null && item.getTipo() != null)
                .collect(Collectors.groupingBy(
                        item -> item.getTipo().trim().toUpperCase(Locale.ROOT)));

        if (request.getCpuDisponibleId() == null && componentesByTipo.getOrDefault("CPU", Collections.emptyList()).isEmpty()) {
            return ResponseEntity.badRequest().body("Debes enviar CPU en componentes o cpuDisponibleId.");
        }
        if (request.getRamDisponibleId() == null && componentesByTipo.getOrDefault("RAM", Collections.emptyList()).isEmpty()) {
            return ResponseEntity.badRequest().body("Debes enviar RAM en componentes o ramDisponibleId.");
        }
        if (request.getDiscoDisponibleId() == null && componentesByTipo.getOrDefault("DISCO", Collections.emptyList()).isEmpty()) {
            return ResponseEntity.badRequest().body("Debes enviar DISCO en componentes o discoDisponibleId.");
        }
        if (request.getMotherboardDisponibleId() == null
                && componentesByTipo.getOrDefault("MOTHERBOARD", Collections.emptyList()).isEmpty()) {
            return ResponseEntity.badRequest().body("Debes enviar MOTHERBOARD en componentes o motherboardDisponibleId.");
        }
        if (request.getNicDisponibleId() == null && componentesByTipo.getOrDefault("NIC", Collections.emptyList()).isEmpty()) {
            return ResponseEntity.badRequest().body("Debes enviar NIC en componentes o nicDisponibleId.");
        }

        try {
            asignarDisponibles(request, activoGuardado);
            crearComponentesNuevos(componentesByTipo, activoGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el equipo de escritorio: " + e.getMessage());
        }

        return ResponseEntity.ok().body("Equipo creado exitosamente");
    }

    private void asignarDisponibles(DtoActivoCreateReq request, Activo activoGuardado) {
        if (request.getCpuDisponibleId() != null) {
            Optional<Cpu> cpuDisponible = cpuService.findDisponibleById(request.getCpuDisponibleId());
            if (cpuDisponible.isEmpty()) {
                throw new IllegalArgumentException("CPU no disponible para asignacion.");
            }
            Cpu cpu = cpuDisponible.get();
            cpu.setActivo(activoGuardado);
            cpuService.save(cpu);
        }

        if (request.getRamDisponibleId() != null) {
            Optional<Ram> ramDisponible = ramService.findDisponibleById(request.getRamDisponibleId());
            if (ramDisponible.isEmpty()) {
                throw new IllegalArgumentException("RAM no disponible para asignacion.");
            }
            Ram ram = ramDisponible.get();
            ram.setActivo(activoGuardado);
            ramService.save(ram);
        }

        if (request.getDiscoDisponibleId() != null) {
            Optional<Disco> discoDisponible = discoService.findDisponibleById(request.getDiscoDisponibleId());
            if (discoDisponible.isEmpty()) {
                throw new IllegalArgumentException("DISCO no disponible para asignacion.");
            }
            Disco disco = discoDisponible.get();
            disco.setActivo(activoGuardado);
            discoService.save(disco);
        }

        if (request.getMotherboardDisponibleId() != null) {
            Optional<Motherboard> motherboardDisponible = motherboardService.findDisponibleById(request.getMotherboardDisponibleId());
            if (motherboardDisponible.isEmpty()) {
                throw new IllegalArgumentException("MOTHERBOARD no disponible para asignacion.");
            }
            Motherboard motherboard = motherboardDisponible.get();
            motherboard.setActivo(activoGuardado);
            motherboardService.save(motherboard);
        }

        if (request.getNicDisponibleId() != null) {
            Optional<Nic> nicDisponible = nicService.findDisponibleById(request.getNicDisponibleId());
            if (nicDisponible.isEmpty()) {
                throw new IllegalArgumentException("NIC no disponible para asignacion.");
            }
            Nic nic = nicDisponible.get();
            nic.setActivo(activoGuardado);
            nicService.save(nic);
        }
    }

    private void crearComponentesNuevos(Map<String, List<DtoActivoCreateReq.ComponenteReq>> componentesByTipo, Activo activoGuardado) {
        for (DtoActivoCreateReq.ComponenteReq componente : componentesByTipo.getOrDefault("CPU", Collections.emptyList())) {
            validarComponente(componente, "CPU");
            CpuModelo modelo = cpuModeloService.findById(componente.getModeloId())
                    .orElseThrow(() -> new IllegalArgumentException("modeloId de CPU no existe: " + componente.getModeloId()));

            Cpu cpu = new Cpu();
            cpu.setIdProcesador(cpuService.getNextIdProcesador());
            cpu.setModeloCpu(modelo);
            cpu.setNumeroSerie(componente.getNumeroSerie().trim());
            cpu.setFechaCompra(parseLocalDateFlexible(componente.getFechaCompra(), "fechaCompra de CPU"));
            cpu.setActivo(activoGuardado);
            cpuService.save(cpu);
        }

        for (DtoActivoCreateReq.ComponenteReq componente : componentesByTipo.getOrDefault("RAM", Collections.emptyList())) {
            validarComponente(componente, "RAM");
            RamModelo modelo = ramModeloService.findById(componente.getModeloId())
                    .orElseThrow(() -> new IllegalArgumentException("modeloId de RAM no existe: " + componente.getModeloId()));

            Ram ram = new Ram();
            ram.setIdRam(ramService.getNextIdRam());
            ram.setModeloRam(modelo);
            ram.setNumeroSerie(componente.getNumeroSerie().trim());
            ram.setFechaCompra(parseLocalDateFlexible(componente.getFechaCompra(), "fechaCompra de RAM"));
            ram.setActivo(activoGuardado);
            ramService.save(ram);
        }

        for (DtoActivoCreateReq.ComponenteReq componente : componentesByTipo.getOrDefault("DISCO", Collections.emptyList())) {
            validarComponente(componente, "DISCO");
            DiscoModelo modelo = discoModeloService.findById(componente.getModeloId())
                    .orElseThrow(() -> new IllegalArgumentException("modeloId de DISCO no existe: " + componente.getModeloId()));

            Disco disco = new Disco();
            disco.setIdDisco(discoService.getNextIdDisco());
            disco.setModeloDisco(modelo);
            disco.setNumeroSerie(componente.getNumeroSerie().trim());
            disco.setFechaCompra(parseLocalDateFlexible(componente.getFechaCompra(), "fechaCompra de DISCO"));
            disco.setEstadoSalud("DESCONOCIDO");
            disco.setActivo(activoGuardado);
            discoService.save(disco);
        }

        for (DtoActivoCreateReq.ComponenteReq componente : componentesByTipo.getOrDefault("MOTHERBOARD", Collections.emptyList())) {
            validarComponente(componente, "MOTHERBOARD");
            MotherboardModelo modelo = motherboardModeloService.findById(componente.getModeloId())
                    .orElseThrow(() -> new IllegalArgumentException("modeloId de MOTHERBOARD no existe: " + componente.getModeloId()));

            Motherboard motherboard = new Motherboard();
            motherboard.setModeloMotherboard(modelo);
            motherboard.setNumeroSerie(componente.getNumeroSerie().trim());
            motherboard.setFechaCompra(parseLocalDateFlexible(componente.getFechaCompra(), "fechaCompra de MOTHERBOARD"));
            motherboard.setActivo(activoGuardado);
            motherboardService.save(motherboard);
        }

        for (DtoActivoCreateReq.ComponenteReq componente : componentesByTipo.getOrDefault("NIC", Collections.emptyList())) {
            validarComponente(componente, "NIC");
            NicModelo modelo = nicModeloService.findById(componente.getModeloId())
                    .orElseThrow(() -> new IllegalArgumentException("modeloId de NIC no existe: " + componente.getModeloId()));

            Nic nic = new Nic();
            Integer idNic = nicService.getNextIdNic();
            nic.setIdNic(idNic);
            nic.setModeloNic(modelo);
            nic.setNumeroSerie(componente.getNumeroSerie().trim());
            nic.setMacAddress(buildPlaceholderMac(idNic));
            nic.setFechaCompra(parseLocalDateFlexible(componente.getFechaCompra(), "fechaCompra de NIC"));
            nic.setActivo(activoGuardado);
            nicService.save(nic);
        }
    }

    private void validarComponente(DtoActivoCreateReq.ComponenteReq componente, String tipo) {
        if (componente.getModeloId() == null) {
            throw new IllegalArgumentException("modeloId es obligatorio para " + tipo + ".");
        }
        if (componente.getNumeroSerie() == null || componente.getNumeroSerie().isBlank()) {
            throw new IllegalArgumentException("numeroSerie es obligatorio para " + tipo + ".");
        }
        if (componente.getFechaCompra() == null || componente.getFechaCompra().isBlank()) {
            throw new IllegalArgumentException("fechaCompra es obligatoria para " + tipo + ".");
        }
    }

    private LocalDate parseLocalDateFlexible(String value, String fieldName) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDateTime.parse(value).toLocalDate();
            } catch (DateTimeParseException ignoredToo) {
                throw new IllegalArgumentException("Formato invalido para " + fieldName + ".");
            }
        }
    }

    private String buildPlaceholderMac(Integer idNic) {
        int safe = idNic == null ? 0 : idNic;
        return String.format(Locale.ROOT, "02:00:00:%02X:%02X:%02X",
                (safe >> 16) & 0xFF,
                (safe >> 8) & 0xFF,
                safe & 0xFF);
    }

    @Override
    public List<Activo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Activo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Activo save(Activo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
