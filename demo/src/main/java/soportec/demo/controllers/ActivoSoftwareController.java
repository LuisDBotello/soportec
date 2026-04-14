package soportec.demo.controllers;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import soportec.demo.dto.requests.DtoActivoSoftwareReq;
import soportec.demo.models.Activo;
import soportec.demo.models.ActivoSoftware;
import soportec.demo.models.Software;
import soportec.demo.services.impl.ActivoServiceImpl;
import soportec.demo.services.impl.ActivoSoftwareServiceImpl;
import soportec.demo.services.impl.SoftwareServiceImpl;

@RestController
@RequestMapping("/api/activo-software")
public class ActivoSoftwareController {

    private final ActivoSoftwareServiceImpl activoSoftwareService;
    private final ActivoServiceImpl activoService;
    private final SoftwareServiceImpl softwareService;

    public ActivoSoftwareController(
            ActivoSoftwareServiceImpl activoSoftwareService,
            ActivoServiceImpl activoService,
            SoftwareServiceImpl softwareService) {
        this.activoSoftwareService = activoSoftwareService;
        this.activoService = activoService;
        this.softwareService = softwareService;
    }

    @GetMapping("/activo/{idActivo}")
    public ResponseEntity<?> getSoftwareByActivo(@PathVariable("idActivo") Integer idActivo) {
        return ResponseEntity.ok(activoSoftwareService.findSoftwareByActivo(idActivo));
    }

    @PostMapping("")
    public ResponseEntity<?> vincularSoftware(@RequestBody DtoActivoSoftwareReq request) {
        if (request == null || request.getActivoId() == null || request.getSoftwareId() == null) {
            return ResponseEntity.badRequest().body("activoId y softwareId son obligatorios.");
        }

        Optional<Activo> activo = activoService.findById(request.getActivoId());
        if (activo.isEmpty()) {
            return ResponseEntity.badRequest().body("El activo no existe.");
        }

        Optional<Software> software = softwareService.findById(request.getSoftwareId());
        if (software.isEmpty()) {
            return ResponseEntity.badRequest().body("El software no existe.");
        }

        if (activoSoftwareService.existsRelacion(request.getActivoId(), request.getSoftwareId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La relacion activo-software ya existe.");
        }

        ActivoSoftware relation = new ActivoSoftware();
        relation.setActivo(activo.get());
        relation.setSoftware(software.get());
        activoSoftwareService.save(relation);

        return ResponseEntity.ok("Software vinculado al activo correctamente.");
    }

    @DeleteMapping("")
    public ResponseEntity<?> desvincularSoftware(
            @RequestParam("activoId") Integer activoId,
            @RequestParam("softwareId") Integer softwareId) {
        if (!activoSoftwareService.existsRelacion(activoId, softwareId)) {
            return ResponseEntity.notFound().build();
        }

        activoSoftwareService.deleteRelacion(activoId, softwareId);
        return ResponseEntity.ok("Relacion activo-software eliminada correctamente.");
    }
}
