package soportec.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soportec.demo.models.Edificio;
import soportec.demo.services.impl.EdificioServiceImpl;
import soportec.demo.services.impl.EspacioServiceImpl;

@RestController
@RequestMapping("/api/edificios")
public class UbicacionController {
    
    @Autowired
    EdificioServiceImpl edificioServiceImpl;

    @Autowired
    EspacioServiceImpl espacioServiceImpl;

    @GetMapping("")
    public ResponseEntity<?> getAllEdificios() {
        
        List<Edificio> edificios = edificioServiceImpl.findAll();

        return ResponseEntity.ok().body(edificios);
    }

}
