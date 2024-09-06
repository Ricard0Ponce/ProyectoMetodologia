package uam.mx.demometodologia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncuestaDTO {
    private Long id;
    private String nombre;
    private List<RespuestaDTO> respuestas;
}
