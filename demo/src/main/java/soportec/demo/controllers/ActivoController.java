package soportec.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soportec.demo.dto.requests.DtoActivoCreateReq;
import soportec.demo.services.impl.ActivoServiceImpl;
import soportec.demo.utilidades.JwtUtil;

@RestController
@RequestMapping("/activos")
public class ActivoController {        

    private final ActivoServiceImpl activoServiceImpl;
    JwtUtil jwtUtil = new JwtUtil();

    public ActivoController(ActivoServiceImpl activoServiceImpl) {
        this.activoServiceImpl = activoServiceImpl;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createActivo(String BearerToken, @RequestBody DtoActivoCreateReq request) {

        if (!JwtUtil.esValido(BearerToken)) 
            return ResponseEntity.status(401).body("Token inválido");
        
        return activoServiceImpl.createActivo(request);
    }    
}
