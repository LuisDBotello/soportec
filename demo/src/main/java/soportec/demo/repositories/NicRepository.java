package soportec.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Nic;

public interface NicRepository extends JpaRepository<Nic, Integer> {

    List<Nic> findByActivoIsNull();

    Optional<Nic> findByIdNicAndActivoIsNull(Integer idNic);
}
