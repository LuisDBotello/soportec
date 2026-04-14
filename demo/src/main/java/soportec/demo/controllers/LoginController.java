package soportec.demo.controllers;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonProperty;
import soportec.demo.models.Usuario;
import soportec.demo.services.service.UsuarioService;

@RestController
@RequestMapping({ "/login", "/api/login" })
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = normalize(request.getUsername());
        String password = normalize(request.getPassword());

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Captura usuario y contrasena para continuar."));
        }

        Usuario user = usuarioService.findByUsername(username);
        if (user == null || user.getPasswordHash() == null || !user.getPasswordHash().equals(password)) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Credenciales invalidas"));
        }

        Integer nivelId = user.getNivel() == null ? null : user.getNivel().getId_nivel();
        String nivelNombre = user.getNivel() == null ? null : user.getNivel().getNombre();
        Map<String, Object> nivel = new LinkedHashMap<>();
        nivel.put("id_nivel", nivelId);
        nivel.put("nombre", nivelNombre);

        Map<String, Object> userPayload = new LinkedHashMap<>();
        userPayload.put("idUsuario", user.getIdUsuario());
        userPayload.put("username", user.getUsername());
        userPayload.put("nombre", user.getNombre());
        userPayload.put("nivel", nivel);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Bienvenido " + user.getUsername());
        response.put("user", userPayload);

        return ResponseEntity.ok(response);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    public static class LoginRequest {
        private String username;
        @JsonProperty("password")
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
