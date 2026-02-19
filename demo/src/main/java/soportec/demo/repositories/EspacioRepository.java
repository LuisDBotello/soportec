package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Espacio;

public interface EspacioRepository extends JpaRepository<Espacio, Integer> {
}
