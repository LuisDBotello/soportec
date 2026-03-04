package soportec.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Ram;

public interface RamRepository extends JpaRepository<Ram, Integer> {

    List<Ram> findByActivoIsNull();

    Optional<Ram> findByIdRamAndActivoIsNull(Integer idRam);
}
