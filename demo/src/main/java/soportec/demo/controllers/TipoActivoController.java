package soportec.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import soportec.demo.models.TipoActivo;
import soportec.demo.services.impl.TipoActivoServiceImpl;

@RestController
@RequestMapping("/api/tipos-activo")
public class TipoActivoController {

    @Autowired
    TipoActivoServiceImpl tipoActivoServiceImpl;

    @GetMapping
    public ResponseEntity<List<TipoActivo>> getTiposActivoByCategoria(@RequestParam("categoria-activo") Integer categoriaActivo) {

        List<TipoActivo> tiposActivo = tipoActivoServiceImpl.findByCategoriaActivo(categoriaActivo);

        return ResponseEntity.ok(tiposActivo);
    }
}