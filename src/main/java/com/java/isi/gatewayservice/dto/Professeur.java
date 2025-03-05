package com.java.isi.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Professeur {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Matiere matiere; // ID de la matière associée
}