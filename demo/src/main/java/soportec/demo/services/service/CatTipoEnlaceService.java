package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.CatTipoEnlace;

public interface CatTipoEnlaceService {

    List<CatTipoEnlace> findAll();

    Optional<CatTipoEnlace> findById(Integer id);

    CatTipoEnlace save(CatTipoEnlace entity);

    void deleteById(Integer id);
}
