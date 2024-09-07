package uam.mx.demometodologia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private String nombreUsuario;
    private String sessionId;
}

