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

    // fetch = FetchType.EAGER
    @OneToMany(mappedBy = "encuestado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Respuesta> respuestas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "claves_id", nullable = false)
    Claves claves;
}
