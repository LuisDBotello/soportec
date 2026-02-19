package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
}
