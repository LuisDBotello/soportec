package soportec.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.NicModelo;

public interface NicModeloRepository extends JpaRepository<NicModelo, Integer> {

    List<NicModelo> findByMarcaNic_IdMarcaNic(Integer idMarcaNic);
}
