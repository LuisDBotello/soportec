package soportec.demo.controllers.escritorio;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import soportec.demo.models.Ram;
import soportec.demo.models.RamMarca;
import soportec.demo.models.RamModelo;
import soportec.demo.services.impl.RamMarcaServiceImpl;
import soportec.demo.services.impl.RamModeloServiceImpl;
import soportec.demo.services.impl.RamServiceImpl;

@RestController
@RequestMapping("/api/ram")
public class RamController {

    private final RamServiceImpl ramService;
    private final RamMarcaServiceImpl ramMarcaService;
    private final RamModeloServiceImpl ramModeloService;

    public RamController(
            RamServiceImpl ramService,
            RamMarcaServiceImpl ramMarcaService,
            RamModeloServiceImpl ramModeloService) {
        this.ramService = ramService;
        this.ramMarcaService = ramMarcaService;
        this.ramModeloService = ramModeloService;
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<RamMarca>> getMarcas() {
        return ResponseEntity.ok(ramMarcaService.findAll());
    }

    @GetMapping("/modelos")
    public ResponseEntity<List<RamModelo>> getModelos(@RequestParam("marca") Integer idMarca) {
        return ResponseEntity.ok(ramModeloService.findByMarcaRam(idMarca));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Ram>> getDisponibles() {
        return ResponseEntity.ok(ramService.findDisponibles());
    }
}
