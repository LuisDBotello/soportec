package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
}
