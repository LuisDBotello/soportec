package soportec.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soportec.demo.models.EstadoActivo;
import soportec.demo.services.impl.EstadoActivoServiceImpl;

@RestController
@RequestMapping("/api/estados-activo")
public class EstadoActivoController {
    
    @Autowired
    EstadoActivoServiceImpl estadoActivoServiceImpl;

    @GetMapping()
    public ResponseEntity<?> getAllEstadosActivo() {

        List<EstadoActivo> estadosActivo = estadoActivoServiceImpl.findAll();
        return ResponseEntity.ok().body(estadosActivo);
    }
}
