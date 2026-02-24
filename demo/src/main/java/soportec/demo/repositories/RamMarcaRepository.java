package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.RamMarca;

public interface RamMarcaRepository extends JpaRepository<RamMarca, Integer> {
}
