package uam.mx.demometodologia.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import uam.mx.demometodologia.entities.Claves;
import uam.mx.demometodologia.entities.Encuestado;
import uam.mx.demometodologia.entities.Respuesta;
import uam.mx.demometodologia.repositories.ClavesRepository;
import uam.mx.demometodologia.repositories.EncuestadoRepository;
import uam.mx.demometodologia.repositories.RespuestasRepository;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final RespuestasRepository respuestaRepository;
    private final EncuestadoRepository encuestadoRepository;
    private final ClavesRepository clavesRepository;

    @MessageMapping("/response")
    @SendTo("/topic/responses")
    public List<Respuesta> handleResponse(String responsePayload, SimpMessageHeaderAccessor headerAccessor) {
        // Parsear el JSON
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responsePayload);
            String name = jsonNode.get("name").asText();
            String key = jsonNode.get("key").asText();
            String response = jsonNode.get("response").asText();
            String sessionId = headerAccessor.getSessionId();
            Claves clave = clavesRepository.findByClaveEscrita(key);
            if(clave!=null) {
                // Buscar o crear un encuestado por nombre utilizando Optional
                Encuestado encuestado = encuestadoRepository.findByNombre(name)
                        .orElseGet(() -> {
                            Encuestado nuevoEncuestado = new Encuestado();
                            nuevoEncuestado.setNombre(name);
                            nuevoEncuestado.setClaves(clave);
                            //nuevoEncuestado.setClave(key);  // Si también quieres guardar la clave
                            encuestadoRepository.save(nuevoEncuestado); // Se almacena el encuestado
                            return nuevoEncuestado;
                        });

                // Guardar la respuesta
                Respuesta respuesta = new Respuesta();
                respuesta.setEncuestado(encuestado);
                respuesta.setResOriginal(response);
                respuesta.setSessionId(sessionId);
                respuestaRepository.save(respuesta);


                // Retornar todas las respuestas
                return respuestaRepository.findAll();
            } else{
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
