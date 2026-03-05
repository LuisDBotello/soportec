package soportec.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import soportec.demo.models.Espacio;
import soportec.demo.services.impl.EspacioServiceImpl;

@RestController
@RequestMapping("/api/espacios")
public class EspacioController {
    
    @Autowired
    EspacioServiceImpl espacioServiceImpl;

    
    @GetMapping
    public ResponseEntity<?> getEspaciosByEdificio(@RequestParam Integer edificio) {

        List<Espacio> espacios = espacioServiceImpl.findByEdificioId(edificio);

        return ResponseEntity.ok().body(espacios);
    }
}
