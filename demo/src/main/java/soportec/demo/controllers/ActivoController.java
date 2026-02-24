package soportec.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import soportec.demo.dto.requests.DtoActivoCreateReq;
import soportec.demo.services.impl.ActivoServiceImpl;
import soportec.demo.utilidades.JwtUtil;

@Controller
@RequestMapping("/activos")
public class ActivoController {        

    ActivoServiceImpl activoServiceImpl;
    JwtUtil jwtUtil = new JwtUtil();

    @PostMapping("/create")
    public ResponseEntity<?> createActivo(String BearerToken, @RequestBody DtoActivoCreateReq request) {

        if (!JwtUtil.esValido(BearerToken)) 
            return ResponseEntity.status(401).body("Token inválido");
        
        return activoServiceImpl.createActivo(request);
    }    
}
