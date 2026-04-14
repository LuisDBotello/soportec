package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import soportec.demo.models.Orden;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    @Query("select coalesce(max(o.folio), 0) from Orden o")
    Integer findMaxFolio();

    @Query("select o from Orden o order by o.fechaCreacion asc, o.folio asc")
    List<Orden> findAllByFechaCreacionAsc();

    @Query("select o from Orden o where o.encargado.idUsuario = :tecnicoId order by o.fechaCreacion desc, o.folio desc")
    List<Orden> findByTecnicoAsignado(@Param("tecnicoId") Integer tecnicoId);

    @Query("select o from Orden o where o.usuario.idUsuario = :solicitanteId order by o.fechaCreacion desc, o.folio desc")
    List<Orden> findBySolicitante(@Param("solicitanteId") Integer solicitanteId);
}
