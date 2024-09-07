package uam.mx.demometodologia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uam.mx.demometodologia.entities.Claves;
import uam.mx.demometodologia.repositories.ClavesRepository;

@Service
@RequiredArgsConstructor
public class EntrarService {

    private final ClavesRepository clavesRepository;

    public boolean verificarClave(String clave) {
        // Verifica si la clave existe en la base de datos
        return clavesRepository.existsByClaveEscrita(clave);
    }
}