package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Nic;

public interface NicRepository extends JpaRepository<Nic, Integer> {
}
