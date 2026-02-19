package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.EquipoCategoria;

public interface EquipoCategoriaService {

    List<EquipoCategoria> findAll();

    Optional<EquipoCategoria> findById(Integer id);

    EquipoCategoria save(EquipoCategoria entity);

    void deleteById(Integer id);
}
