package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.CpuModelo;

public interface CpuModeloRepository extends JpaRepository<CpuModelo, Integer> {
}
