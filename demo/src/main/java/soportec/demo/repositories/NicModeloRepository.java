package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.NicModelo;

public interface NicModeloRepository extends JpaRepository<NicModelo, Integer> {
}
