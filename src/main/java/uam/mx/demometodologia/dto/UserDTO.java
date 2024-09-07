package uam.mx.demometodologia.dto;

public class UserDTO {
    private String nombreUsuario;
    private String sessionId;

    // Constructor vacío
    public UserDTO() {}

    // Constructor con parámetros
    public UserDTO(String nombreUsuario, String sessionId) {
        this.nombreUsuario = nombreUsuario;
        this.sessionId = sessionId;
    }

    // Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
