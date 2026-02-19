package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
}
