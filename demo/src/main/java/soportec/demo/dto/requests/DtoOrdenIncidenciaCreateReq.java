package soportec.demo.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoOrdenIncidenciaCreateReq {

    private Integer usuarioId;
    private Integer espacioId;
    private Integer activoId;
    private String descripcion;
    private Integer prioridad;
}
