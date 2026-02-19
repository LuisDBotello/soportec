package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
}
