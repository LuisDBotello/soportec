package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.CatVelocidad;

public interface CatVelocidadRepository extends JpaRepository<CatVelocidad, Integer> {
}
