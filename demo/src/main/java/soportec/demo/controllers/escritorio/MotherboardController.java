package soportec.demo.controllers.escritorio;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import soportec.demo.models.Motherboard;
import soportec.demo.models.MotherboardMarca;
import soportec.demo.models.MotherboardModelo;
import soportec.demo.services.impl.MotherboardMarcaServiceImpl;
import soportec.demo.services.impl.MotherboardModeloServiceImpl;
import soportec.demo.services.impl.MotherboardServiceImpl;

@RestController
@RequestMapping("/api/motherboard")
public class MotherboardController {

    private final MotherboardServiceImpl motherboardService;
    private final MotherboardMarcaServiceImpl motherboardMarcaService;
    private final MotherboardModeloServiceImpl motherboardModeloService;

    public MotherboardController(
            MotherboardServiceImpl motherboardService,
            MotherboardMarcaServiceImpl motherboardMarcaService,
            MotherboardModeloServiceImpl motherboardModeloService) {
        this.motherboardService = motherboardService;
        this.motherboardMarcaService = motherboardMarcaService;
        this.motherboardModeloService = motherboardModeloService;
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<MotherboardMarca>> getMarcas() {
        return ResponseEntity.ok(motherboardMarcaService.findAll());
    }

    @GetMapping("/modelos")
    public ResponseEntity<List<MotherboardModelo>> getModelos(@RequestParam("marca") Integer idMarca) {
        return ResponseEntity.ok(motherboardModeloService.findByMarcaMotherboard(idMarca));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Motherboard>> getDisponibles() {
        return ResponseEntity.ok(motherboardService.findDisponibles());
    }
}
