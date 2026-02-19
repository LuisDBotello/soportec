package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Usuario;

public interface UsuarioService {

    List<Usuario> findAll();

    Optional<Usuario> findById(Integer id);

    Usuario save(Usuario entity);

    void deleteById(Integer id);

    Usuario findByUsername(String username);
}
