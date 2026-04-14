package soportec.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Software;

public interface SoftwareRepository extends JpaRepository<Software, Integer> {

    List<Software> findAllByOrderByNombreAsc();
}
