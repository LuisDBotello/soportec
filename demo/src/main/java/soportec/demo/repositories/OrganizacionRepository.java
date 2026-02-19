package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.Organizacion;

public interface OrganizacionRepository extends JpaRepository<Organizacion, Integer> {
}
