package uam.mx.demometodologia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uam.mx.demometodologia.entities.Claves;

public interface ClavesRepository extends JpaRepository<Claves, Long> {
    Claves findByClaveEscrita(String key);
}
