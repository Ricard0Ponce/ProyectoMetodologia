package uam.mx.demometodologia.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "claves")
public class Claves {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String claveEscrita;

    @OneToMany
    List<Encuestado> encuestado;

}
