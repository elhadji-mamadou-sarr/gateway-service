package com.java.isi.gatewayservice.controller;

import com.java.isi.gatewayservice.dto.Matiere;
import com.java.isi.gatewayservice.dto.Professeur;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/matieres")
public class MatiereController {

    private final WebClient webClient;

    public MatiereController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }


    @GetMapping
    public Mono<String> listeDesMatieres(Model model) {
        return webClient.get()
                .uri("/api/matieres")
                .retrieve()
                .bodyToFlux(Matiere.class)
                .collectList()
                .defaultIfEmpty(List.of())
                .doOnNext(matieres -> model.addAttribute("matieres", matieres))
                .thenReturn("matieres/list"); // Renvoie le nom de la vue
    }

    // Afficher le formulaire d'ajout d'une matière
    @GetMapping("/ajout")
    public String formulaireAjout(Model model) {
        model.addAttribute("matiere", new Matiere());
        return "matieres/add";
    }

    // Ajouter une matière
    @PostMapping("/ajout")
    public Mono<String> ajouterMatiere(@ModelAttribute Matiere matiereDTO) {
        return webClient.post()
                .uri("/api/matieres")
                .bodyValue(matiereDTO)
                .retrieve()
                .bodyToMono(Matiere.class)
                .thenReturn("redirect:/matieres"); // Redirection vers la liste
    }

    // Afficher le formulaire d'édition
    @GetMapping("/edit/{id}")
    public Mono<String> formulaireModification(@PathVariable Long id, Model model) {
        return webClient.get()
                .uri("/api/matieres/{id}", id)
                .retrieve()
                .bodyToMono(Matiere.class)
                .doOnNext(matiere -> model.addAttribute("matiere", matiere))
                .thenReturn("matieres/edit");
    }

    // Modifier une matière
    @PostMapping("/edit/{id}")
    public Mono<String> modifierMatiere(@PathVariable Long id, @ModelAttribute Matiere matiereDTO) {
        return webClient.put()
                .uri("/api/matieres/{id}", id)
                .bodyValue(matiereDTO)
                .retrieve()
                .bodyToMono(Matiere.class)
                .thenReturn("redirect:/matieres");
    }

    // Supprimer une matière
    @PostMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable Long id) {
        return webClient.delete()
                .uri("/api/matieres/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn("redirect:/matieres");
    }

}

