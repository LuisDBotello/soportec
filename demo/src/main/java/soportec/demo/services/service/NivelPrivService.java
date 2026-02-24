package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.NivelPriv;

public interface NivelPrivService {

    List<NivelPriv> findAll();

    Optional<NivelPriv> findById(Integer id);

    NivelPriv save(NivelPriv entity);

    void deleteById(Integer id);
}
