package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import soportec.demo.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query(value = """
            SELECT * FROM USUARIO WHERE username = :username
            """, nativeQuery = true)
    public Usuario findByUsername(String username);
}
