package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Motherboard;

public interface MotherboardRepository extends JpaRepository<Motherboard, Integer> {
}
