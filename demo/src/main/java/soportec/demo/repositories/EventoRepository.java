package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import soportec.demo.models.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

    @Query("select coalesce(max(e.id), 0) from Evento e")
    Integer findMaxId();
}
