package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.NicMarca;

public interface NicMarcaRepository extends JpaRepository<NicMarca, Integer> {
}
