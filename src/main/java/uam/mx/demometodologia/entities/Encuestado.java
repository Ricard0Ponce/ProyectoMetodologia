package uam.mx.demometodologia.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name="encuestado")
public class Encuestado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "encuestado", cascade = CascadeType.ALL)
    private List<Respuesta> respuestas = new ArrayList<>();

    @ManyToOne
    Claves claves;


}
