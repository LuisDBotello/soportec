package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soportec.demo.models.OrdenEvento;
import soportec.demo.models.OrdenEventoId;

public interface OrdenEventoRepository extends JpaRepository<OrdenEvento, OrdenEventoId> {
}
