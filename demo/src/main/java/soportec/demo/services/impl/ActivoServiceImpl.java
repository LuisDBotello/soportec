package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import soportec.demo.dto.requests.DtoActivoCreateReq;
import soportec.demo.models.Activo;
import soportec.demo.models.Cpu;
import soportec.demo.models.Disco;
import soportec.demo.models.Motherboard;
import soportec.demo.models.Nic;
import soportec.demo.models.Ram;
import soportec.demo.repositories.ActivoRepository;
import soportec.demo.services.service.ActivoService;

@Service
public class ActivoServiceImpl implements ActivoService {

    ActivoRepository repository;    
    TipoActivoServiceImpl tipoActivoService;
    CpuServiceImpl cpuService;
    RamServiceImpl ramService;
    DiscoServiceImpl discoService;
    MotherboardServiceImpl motherboardService;
    NicServiceImpl nicService;

    @Transactional
    public ResponseEntity<?> createActivo(DtoActivoCreateReq request) {
        
        Activo activo = new Activo();

        activo.setFechaCompra(request.getActivo().getFechaCompra());
        activo.setTipoActivo(request.getActivo().getTipoActivo());
        activo.setEstado(request.getActivo().getEstado());
        
        if (tipoActivoService.esEscritorio(request.getActivo().getTipoActivo())) {
            
            if (request.getCpu() == null || request.getRam() == null || request.getDisco() == null || request.getMotherboard() == null || request.getNic() == null) 
                return ResponseEntity.badRequest().body("Para un activo de tipo 'Escritorio', se deben proporcionar los componentes CPU, RAM, Disco, Motherboard y NIC.");
            
            try {
                cpuService.save(request.getCpu());
                ramService.save(request.getRam());
                discoService.save(request.getDisco());
                motherboardService.save(request.getMotherboard());
                nicService.save(request.getNic());

                repository.save(activo);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error al crear el activo: " + e.getMessage());
            }
            
            return ResponseEntity.ok().body("Equipo creado exitosamente");
        }

        

    }

    @Override
    public List<Activo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Activo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Activo save(Activo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
