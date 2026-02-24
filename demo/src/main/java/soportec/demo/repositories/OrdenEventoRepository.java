package soportec.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import soportec.demo.models.OrdenEvento;
import soportec.demo.models.ids.OrdenEventoId;

public interface OrdenEventoRepository extends JpaRepository<OrdenEvento, OrdenEventoId> {
}
