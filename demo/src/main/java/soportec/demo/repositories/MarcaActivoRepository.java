package soportec.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.MarcaActivo;

public interface MarcaActivoRepository extends JpaRepository<MarcaActivo, Integer> {

    List<MarcaActivo> findByTipoActivo_IdTipoActivoOrderByNombreAsc(Integer idTipoActivo);
}
