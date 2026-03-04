package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import soportec.demo.dto.requests.DtoActivoCreateReq;
import soportec.demo.models.Activo;
import soportec.demo.models.Cpu;
import soportec.demo.models.Disco;
import soportec.demo.models.Motherboard;
import soportec.demo.models.Nic;
import soportec.demo.models.Ram;
import soportec.demo.repositories.ActivoRepository;
import soportec.demo.services.service.ActivoService;

@Service
public class ActivoServiceImpl implements ActivoService {

    private final ActivoRepository repository;
    private final TipoActivoServiceImpl tipoActivoService;
    private final CpuServiceImpl cpuService;
    private final RamServiceImpl ramService;
    private final DiscoServiceImpl discoService;
    private final MotherboardServiceImpl motherboardService;
    private final NicServiceImpl nicService;

    public ActivoServiceImpl(
            ActivoRepository repository,
            TipoActivoServiceImpl tipoActivoService,
            CpuServiceImpl cpuService,
            RamServiceImpl ramService,
            DiscoServiceImpl discoService,
            MotherboardServiceImpl motherboardService,
            NicServiceImpl nicService) {
        this.repository = repository;
        this.tipoActivoService = tipoActivoService;
        this.cpuService = cpuService;
        this.ramService = ramService;
        this.discoService = discoService;
        this.motherboardService = motherboardService;
        this.nicService = nicService;
    }

    @Transactional
    public ResponseEntity<?> createActivo(DtoActivoCreateReq request) {
        if (request == null || request.getActivo() == null) {
            return ResponseEntity.badRequest().body("Se requiere la informacion del activo.");
        }

        Activo activo = new Activo();
        activo.setIdActivo(request.getActivo().getIdActivo());
        activo.setMarca(request.getActivo().getMarca());
        activo.setModelo(request.getActivo().getModelo());
        activo.setNumeroSerie(request.getActivo().getNumeroSerie());
        activo.setFechaCompra(request.getActivo().getFechaCompra());
        activo.setTipoActivo(request.getActivo().getTipoActivo());
        activo.setEstado(request.getActivo().getEstado());
        activo.setEspacio(request.getActivo().getEspacio());

        if (activo.getTipoActivo() == null) {
            return ResponseEntity.badRequest().body("El tipo de activo es obligatorio.");
        }

        final Activo activoGuardado;
        try {
            activoGuardado = repository.save(activo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el activo: " + e.getMessage());
        }

        if (!tipoActivoService.esEscritorio(activoGuardado.getTipoActivo())) {
            return ResponseEntity.ok().body("Activo creado exitosamente");
        }

        try {
            if (request.getCpuDisponibleId() != null) {
                Optional<Cpu> cpuDisponible = cpuService.findDisponibleById(request.getCpuDisponibleId());
                if (cpuDisponible.isEmpty()) {
                    return ResponseEntity.badRequest().body("CPU no disponible para asignacion.");
                }

                Cpu cpu = cpuDisponible.get();
                cpu.setActivo(activoGuardado);
                cpuService.save(cpu);
            } else {
                if (request.getCpu() == null) {
                    return ResponseEntity.badRequest().body("Debes enviar un CPU nuevo o cpuDisponibleId.");
                }

                Cpu cpuNuevo = request.getCpu();
                if (cpuNuevo.getModeloCpu() == null || cpuNuevo.getNumeroSerie() == null || cpuNuevo.getNumeroSerie().isBlank()
                        || cpuNuevo.getFechaCompra() == null) {
                    return ResponseEntity.badRequest().body("El CPU nuevo requiere modelo, numero de serie y fecha de compra.");
                }

                if (cpuNuevo.getIdProcesador() == null) {
                    cpuNuevo.setIdProcesador(cpuService.getNextIdProcesador());
                }
                cpuNuevo.setActivo(activoGuardado);
                cpuService.save(cpuNuevo);
            }

            if (request.getRamDisponibleId() != null) {
                Optional<Ram> ramDisponible = ramService.findDisponibleById(request.getRamDisponibleId());
                if (ramDisponible.isEmpty()) {
                    return ResponseEntity.badRequest().body("RAM no disponible para asignacion.");
                }
                Ram ram = ramDisponible.get();
                ram.setActivo(activoGuardado);
                ramService.save(ram);
            } else {
                Ram ram = request.getRam();
                if (ram == null) {
                    return ResponseEntity.badRequest().body("Debes enviar RAM nueva o ramDisponibleId.");
                }
                ram.setActivo(activoGuardado);
                ramService.save(ram);
            }

            if (request.getDiscoDisponibleId() != null) {
                Optional<Disco> discoDisponible = discoService.findDisponibleById(request.getDiscoDisponibleId());
                if (discoDisponible.isEmpty()) {
                    return ResponseEntity.badRequest().body("Disco no disponible para asignacion.");
                }
                Disco disco = discoDisponible.get();
                disco.setActivo(activoGuardado);
                discoService.save(disco);
            } else {
                Disco disco = request.getDisco();
                if (disco == null) {
                    return ResponseEntity.badRequest().body("Debes enviar Disco nuevo o discoDisponibleId.");
                }
                disco.setActivo(activoGuardado);
                discoService.save(disco);
            }

            if (request.getMotherboardDisponibleId() != null) {
                Optional<Motherboard> motherboardDisponible = motherboardService.findDisponibleById(request.getMotherboardDisponibleId());
                if (motherboardDisponible.isEmpty()) {
                    return ResponseEntity.badRequest().body("Motherboard no disponible para asignacion.");
                }
                Motherboard motherboard = motherboardDisponible.get();
                motherboard.setActivo(activoGuardado);
                motherboardService.save(motherboard);
            } else {
                Motherboard motherboard = request.getMotherboard();
                if (motherboard == null) {
                    return ResponseEntity.badRequest().body("Debes enviar Motherboard nueva o motherboardDisponibleId.");
                }
                motherboard.setActivo(activoGuardado);
                motherboardService.save(motherboard);
            }

            if (request.getNicDisponibleId() != null) {
                Optional<Nic> nicDisponible = nicService.findDisponibleById(request.getNicDisponibleId());
                if (nicDisponible.isEmpty()) {
                    return ResponseEntity.badRequest().body("NIC no disponible para asignacion.");
                }
                Nic nic = nicDisponible.get();
                nic.setActivo(activoGuardado);
                nicService.save(nic);
            } else {
                Nic nic = request.getNic();
                if (nic == null) {
                    return ResponseEntity.badRequest().body("Debes enviar NIC nueva o nicDisponibleId.");
                }
                nic.setActivo(activoGuardado);
                nicService.save(nic);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el equipo de escritorio: " + e.getMessage());
        }

        return ResponseEntity.ok().body("Equipo creado exitosamente");
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
