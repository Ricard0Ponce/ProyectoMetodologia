package uam.mx.demometodologia.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import uam.mx.demometodologia.dto.RespuestaDTO;
import uam.mx.demometodologia.dto.UserDTO;
import uam.mx.demometodologia.entities.Claves;
import uam.mx.demometodologia.entities.Encuestado;
import uam.mx.demometodologia.entities.Respuesta;
import uam.mx.demometodologia.repositories.ClavesRepository;
import uam.mx.demometodologia.repositories.EncuestadoRepository;
import uam.mx.demometodologia.repositories.RespuestasRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final RespuestasRepository respuestaRepository;
    private final EncuestadoRepository encuestadoRepository;
    private final ClavesRepository clavesRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    private final AtomicBoolean sessionStarted = new AtomicBoolean(false);
    private final List<UserDTO> connectedUsers = new ArrayList<>();

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

                List<RespuestaDTO> respuestasDTO = respuestaRepository.findByEncuestado_Claves_ClaveEscrita(key)
                        .stream()
                        .map(res -> new RespuestaDTO(res.getEncuestado().getNombre(), res.getResOriginal()))
                        .collect(Collectors.toList());
                return respuestasDTO;
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @MessageMapping("/start")
    @SendTo("/topic/start")
    public boolean startSession() {
        sessionStarted.set(true); // Iniciamos la sesion
        return true; // Regresamos un booleano que indica que la sesion ha iniciado
    }

    @MessageMapping("/connect")
    public void handleConnect(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        UserDTO user = new UserDTO("Usuario_" + sessionId, sessionId);
        connectedUsers.add(user);
        messagingTemplate.convertAndSend("/topic/users", connectedUsers);
    }

    @MessageMapping("/disconnect")
    public void handleDisconnect(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        connectedUsers.removeIf(user -> user.getSessionId().equals(sessionId));
        messagingTemplate.convertAndSend("/topic/users", connectedUsers);
    }

    // Agregamos una funcion para mostrarle al admin los usuarios que se van conectando

    @MessageMapping("/users")
    @SendTo("/topic/users")
    public List<UserDTO> getUsers() {
        return connectedUsers;
    }


}
