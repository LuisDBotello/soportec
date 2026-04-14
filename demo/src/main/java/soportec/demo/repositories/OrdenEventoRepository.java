package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import soportec.demo.models.OrdenEvento;
import soportec.demo.models.ids.OrdenEventoId;

public interface OrdenEventoRepository extends JpaRepository<OrdenEvento, OrdenEventoId> {

    @Query("select oe from OrdenEvento oe where oe.folio.folio = :folio order by oe.fecha asc, oe.id_evento.id asc")
    List<OrdenEvento> findByFolio(@Param("folio") Integer folio);
}
