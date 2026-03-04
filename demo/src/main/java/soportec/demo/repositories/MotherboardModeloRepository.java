package soportec.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.MotherboardModelo;

public interface MotherboardModeloRepository extends JpaRepository<MotherboardModelo, Integer> {

    List<MotherboardModelo> findByMarcaMotherboard_IdMarcaMotherboard(Integer idMarcaMotherboard);
}
