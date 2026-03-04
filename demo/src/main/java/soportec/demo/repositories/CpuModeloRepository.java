package soportec.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.CpuModelo;

public interface CpuModeloRepository extends JpaRepository<CpuModelo, Integer> {

    List<CpuModelo> findByMarcaCpu_IdMarcaCpu(Integer idMarcaCpu);
}
