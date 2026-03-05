package soportec.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.ModeloActivo;

public interface ModeloActivoRepository extends JpaRepository<ModeloActivo, Integer> {

    List<ModeloActivo> findByMarcaActivo_IdMarcaActivoOrderByNombreAsc(Integer idMarcaActivo);
}
