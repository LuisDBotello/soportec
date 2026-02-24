package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.NivelPriv;

public interface NivelPrivRepository extends JpaRepository<NivelPriv, Integer> {
}
