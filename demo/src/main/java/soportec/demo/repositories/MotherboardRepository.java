package soportec.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Motherboard;

public interface MotherboardRepository extends JpaRepository<Motherboard, Integer> {

    List<Motherboard> findByActivoIsNull();

    Optional<Motherboard> findByIdMotherboardAndActivoIsNull(Integer idMotherboard);
}
