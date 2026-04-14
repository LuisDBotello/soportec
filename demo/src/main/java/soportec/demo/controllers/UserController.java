package soportec.demo.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonProperty;
import soportec.demo.models.Departamento;
import soportec.demo.models.NivelPriv;
import soportec.demo.models.Usuario;
import soportec.demo.services.service.DepartamentoService;
import soportec.demo.services.service.NivelPrivService;
import soportec.demo.services.service.UsuarioService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UsuarioService usuarioService;
    private final DepartamentoService departamentoService;
    private final NivelPrivService nivelPrivService;

    public UserController(
            UsuarioService usuarioService,
            DepartamentoService departamentoService,
            NivelPrivService nivelPrivService) {
        this.usuarioService = usuarioService;
        this.departamentoService = departamentoService;
        this.nivelPrivService = nivelPrivService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUsers() {
        List<Usuario> users = usuarioService.findAll();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        Integer idDepto = request.getDepartamento() == null ? null : request.getDepartamento().getIdDepartamento();
        Integer idNivel = request.getNivel() == null ? null : request.getNivel().getIdNivel();

        if (idDepto == null || idNivel == null) {
            return ResponseEntity.badRequest().body("departamento.idDepartamento y nivel.id_nivel son obligatorios.");
        }

        Optional<Departamento> departamentoOpt = departamentoService.findById(idDepto);
        if (departamentoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("No existe el departamento con id: " + idDepto);
        }

        Optional<NivelPriv> nivelOpt = nivelPrivService.findById(idNivel);
        if (nivelOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("No existe el nivel con id: " + idNivel);
        }

        Usuario user = new Usuario();
        user.setNombre(normalize(request.getNombre()));
        user.setApellidoP(normalize(request.getApellidoP()));
        user.setApellidoM(normalize(request.getApellidoM()));
        user.setCorreo(normalize(request.getCorreo()));
        user.setUsername(normalize(request.getUsername()));
        user.setPasswordHash(normalize(resolvePassword(request)));
        user.setDepartamento(departamentoOpt.get());
        user.setNivel(nivelOpt.get());

        Usuario saved = usuarioService.save(user);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer idUsuario) {
        Optional<Usuario> userOpt = usuarioService.findById(idUsuario);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe el usuario con id: " + idUsuario);
        }

        usuarioService.deleteById(idUsuario);
        return ResponseEntity.noContent().build();
    }

    private String resolvePassword(CreateUserRequest request) {
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            return request.getContrasena();
        }
        return request.getPasswordHash();
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    public static class CreateUserRequest {
        private String nombre;
        private String apellidoP;
        private String apellidoM;
        private String correo;
        private String username;
        @JsonProperty("contrase\u00f1a")
        private String contrasena;
        private String passwordHash;
        private DepartamentoPayload departamento;
        private NivelPayload nivel;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellidoP() {
            return apellidoP;
        }

        public void setApellidoP(String apellidoP) {
            this.apellidoP = apellidoP;
        }

        public String getApellidoM() {
            return apellidoM;
        }

        public void setApellidoM(String apellidoM) {
            this.apellidoM = apellidoM;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }

        public String getPasswordHash() {
            return passwordHash;
        }

        public void setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
        }

        public DepartamentoPayload getDepartamento() {
            return departamento;
        }

        public void setDepartamento(DepartamentoPayload departamento) {
            this.departamento = departamento;
        }

        public NivelPayload getNivel() {
            return nivel;
        }

        public void setNivel(NivelPayload nivel) {
            this.nivel = nivel;
        }
    }

    public static class DepartamentoPayload {
        private Integer idDepartamento;

        public Integer getIdDepartamento() {
            return idDepartamento;
        }

        public void setIdDepartamento(Integer idDepartamento) {
            this.idDepartamento = idDepartamento;
        }
    }

    public static class NivelPayload {
        @JsonProperty("id_nivel")
        private Integer idNivel;

        public Integer getIdNivel() {
            return idNivel;
        }

        public void setIdNivel(Integer idNivel) {
            this.idNivel = idNivel;
        }
    }
}
