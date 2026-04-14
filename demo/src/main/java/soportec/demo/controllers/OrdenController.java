package soportec.demo.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soportec.demo.dto.requests.DtoOrdenIncidenciaCreateReq;
import soportec.demo.models.Activo;
import soportec.demo.models.EstadoEvento;
import soportec.demo.models.Evento;
import soportec.demo.models.Orden;
import soportec.demo.models.OrdenEvento;
import soportec.demo.models.Usuario;
import soportec.demo.services.impl.ActivoServiceImpl;
import soportec.demo.services.impl.EstadoEventoServiceImpl;
import soportec.demo.services.impl.EventoServiceImpl;
import soportec.demo.services.impl.OrdenServiceImpl;
import soportec.demo.services.impl.OrdenEventoServiceImpl;
import soportec.demo.services.impl.UsuarioServiceImpl;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenServiceImpl ordenService;
    private final OrdenEventoServiceImpl ordenEventoService;
    private final EventoServiceImpl eventoService;
    private final EstadoEventoServiceImpl estadoEventoService;
    private final ActivoServiceImpl activoService;
    private final UsuarioServiceImpl usuarioService;

    public OrdenController(
            OrdenServiceImpl ordenService,
            OrdenEventoServiceImpl ordenEventoService,
            EventoServiceImpl eventoService,
            EstadoEventoServiceImpl estadoEventoService,
            ActivoServiceImpl activoService,
            UsuarioServiceImpl usuarioService) {
        this.ordenService = ordenService;
        this.ordenEventoService = ordenEventoService;
        this.eventoService = eventoService;
        this.estadoEventoService = estadoEventoService;
        this.activoService = activoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/incidencias/cronologico")
    public ResponseEntity<?> getIncidenciasCronologico() {
        List<Orden> incidencias = ordenService.findAllByFechaCreacionAsc();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Orden incidencia : incidencias) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("folio", incidencia.getFolio());
            item.put("fechaCreacion", incidencia.getFechaCreacion());
            item.put("estatus", incidencia.getEstatus());
            item.put("descripcion", incidencia.getDescripcion());
            item.put("prioridad", incidencia.getPrioridad());
            item.put("tipo", incidencia.getTipo());
            item.put("tiempoEstimado", incidencia.getTiempoEstimado());

            Activo activo = incidencia.getActivo();
            if (activo != null) {
                item.put("activoId", activo.getIdActivo());
                item.put("activoEtiqueta",
                        String.format("%s %s | S/N: %s",
                                safeText(activo.getMarca()),
                                safeText(activo.getModelo()),
                                safeText(activo.getNumeroSerie())));

                if (activo.getEspacio() != null) {
                    item.put("espacio", activo.getEspacio().getNombre());
                    item.put("edificio", activo.getEspacio().getEdificio() == null
                            ? null
                            : activo.getEspacio().getEdificio().getNombre());
                } else {
                    item.put("espacio", null);
                    item.put("edificio", null);
                }
            } else {
                item.put("activoId", null);
                item.put("activoEtiqueta", null);
                item.put("espacio", null);
                item.put("edificio", null);
            }

            item.put("solicitante", buildNombreUsuario(incidencia.getUsuario()));
            item.put("encargado", buildNombreUsuario(incidencia.getEncargado()));
            item.put("encargadoId", incidencia.getEncargado() == null ? null : incidencia.getEncargado().getIdUsuario());

            response.add(item);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/incidencias/asignadas")
    public ResponseEntity<?> getIncidenciasAsignadas(@org.springframework.web.bind.annotation.RequestParam Integer tecnicoId) {
        if (tecnicoId == null) {
            return ResponseEntity.badRequest().body("tecnicoId es obligatorio.");
        }
        Optional<Usuario> tecnicoOpt = usuarioService.findById(tecnicoId);
        if (tecnicoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("tecnicoId no existe.");
        }
        Integer nivelId = tecnicoOpt.get().getNivel() == null ? null : tecnicoOpt.get().getNivel().getId_nivel();
        if (nivelId == null || nivelId != 2) {
            return ResponseEntity.badRequest().body("El usuario seleccionado no es tecnico.");
        }

        List<Orden> incidencias = ordenService.findByTecnicoAsignado(tecnicoId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Orden incidencia : incidencias) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("folio", incidencia.getFolio());
            item.put("fechaCreacion", incidencia.getFechaCreacion());
            item.put("estatus", incidencia.getEstatus());
            item.put("descripcion", incidencia.getDescripcion());
            item.put("prioridad", incidencia.getPrioridad());
            item.put("tipo", incidencia.getTipo());
            item.put("tiempoEstimado", incidencia.getTiempoEstimado());
            item.put("solicitante", buildNombreUsuario(incidencia.getUsuario()));

            Activo activo = incidencia.getActivo();
            if (activo != null) {
                item.put("activoEtiqueta",
                        String.format("%s %s | S/N: %s",
                                safeText(activo.getMarca()),
                                safeText(activo.getModelo()),
                                safeText(activo.getNumeroSerie())));
                item.put("espacio", activo.getEspacio() == null ? null : activo.getEspacio().getNombre());
                item.put("edificio", activo.getEspacio() == null || activo.getEspacio().getEdificio() == null
                        ? null
                        : activo.getEspacio().getEdificio().getNombre());
            } else {
                item.put("activoEtiqueta", null);
                item.put("espacio", null);
                item.put("edificio", null);
            }
            response.add(item);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/incidencias/solicitadas")
    public ResponseEntity<?> getIncidenciasSolicitadas(@org.springframework.web.bind.annotation.RequestParam Integer solicitanteId) {
        if (solicitanteId == null) {
            return ResponseEntity.badRequest().body("solicitanteId es obligatorio.");
        }
        Optional<Usuario> solicitanteOpt = usuarioService.findById(solicitanteId);
        if (solicitanteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("solicitanteId no existe.");
        }

        List<Orden> incidencias = ordenService.findBySolicitante(solicitanteId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Orden incidencia : incidencias) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("folio", incidencia.getFolio());
            item.put("fechaCreacion", incidencia.getFechaCreacion());
            item.put("estatus", incidencia.getEstatus());
            item.put("descripcion", incidencia.getDescripcion());
            item.put("prioridad", incidencia.getPrioridad());
            item.put("tipo", incidencia.getTipo());
            item.put("tiempoEstimado", incidencia.getTiempoEstimado());
            item.put("solicitante", buildNombreUsuario(incidencia.getUsuario()));
            item.put("encargado", buildNombreUsuario(incidencia.getEncargado()));
            item.put("encargadoId", incidencia.getEncargado() == null ? null : incidencia.getEncargado().getIdUsuario());

            Activo activo = incidencia.getActivo();
            if (activo != null) {
                item.put("activoEtiqueta",
                        String.format("%s %s | S/N: %s",
                                safeText(activo.getMarca()),
                                safeText(activo.getModelo()),
                                safeText(activo.getNumeroSerie())));
                item.put("espacio", activo.getEspacio() == null ? null : activo.getEspacio().getNombre());
                item.put("edificio", activo.getEspacio() == null || activo.getEspacio().getEdificio() == null
                        ? null
                        : activo.getEspacio().getEdificio().getNombre());
            } else {
                item.put("activoEtiqueta", null);
                item.put("espacio", null);
                item.put("edificio", null);
            }
            response.add(item);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/incidencias/estados-evento")
    public ResponseEntity<?> getEstadosEvento() {
        List<EstadoEvento> estados = estadoEventoService.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (EstadoEvento estado : estados) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", estado.getId_estado());
            item.put("nombre", estado.getNombre());
            response.add(item);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/incidencias/{folio}/historial")
    public ResponseEntity<?> getHistorialIncidencia(@PathVariable Integer folio) {
        Optional<Orden> ordenOpt = ordenService.findById(folio);
        if (ordenOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No existe la incidencia con folio: " + folio);
        }

        Orden incidencia = ordenOpt.get();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("folio", incidencia.getFolio());
        response.put("fechaCreacion", incidencia.getFechaCreacion());
        response.put("estatus", incidencia.getEstatus());
        response.put("descripcion", incidencia.getDescripcion());
        response.put("prioridad", incidencia.getPrioridad());
        response.put("tipo", incidencia.getTipo());
        response.put("tiempoEstimado", incidencia.getTiempoEstimado());
        response.put("solicitante", buildNombreUsuario(incidencia.getUsuario()));
        response.put("encargado", buildNombreUsuario(incidencia.getEncargado()));

        List<OrdenEvento> eventos = ordenEventoService.findByFolio(folio);
        List<Map<String, Object>> historial = new ArrayList<>();
        for (OrdenEvento item : eventos) {
            Map<String, Object> evento = new LinkedHashMap<>();
            evento.put("idEvento", item.getId_evento() == null ? null : item.getId_evento().getId());
            evento.put("fecha", item.getFecha());
            evento.put("comentario", item.getComentario());
            evento.put("estadoId", item.getEstadoEvento() == null ? null : item.getEstadoEvento().getId_estado());
            evento.put("estado", item.getEstadoEvento() == null ? null : item.getEstadoEvento().getNombre());
            historial.add(evento);
        }
        response.put("historial", historial);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/incidencias/{folio}/historial")
    public ResponseEntity<?> createEntradaHistorial(
            @PathVariable Integer folio,
            @RequestBody HistorialEventoCreateRequest request) {
        if (request == null || request.getEstadoId() == null || request.getIdTecnico() == null) {
            return ResponseEntity.badRequest().body("estadoId e idTecnico son obligatorios.");
        }

        Optional<Orden> ordenOpt = ordenService.findById(folio);
        if (ordenOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No existe la incidencia con folio: " + folio);
        }
        Orden orden = ordenOpt.get();

        if (orden.getEncargado() == null || !request.getIdTecnico().equals(orden.getEncargado().getIdUsuario())) {
            return ResponseEntity.badRequest().body("Solo el tecnico asignado puede registrar eventos.");
        }

        if (request.getEstadoId() == 1) {
            return ResponseEntity.badRequest().body("El estado INICIADO se registra automaticamente al asignar tecnico.");
        }
        if (request.getEstadoId() == 5) {
            return ResponseEntity.badRequest().body("El estado LIBERADO solo puede ser registrado por el solicitante.");
        }

        LocalDate fecha = LocalDate.now();
        if (request.getFecha() != null && !request.getFecha().isBlank()) {
            try {
                fecha = LocalDate.parse(request.getFecha().trim());
            } catch (DateTimeParseException ex) {
                return ResponseEntity.badRequest().body("fecha debe tener formato YYYY-MM-DD.");
            }
        }

        String comentario = request.getComentario() == null ? null : request.getComentario().trim();
        if (comentario != null && comentario.length() > 255) {
            return ResponseEntity.badRequest().body("comentario no debe exceder 255 caracteres.");
        }

        OrdenEvento created;
        try {
            created = createHistorialEntry(orden, request.getEstadoId(), fecha, comentario);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Entrada de historial creada correctamente.");
        response.put("folio", folio);
        response.put("idEvento", created.getId_evento().getId());
        response.put("estado", created.getEstadoEvento() == null ? null : created.getEstadoEvento().getNombre());
        response.put("fecha", created.getFecha());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/incidencias/{folio}/liberar")
    public ResponseEntity<?> liberarIncidencia(
            @PathVariable Integer folio,
            @RequestBody LiberarIncidenciaRequest request) {
        if (request == null || request.getSolicitanteId() == null) {
            return ResponseEntity.badRequest().body("solicitanteId es obligatorio.");
        }

        Optional<Orden> ordenOpt = ordenService.findById(folio);
        if (ordenOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No existe la incidencia con folio: " + folio);
        }
        Orden orden = ordenOpt.get();

        if (orden.getUsuario() == null || !request.getSolicitanteId().equals(orden.getUsuario().getIdUsuario())) {
            return ResponseEntity.badRequest().body("Solo el solicitante puede registrar el estado LIBERADO.");
        }

        String comentario = request.getComentario() == null ? null : request.getComentario().trim();
        if (comentario != null && comentario.length() > 255) {
            return ResponseEntity.badRequest().body("comentario no debe exceder 255 caracteres.");
        }
        if (comentario == null || comentario.isBlank()) {
            comentario = "Incidencia liberada por solicitante.";
        }

        OrdenEvento created;
        try {
            created = createHistorialEntry(orden, 5, LocalDate.now(), comentario);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Incidencia liberada correctamente.");
        response.put("folio", folio);
        response.put("estatus", orden.getEstatus());
        response.put("idEvento", created.getId_evento().getId());
        response.put("estado", created.getEstadoEvento() == null ? null : created.getEstadoEvento().getNombre());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/incidencias/tecnicos")
    public ResponseEntity<?> getTecnicosDisponibles() {
        List<Usuario> users = usuarioService.findAll();
        List<Map<String, Object>> tecnicos = new ArrayList<>();

        for (Usuario user : users) {
            Integer nivelId = user.getNivel() == null ? null : user.getNivel().getId_nivel();
            if (nivelId == null || nivelId != 2) {
                continue;
            }

            Map<String, Object> tecnico = new LinkedHashMap<>();
            tecnico.put("idUsuario", user.getIdUsuario());
            tecnico.put("nombreCompleto", buildNombreUsuario(user));
            tecnico.put("username", user.getUsername());
            tecnicos.add(tecnico);
        }

        return ResponseEntity.ok(tecnicos);
    }

    @PatchMapping("/incidencias/{folio}/prioridad")
    public ResponseEntity<?> updatePrioridadIncidencia(
            @PathVariable Integer folio,
            @RequestBody PrioridadUpdateRequest request) {
        if (request == null || request.getPrioridad() == null) {
            return ResponseEntity.badRequest().body("prioridad es obligatoria.");
        }

        Integer prioridad = request.getPrioridad();
        if (prioridad < 1 || prioridad > 3) {
            return ResponseEntity.badRequest().body("prioridad debe ser 1 (Alta), 2 (Media) o 3 (Baja).");
        }

        Optional<Orden> ordenOpt = ordenService.findById(folio);
        if (ordenOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No existe la incidencia con folio: " + folio);
        }

        Orden orden = ordenOpt.get();
        orden.setPrioridad(prioridad);
        Orden updated = ordenService.save(orden);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Prioridad actualizada correctamente.");
        response.put("folio", updated.getFolio());
        response.put("prioridad", updated.getPrioridad());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/incidencias/{folio}/tipo")
    public ResponseEntity<?> updateTipoIncidencia(
            @PathVariable Integer folio,
            @RequestBody TipoUpdateRequest request) {
        if (request == null || request.getTipo() == null) {
            return ResponseEntity.badRequest().body("tipo es obligatorio.");
        }

        Integer tipo = request.getTipo();
        if (tipo < 1 || tipo > 4) {
            return ResponseEntity.badRequest().body("tipo debe ser 1 (Hardware), 2 (Software), 3 (Redes) o 4 (Telefonia).");
        }

        Optional<Orden> ordenOpt = ordenService.findById(folio);
        if (ordenOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No existe la incidencia con folio: " + folio);
        }

        Orden orden = ordenOpt.get();
        orden.setTipo(tipo);
        Orden updated = ordenService.save(orden);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Tipo actualizado correctamente.");
        response.put("folio", updated.getFolio());
        response.put("tipo", updated.getTipo());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/incidencias/{folio}/tiempo-estimado")
    public ResponseEntity<?> updateTiempoEstimadoIncidencia(
            @PathVariable Integer folio,
            @RequestBody TiempoEstimadoUpdateRequest request) {
        if (request == null || request.getTiempoEstimado() == null) {
            return ResponseEntity.badRequest().body("tiempoEstimado es obligatorio.");
        }

        Integer tiempoEstimado = request.getTiempoEstimado();
        if (tiempoEstimado < 1 || tiempoEstimado > 1000) {
            return ResponseEntity.badRequest().body("tiempoEstimado debe estar entre 1 y 1000 horas.");
        }

        Optional<Orden> ordenOpt = ordenService.findById(folio);
        if (ordenOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No existe la incidencia con folio: " + folio);
        }

        Orden orden = ordenOpt.get();
        orden.setTiempoEstimado(tiempoEstimado);
        Orden updated = ordenService.save(orden);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Tiempo estimado actualizado correctamente.");
        response.put("folio", updated.getFolio());
        response.put("tiempoEstimado", updated.getTiempoEstimado());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/incidencias/{folio}/clasificacion")
    public ResponseEntity<?> updateClasificacionIncidencia(
            @PathVariable Integer folio,
            @RequestBody ClasificacionUpdateRequest request) {
        if (request == null
                || request.getPrioridad() == null
                || request.getTipo() == null
                || request.getTiempoEstimado() == null
                || request.getTecnicoId() == null) {
            return ResponseEntity.badRequest()
                    .body("prioridad, tipo, tiempoEstimado y tecnicoId son obligatorios.");
        }

        Integer prioridad = request.getPrioridad();
        if (prioridad < 1 || prioridad > 3) {
            return ResponseEntity.badRequest().body("prioridad debe ser 1 (Alta), 2 (Media) o 3 (Baja).");
        }

        Integer tipo = request.getTipo();
        if (tipo < 1 || tipo > 4) {
            return ResponseEntity.badRequest()
                    .body("tipo debe ser 1 (Hardware), 2 (Software), 3 (Redes) o 4 (Telefonia).");
        }

        Integer tiempoEstimado = request.getTiempoEstimado();
        if (tiempoEstimado < 1 || tiempoEstimado > 1000) {
            return ResponseEntity.badRequest().body("tiempoEstimado debe estar entre 1 y 1000 horas.");
        }

        Optional<Usuario> tecnicoOpt = usuarioService.findById(request.getTecnicoId());
        if (tecnicoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("tecnicoId no existe.");
        }
        Usuario tecnico = tecnicoOpt.get();
        Integer nivelId = tecnico.getNivel() == null ? null : tecnico.getNivel().getId_nivel();
        if (nivelId == null || nivelId != 2) {
            return ResponseEntity.badRequest().body("El usuario seleccionado no es tecnico.");
        }

        Optional<Orden> ordenOpt = ordenService.findById(folio);
        if (ordenOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No existe la incidencia con folio: " + folio);
        }

        Orden orden = ordenOpt.get();
        boolean wasUnassigned = orden.getEncargado() == null;
        orden.setPrioridad(prioridad);
        orden.setTipo(tipo);
        orden.setTiempoEstimado(tiempoEstimado);
        orden.setEncargado(tecnico);
        Orden updated = ordenService.save(orden);

        if (wasUnassigned) {
            createHistorialEntry(
                    updated,
                    1,
                    LocalDate.now(),
                    "Incidencia iniciada y asignada al tecnico: " + safeText(buildNombreUsuario(tecnico)));
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Clasificacion actualizada correctamente.");
        response.put("folio", updated.getFolio());
        response.put("prioridad", updated.getPrioridad());
        response.put("tipo", updated.getTipo());
        response.put("tiempoEstimado", updated.getTiempoEstimado());
        response.put("encargadoId", updated.getEncargado() == null ? null : updated.getEncargado().getIdUsuario());
        response.put("encargado", buildNombreUsuario(updated.getEncargado()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/incidencias")
    public ResponseEntity<?> createIncidencia(@RequestBody DtoOrdenIncidenciaCreateReq request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("Se requiere el payload de la incidencia.");
        }
        if (request.getEspacioId() == null) {
            return ResponseEntity.badRequest().body("espacioId es obligatorio.");
        }
        if (request.getActivoId() == null) {
            return ResponseEntity.badRequest().body("activoId es obligatorio.");
        }
        if (request.getDescripcion() == null || request.getDescripcion().isBlank()) {
            return ResponseEntity.badRequest().body("descripcion es obligatoria.");
        }
        if (request.getDescripcion().trim().length() > 255) {
            return ResponseEntity.badRequest().body("descripcion no debe exceder 255 caracteres.");
        }

        Optional<Activo> activo = activoService.findById(request.getActivoId());
        if (activo.isEmpty()) {
            return ResponseEntity.badRequest().body("El activo no existe.");
        }
        if (activo.get().getEspacio() == null || !request.getEspacioId().equals(activo.get().getEspacio().getIdEspacio())) {
            return ResponseEntity.badRequest().body("El activo no pertenece al espacio seleccionado.");
        }

        Usuario usuarioSolicitante = null;
        if (request.getUsuarioId() != null) {
            usuarioSolicitante = usuarioService.findById(request.getUsuarioId()).orElse(null);
            if (usuarioSolicitante == null) {
                return ResponseEntity.badRequest().body("usuarioId no existe.");
            }
        }

        Orden orden = new Orden();
        orden.setFolio(ordenService.getNextFolio());
        orden.setPrioridad(null);
        orden.setTipo(null);
        orden.setTiempoEstimado(null);
        orden.setUsuario(usuarioSolicitante);
        orden.setActivo(activo.get());
        orden.setFechaCreacion(LocalDate.now());
        orden.setEstatus("ABIERTA");
        orden.setDescripcion(request.getDescripcion().trim());

        Orden created = ordenService.save(orden);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Incidencia creada correctamente.");
        response.put("folio", created.getFolio());
        response.put("estatus", created.getEstatus());
        response.put("fechaCreacion", created.getFechaCreacion());
        return ResponseEntity.ok(response);
    }

    private String buildNombreUsuario(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        StringBuilder nombreCompleto = new StringBuilder();
        appendIfPresent(nombreCompleto, usuario.getNombre());
        appendIfPresent(nombreCompleto, usuario.getApellidoP());
        appendIfPresent(nombreCompleto, usuario.getApellidoM());
        return nombreCompleto.isEmpty() ? null : nombreCompleto.toString();
    }

    private String safeText(String value) {
        return value == null || value.isBlank() ? "-" : value.trim();
    }

    private void appendIfPresent(StringBuilder builder, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (!builder.isEmpty()) {
            builder.append(" ");
        }
        builder.append(value.trim());
    }

    private OrdenEvento createHistorialEntry(Orden orden, Integer estadoId, LocalDate fecha, String comentario) {
        Optional<EstadoEvento> estadoOpt = estadoEventoService.findById(estadoId);
        if (estadoOpt.isEmpty()) {
            throw new IllegalArgumentException("estadoId no existe.");
        }
        EstadoEvento estado = estadoOpt.get();

        if (estado.getNombre() != null && !estado.getNombre().isBlank()) {
            orden.setEstatus(estado.getNombre().trim());
            ordenService.save(orden);
        }

        Evento evento = new Evento();
        evento.setId(eventoService.getNextId());
        evento.setClave("ORD-" + orden.getFolio() + "-EV-" + evento.getId());
        evento.setDescripcion("Historial incidencia folio " + orden.getFolio());
        Evento savedEvento = eventoService.save(evento);

        OrdenEvento ordenEvento = new OrdenEvento();
        ordenEvento.setId_evento(savedEvento);
        ordenEvento.setFolio(orden);
        ordenEvento.setEstadoEvento(estado);
        ordenEvento.setFecha(fecha);
        ordenEvento.setComentario(comentario);
        return ordenEventoService.save(ordenEvento);
    }

    public static class PrioridadUpdateRequest {
        private Integer prioridad;

        public Integer getPrioridad() {
            return prioridad;
        }

        public void setPrioridad(Integer prioridad) {
            this.prioridad = prioridad;
        }
    }

    public static class TipoUpdateRequest {
        private Integer tipo;

        public Integer getTipo() {
            return tipo;
        }

        public void setTipo(Integer tipo) {
            this.tipo = tipo;
        }
    }

    public static class TiempoEstimadoUpdateRequest {
        private Integer tiempoEstimado;

        public Integer getTiempoEstimado() {
            return tiempoEstimado;
        }

        public void setTiempoEstimado(Integer tiempoEstimado) {
            this.tiempoEstimado = tiempoEstimado;
        }
    }

    public static class ClasificacionUpdateRequest {
        private Integer prioridad;
        private Integer tipo;
        private Integer tiempoEstimado;
        private Integer tecnicoId;

        public Integer getPrioridad() {
            return prioridad;
        }

        public void setPrioridad(Integer prioridad) {
            this.prioridad = prioridad;
        }

        public Integer getTipo() {
            return tipo;
        }

        public void setTipo(Integer tipo) {
            this.tipo = tipo;
        }

        public Integer getTiempoEstimado() {
            return tiempoEstimado;
        }

        public void setTiempoEstimado(Integer tiempoEstimado) {
            this.tiempoEstimado = tiempoEstimado;
        }

        public Integer getTecnicoId() {
            return tecnicoId;
        }

        public void setTecnicoId(Integer tecnicoId) {
            this.tecnicoId = tecnicoId;
        }
    }

    public static class HistorialEventoCreateRequest {
        private Integer estadoId;
        private String fecha;
        private String comentario;
        private Integer idTecnico;

        public Integer getEstadoId() {
            return estadoId;
        }

        public void setEstadoId(Integer estadoId) {
            this.estadoId = estadoId;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getComentario() {
            return comentario;
        }

        public void setComentario(String comentario) {
            this.comentario = comentario;
        }

        public Integer getIdTecnico() {
            return idTecnico;
        }

        public void setIdTecnico(Integer idTecnico) {
            this.idTecnico = idTecnico;
        }
    }

    public static class LiberarIncidenciaRequest {
        private Integer solicitanteId;
        private String comentario;

        public Integer getSolicitanteId() {
            return solicitanteId;
        }

        public void setSolicitanteId(Integer solicitanteId) {
            this.solicitanteId = solicitanteId;
        }

        public String getComentario() {
            return comentario;
        }

        public void setComentario(String comentario) {
            this.comentario = comentario;
        }
    }
}
