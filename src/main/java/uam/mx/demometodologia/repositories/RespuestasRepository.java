package uam.mx.demometodologia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uam.mx.demometodologia.entities.Respuesta;

public interface RespuestasRepository extends JpaRepository<Respuesta, Long> {
    Respuesta findBySessionId(String sessionId);
}
