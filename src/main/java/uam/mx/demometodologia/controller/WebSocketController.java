package uam.mx.demometodologia.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import uam.mx.demometodologia.dto.RespuestaDTO;
import uam.mx.demometodologia.entities.Claves;
import uam.mx.demometodologia.entities.Encuestado;
import uam.mx.demometodologia.entities.Respuesta;
import uam.mx.demometodologia.repositories.ClavesRepository;
import uam.mx.demometodologia.repositories.EncuestadoRepository;
import uam.mx.demometodologia.repositories.RespuestasRepository;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final RespuestasRepository respuestaRepository;
    private final EncuestadoRepository encuestadoRepository;
    private final ClavesRepository clavesRepository;

    @MessageMapping("/response")
    @SendTo("/topic/responses")
    public List<RespuestaDTO> handleResponse(String responsePayload, SimpMessageHeaderAccessor headerAccessor) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responsePayload);
            String name = jsonNode.get("name").asText();
            String key = jsonNode.get("key").asText();
            String response = jsonNode.get("response").asText();
            String sessionId = headerAccessor.getSessionId();

            Claves clave = clavesRepository.findByClaveEscrita(key);
            if (clave != null) {
                Encuestado encuestado = encuestadoRepository.findByNombre(name)
                        .orElseGet(() -> {
                            Encuestado nuevoEncuestado = new Encuestado();
                            nuevoEncuestado.setNombre(name);
                            nuevoEncuestado.setClaves(clave);
                            encuestadoRepository.save(nuevoEncuestado);
                            return nuevoEncuestado;
                        });

                Respuesta respuesta = new Respuesta();
                respuesta.setEncuestado(encuestado);
                respuesta.setResOriginal(response);
                respuesta.setSessionId(sessionId);
                respuestaRepository.save(respuesta);

                // Convertir todas las respuestas a DTOs antes de enviarlas
                List<RespuestaDTO> respuestasDTO = respuestaRepository.findAll().stream()
                        .map(res -> new RespuestaDTO(res.getEncuestado().getNombre(), res.getResOriginal()))
                        .collect(Collectors.toList());

                return respuestasDTO;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
