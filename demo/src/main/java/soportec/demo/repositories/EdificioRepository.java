package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Edificio;

public interface EdificioRepository extends JpaRepository<Edificio, Integer> {
}
