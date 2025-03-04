package com.java.isi.gatewayservice.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Professeur {

    private Long id = null;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Matiere matiere = null; // ID de la matière associée
}