package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import soportec.demo.models.TipoActivo;

public interface TipoActivoRepository extends JpaRepository<TipoActivo, Integer> {

    @Query(value = "SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TipoActivo t WHERE t.nombre = 'Escritorio' AND t.idTipoActivo = :idTipoActivo", nativeQuery = false)
    public Boolean esEscritorio(TipoActivo tipoActivo);
}
