package soportec.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import soportec.demo.models.CatActivo;
import soportec.demo.services.impl.CatActivoServiceImpl;

@Controller
@RequestMapping("/api/categoria-activo")
public class CategoriaActivoController {

    @Autowired
    CatActivoServiceImpl categoriaActivoService;
 
    @GetMapping
    public ResponseEntity<?> getAllCategoriaActivo(){

        List<CatActivo> categorias = categoriaActivoService.findAll();

        return ResponseEntity.ok().body(categorias);
    }
}
