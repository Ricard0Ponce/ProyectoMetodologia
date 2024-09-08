package uam.mx.demometodologia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uam.mx.demometodologia.entities.Respuesta;

import java.util.List;

public interface RespuestasRepository extends JpaRepository<Respuesta, Long> {
    Respuesta findBySessionId(String sessionId);
    List<Respuesta> findByEncuestado_Claves_ClaveEscrita(String claveEscrita);
    List<Respuesta> findByEncuestado_Claves_ClaveEscritaAndRonda(String clave, Integer ronda);
}
