package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Ram;

public interface RamRepository extends JpaRepository<Ram, Integer> {
}
