package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Sysdiagram;

public interface SysdiagramRepository extends JpaRepository<Sysdiagram, Integer> {
}
