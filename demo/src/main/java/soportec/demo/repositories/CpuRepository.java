package soportec.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import soportec.demo.models.Cpu;

public interface CpuRepository extends JpaRepository<Cpu, Integer> {

    List<Cpu> findByActivoIsNull();

    Optional<Cpu> findByIdProcesadorAndActivoIsNull(Integer idProcesador);

    @Query("select coalesce(max(c.idProcesador), 0) from Cpu c")
    Integer findMaxIdProcesador();
}
