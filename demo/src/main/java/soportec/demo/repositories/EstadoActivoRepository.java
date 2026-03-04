package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.EstadoActivo;

public interface EstadoActivoRepository extends JpaRepository<EstadoActivo, Integer> {
}
