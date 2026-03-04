package soportec.demo.controllers.escritorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import soportec.demo.models.Cpu;
import soportec.demo.models.CpuMarca;
import soportec.demo.models.CpuModelo;
import soportec.demo.services.impl.CpuMarcaServiceImpl;
import soportec.demo.services.impl.CpuModeloServiceImpl;
import soportec.demo.services.impl.CpuServiceImpl;

@RestController
@RequestMapping("/api/cpu")
public class CpuController {

    private final CpuServiceImpl cpuService;
    private final CpuMarcaServiceImpl cpuMarcaService;
    private final CpuModeloServiceImpl cpuModeloService;

    public CpuController(
            CpuServiceImpl cpuService,
            CpuMarcaServiceImpl cpuMarcaService,
            CpuModeloServiceImpl cpuModeloService) {
        this.cpuService = cpuService;
        this.cpuMarcaService = cpuMarcaService;
        this.cpuModeloService = cpuModeloService;
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<CpuMarca>> getMarcas() {
        return ResponseEntity.ok(cpuMarcaService.findAll());
    }

    @GetMapping("/modelos")
    public ResponseEntity<List<CpuModelo>> getModelos(@RequestParam("marca") Integer idMarca) {
        return ResponseEntity.ok(cpuModeloService.findByMarcaCpu(idMarca));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Cpu>> getDisponibles() {
        return ResponseEntity.ok(cpuService.findDisponibles());
    }

    @PostMapping("/stock")
    public ResponseEntity<?> crearCpuStock(@RequestBody CpuStockCreateRequest request) {
        if (request == null || request.getModeloId() == null || request.getNumeroSerie() == null
                || request.getNumeroSerie().isBlank() || request.getFechaCompra() == null) {
            return ResponseEntity.badRequest().body("modeloId, numeroSerie y fechaCompra son obligatorios.");
        }

        Optional<CpuModelo> modelo = cpuModeloService.findById(request.getModeloId());
        if (modelo.isEmpty()) {
            return ResponseEntity.badRequest().body("El modelo de CPU no existe.");
        }

        Cpu cpu = new Cpu();
        cpu.setIdProcesador(request.getIdProcesador() != null ? request.getIdProcesador() : cpuService.getNextIdProcesador());
        cpu.setModeloCpu(modelo.get());
        cpu.setNumeroSerie(request.getNumeroSerie().trim());
        cpu.setFechaCompra(request.getFechaCompra());
        cpu.setActivo(null);

        return ResponseEntity.ok(cpuService.save(cpu));
    }

    public static class CpuStockCreateRequest {

        private Integer idProcesador;
        private Integer modeloId;
        private String numeroSerie;
        private LocalDate fechaCompra;

        public Integer getIdProcesador() {
            return idProcesador;
        }

        public void setIdProcesador(Integer idProcesador) {
            this.idProcesador = idProcesador;
        }

        public Integer getModeloId() {
            return modeloId;
        }

        public void setModeloId(Integer modeloId) {
            this.modeloId = modeloId;
        }

        public String getNumeroSerie() {
            return numeroSerie;
        }

        public void setNumeroSerie(String numeroSerie) {
            this.numeroSerie = numeroSerie;
        }

        public LocalDate getFechaCompra() {
            return fechaCompra;
        }

        public void setFechaCompra(LocalDate fechaCompra) {
            this.fechaCompra = fechaCompra;
        }
    }
}
