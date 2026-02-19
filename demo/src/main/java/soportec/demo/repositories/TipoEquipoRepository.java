package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.TipoEquipo;

public interface TipoEquipoRepository extends JpaRepository<TipoEquipo, Integer> {
}
