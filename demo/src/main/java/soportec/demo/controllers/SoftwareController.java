package soportec.demo.controllers;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soportec.demo.models.Software;
import soportec.demo.services.impl.SoftwareServiceImpl;

@RestController
@RequestMapping("/api/software")
public class SoftwareController {

    private final SoftwareServiceImpl softwareService;

    public SoftwareController(SoftwareServiceImpl softwareService) {
        this.softwareService = softwareService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllSoftware() {
        return ResponseEntity.ok(softwareService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSoftwareById(@PathVariable("id") Integer id) {
        Optional<Software> software = softwareService.findById(id);
        if (software.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(software.get());
    }

    @PostMapping("")
    public ResponseEntity<?> createSoftware(@RequestBody Software request) {
        if (request == null || request.getNombre() == null || request.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del software es obligatorio.");
        }
        request.setIdSoftware(null);
        request.setNombre(request.getNombre().trim());
        if (request.getVers() != null) {
            request.setVers(request.getVers().trim());
        }
        return ResponseEntity.ok(softwareService.save(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSoftware(@PathVariable("id") Integer id) {
        if (softwareService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        softwareService.deleteById(id);
        return ResponseEntity.ok("Software eliminado correctamente.");
    }
}
