package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Disco;

public interface DiscoRepository extends JpaRepository<Disco, Integer> {
}
