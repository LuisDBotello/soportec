package soportec.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.RamModelo;

public interface RamModeloRepository extends JpaRepository<RamModelo, Integer> {

    List<RamModelo> findByMarcaRam_IdMarcaRam(Integer idMarcaRam);
}
