package soportec.demo.dto.requests;

import lombok.Getter;
import lombok.Setter;
import soportec.demo.models.Activo;
import soportec.demo.models.Cpu;
import soportec.demo.models.Disco;
import soportec.demo.models.Motherboard;
import soportec.demo.models.Nic;
import soportec.demo.models.Ram;

@Getter
@Setter
public class DtoActivoCreateReq {
    
    private Activo activo;
    private Cpu cpu;
    private Ram ram;
    private Disco disco;
    private Motherboard motherboard;
    private Nic nic;

}
