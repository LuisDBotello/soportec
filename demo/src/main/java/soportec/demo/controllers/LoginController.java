package soportec.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import soportec.demo.models.Usuario;
import soportec.demo.services.service.UsuarioService;
import soportec.demo.utilidades.JwtUtil;


@Controller
@RequestMapping("/login")
public class LoginController {
    
    JwtUtil jwtUtil = new JwtUtil();
    UsuarioService usuarioService;
    

    @PostMapping("")
    public ResponseEntity<?> login(String token, String username, String password) {

        if (!JwtUtil.esValido(token)) 
            return ResponseEntity.status(401).body("Token inválido");

        Usuario user = usuarioService.findByUsername(JwtUtil.getUsername(token));



        return ResponseEntity.ok("Bienvenido " + user.getUsername() + ", tu nivel de privilegio es: " + user.getNivel());
    }
}
