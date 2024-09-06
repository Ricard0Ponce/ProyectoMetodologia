package uam.mx.demometodologia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "respuestas")
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "res_original")
    private String resOriginal;

    @Column(name = "res_modificada")
    private String resModificada;

    @ManyToOne
    @JoinColumn(name = "encuestado_id", nullable = false)
    private Encuestado encuestado;

    //@ManyToOne
    //@JoinColumn(name = "encuesta_id", nullable = false)
    //private Encuesta encuesta;

    // Agregar el campo sessionId
    @Column(name = "session_id")
    private String sessionId;

    // Getters and Setters
}
