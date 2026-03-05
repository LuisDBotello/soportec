package soportec.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import soportec.demo.models.Nic;

public interface NicRepository extends JpaRepository<Nic, Integer> {

    List<Nic> findByActivoIsNull();

    Optional<Nic> findByIdNicAndActivoIsNull(Integer idNic);

    @Query("select coalesce(max(n.idNic), 0) from Nic n")
    Integer findMaxIdNic();
}
