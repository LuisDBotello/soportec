package soportec.demo.controllers.escritorio;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import soportec.demo.models.Nic;
import soportec.demo.models.NicMarca;
import soportec.demo.models.NicModelo;
import soportec.demo.services.impl.NicMarcaServiceImpl;
import soportec.demo.services.impl.NicModeloServiceImpl;
import soportec.demo.services.impl.NicServiceImpl;

@RestController
@RequestMapping("/api/nic")
public class NicController {

    private final NicServiceImpl nicService;
    private final NicMarcaServiceImpl nicMarcaService;
    private final NicModeloServiceImpl nicModeloService;

    public NicController(
            NicServiceImpl nicService,
            NicMarcaServiceImpl nicMarcaService,
            NicModeloServiceImpl nicModeloService) {
        this.nicService = nicService;
        this.nicMarcaService = nicMarcaService;
        this.nicModeloService = nicModeloService;
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<NicMarca>> getMarcas() {
        return ResponseEntity.ok(nicMarcaService.findAll());
    }

    @GetMapping("/modelos")
    public ResponseEntity<List<NicModelo>> getModelos(@RequestParam("marca") Integer idMarca) {
        return ResponseEntity.ok(nicModeloService.findByMarcaNic(idMarca));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Nic>> getDisponibles() {
        return ResponseEntity.ok(nicService.findDisponibles());
    }
}
