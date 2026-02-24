package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.CatTipoRed;

public interface CatTipoRedService {

    List<CatTipoRed> findAll();

    Optional<CatTipoRed> findById(Integer id);

    CatTipoRed save(CatTipoRed entity);

    void deleteById(Integer id);
}
