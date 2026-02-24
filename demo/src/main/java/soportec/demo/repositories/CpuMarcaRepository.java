package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.CpuMarca;

public interface CpuMarcaRepository extends JpaRepository<CpuMarca, Integer> {
}
