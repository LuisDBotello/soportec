package soportec.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import soportec.demo.models.Espacio;

public interface EspacioRepository extends JpaRepository<Espacio, Integer> {

    @Query("SELECT e FROM Espacio e WHERE e.edificio.id = :idEdificio")
    List<Espacio> findByEdificioId(Integer idEdificio);
}
