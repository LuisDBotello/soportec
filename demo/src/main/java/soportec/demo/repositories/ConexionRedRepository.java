package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.ConexionRed;

public interface ConexionRedRepository extends JpaRepository<ConexionRed, Integer> {
}
