package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Evaluacion;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Integer> {
}
