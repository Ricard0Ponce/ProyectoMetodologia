package uam.mx.demometodologia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uam.mx.demometodologia.entities.Encuestado;
import uam.mx.demometodologia.entities.Respuesta;

import java.util.List;
import java.util.Optional;

@Repository
public interface EncuestadoRepository extends JpaRepository<Encuestado, Long> {
    Optional<Encuestado> findByNombre(String nombre);
    List<Respuesta> findAllByClaves_Id(Long id);
}
