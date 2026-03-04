package soportec.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import soportec.demo.models.TipoActivo;

public interface TipoActivoRepository extends JpaRepository<TipoActivo, Integer> {

    @Query(value = "SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TipoActivo t WHERE t.nombre = 'Escritorio' AND t.idTipoActivo = :idTipoActivo", nativeQuery = false)
    public Boolean esEscritorio(TipoActivo tipoActivo);

    @Query("SELECT t FROM TipoActivo t WHERE t.categoriaActivo.idCategoriaActivo = :idCategoriaActivo")
    List<TipoActivo> findByCategoriaActivo(@Param("idCategoriaActivo") Integer idCategoriaActivo);
}
