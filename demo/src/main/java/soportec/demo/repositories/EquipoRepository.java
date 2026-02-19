package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
}
