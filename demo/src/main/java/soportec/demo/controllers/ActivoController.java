package soportec.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import soportec.demo.dto.requests.DtoActivoCreateReq;
import soportec.demo.services.impl.ActivoServiceImpl;
import soportec.demo.services.impl.MarcaActivoServiceImpl;
import soportec.demo.services.impl.ModeloActivoServiceImpl;

@RestController
@RequestMapping("/api/activos")
public class ActivoController {

    private final ActivoServiceImpl activoServiceImpl;
    private final MarcaActivoServiceImpl marcaActivoService;
    private final ModeloActivoServiceImpl modeloActivoService;

    public ActivoController(
            ActivoServiceImpl activoServiceImpl,
            MarcaActivoServiceImpl marcaActivoService,
            ModeloActivoServiceImpl modeloActivoService) {
        this.activoServiceImpl = activoServiceImpl;
        this.marcaActivoService = marcaActivoService;
        this.modeloActivoService = modeloActivoService;
    }

    @GetMapping("/marcas")
    public ResponseEntity<?> getMarcasByTipoActivo(@RequestParam("tipoActivo") Integer tipoActivoId) {
        return ResponseEntity.ok(marcaActivoService.findByTipoActivo(tipoActivoId));
    }

    @GetMapping("/modelos")
    public ResponseEntity<?> getModelosByMarcaActivo(@RequestParam("marca") Integer marcaActivoId) {
        return ResponseEntity.ok(modeloActivoService.findByMarcaActivo(marcaActivoId));
    }

    @PostMapping("")
    public ResponseEntity<?> createActivo(@RequestBody DtoActivoCreateReq request) {
        return activoServiceImpl.createActivo(request);
    }
}
