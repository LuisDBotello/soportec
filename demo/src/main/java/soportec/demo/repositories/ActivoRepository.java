package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import soportec.demo.models.Activo;

public interface ActivoRepository extends JpaRepository<Activo, Integer> {

    @Query("select coalesce(max(a.idActivo), 0) from Activo a")
    Integer findMaxIdActivo();
}
