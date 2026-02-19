package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Periodo;

public interface PeriodoRepository extends JpaRepository<Periodo, Integer> {
}
