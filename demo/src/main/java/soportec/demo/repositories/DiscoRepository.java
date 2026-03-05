package soportec.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import soportec.demo.models.Disco;

public interface DiscoRepository extends JpaRepository<Disco, Integer> {

    List<Disco> findByActivoIsNull();

    Optional<Disco> findByIdDiscoAndActivoIsNull(Integer idDisco);

    @Query("select coalesce(max(d.idDisco), 0) from Disco d")
    Integer findMaxIdDisco();
}
