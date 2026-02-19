package soportec.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties.Json;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soportec.demo.models.Usuario;
import soportec.demo.services.service.UsuarioService;

@Controller
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UsuarioService usuarioService;
    
    @GetMapping("")
    public ResponseEntity<?> getUsers() {
        
        List<Usuario> users = usuarioService.findAll();
        
        return ResponseEntity.ok().body(users);
    }
}
