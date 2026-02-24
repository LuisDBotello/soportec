package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.RamModelo;

public interface RamModeloRepository extends JpaRepository<RamModelo, Integer> {
}
