package soportec.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.DiscoModelo;

public interface DiscoModeloRepository extends JpaRepository<DiscoModelo, Integer> {

    List<DiscoModelo> findByMarcaDisco_IdMarcaDisco(Integer idMarcaDisco);
}
