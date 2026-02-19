package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Orden;

public interface OrdenRepository extends JpaRepository<Orden, Integer> {
}
