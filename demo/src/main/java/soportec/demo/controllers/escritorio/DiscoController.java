package soportec.demo.controllers.escritorio;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import soportec.demo.models.Disco;
import soportec.demo.models.DiscoMarca;
import soportec.demo.models.DiscoModelo;
import soportec.demo.services.impl.DiscoMarcaServiceImpl;
import soportec.demo.services.impl.DiscoModeloServiceImpl;
import soportec.demo.services.impl.DiscoServiceImpl;

@RestController
@RequestMapping("/api/disco")
public class DiscoController {

    private final DiscoServiceImpl discoService;
    private final DiscoMarcaServiceImpl discoMarcaService;
    private final DiscoModeloServiceImpl discoModeloService;

    public DiscoController(
            DiscoServiceImpl discoService,
            DiscoMarcaServiceImpl discoMarcaService,
            DiscoModeloServiceImpl discoModeloService) {
        this.discoService = discoService;
        this.discoMarcaService = discoMarcaService;
        this.discoModeloService = discoModeloService;
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<DiscoMarca>> getMarcas() {
        return ResponseEntity.ok(discoMarcaService.findAll());
    }

    @GetMapping("/modelos")
    public ResponseEntity<List<DiscoModelo>> getModelos(@RequestParam("marca") Integer idMarca) {
        return ResponseEntity.ok(discoModeloService.findByMarcaDisco(idMarca));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Disco>> getDisponibles() {
        return ResponseEntity.ok(discoService.findDisponibles());
    }
}
