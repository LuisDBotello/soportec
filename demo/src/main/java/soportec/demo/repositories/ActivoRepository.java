package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Activo;

public interface ActivoRepository extends JpaRepository<Activo, Integer> {
}
