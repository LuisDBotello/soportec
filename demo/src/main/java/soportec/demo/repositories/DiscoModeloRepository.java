package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.DiscoModelo;

public interface DiscoModeloRepository extends JpaRepository<DiscoModelo, Integer> {
}
