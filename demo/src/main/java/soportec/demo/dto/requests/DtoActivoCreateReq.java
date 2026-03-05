package soportec.demo.dto.requests;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoActivoCreateReq {

    private Integer categoriaId;
    private Integer tipoActivoId;
    private String fechaCompra;
    private Integer estadoId;
    private Integer ubicacionId;
    private Integer marcaActivoId;
    private Integer modeloActivoId;
    private String marca;
    private String modelo;
    private String numeroSerie;

    private List<ComponenteReq> componentes = new ArrayList<>();

    private Integer cpuDisponibleId;
    private Integer ramDisponibleId;
    private Integer discoDisponibleId;
    private Integer motherboardDisponibleId;
    private Integer nicDisponibleId;

    @Getter
    @Setter
    public static class ComponenteReq {

        private String tipo;
        private Integer modeloId;
        private String numeroSerie;
        private String fechaCompra;
    }
}
