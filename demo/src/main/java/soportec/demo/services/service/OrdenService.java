package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Orden;

public interface OrdenService {

    List<Orden> findAll();

    List<Orden> findAllByFechaCreacionAsc();

    List<Orden> findByTecnicoAsignado(Integer tecnicoId);

    List<Orden> findBySolicitante(Integer solicitanteId);

    Optional<Orden> findById(Integer id);

    Orden save(Orden entity);

    void deleteById(Integer id);

    Integer getNextFolio();
}
