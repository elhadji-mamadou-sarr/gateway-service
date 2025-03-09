package com.java.isi.gatewayservice.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.java.isi.gatewayservice.dto.Classe;
import com.java.isi.gatewayservice.dto.Etudiant;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/etudiants")
public class EtudiantController {

    private final WebClient webClient;

    public EtudiantController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    // Afficher la liste des classes
    @GetMapping
    public Mono<String> listeDesClasses(Model model) {
        return webClient.get()
                .uri("/api/etudiants")
                .retrieve()
                .bodyToFlux(Etudiant.class)
                .collectList()
                .defaultIfEmpty(List.of())
                .doOnNext(etudiants -> model.addAttribute("etudiants", etudiants))
                .thenReturn("etudiants/list"); // Renvoie le nom de la vue
    }

    // Afficher le formulaire d'ajout d'une classe
    @GetMapping("/ajout")
    public String formulaireAjout(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "etudiants/add";
    }

    // Ajouter une classe
    @PostMapping("/ajout")
    public Mono<String> ajouterClasse(@ModelAttribute Classe classe) {
        return webClient.post()
                .uri("/api/etudiants")
                .bodyValue(classe)
                .retrieve()
                .bodyToMono(Etudiant.class)
                .thenReturn("redirect:/etudiants"); // Redirection vers la liste
    }

    // Afficher le formulaire d'édition
    @GetMapping("/edit/{id}")
    public Mono<String> formulaireModification(@PathVariable Long id, Model model) {
        return webClient.get()
                .uri("/api/etudiants/{id}", id)
                .retrieve()
                .bodyToMono(Etudiant.class)
                .doOnNext(etudiant -> model.addAttribute("etudiant", etudiant))
                .thenReturn("etudiants/edit");
    }

    // Modifier une classe
    @PostMapping("/edit/{id}")
    public Mono<String> modifierClasse(@PathVariable Long id, @ModelAttribute Classe classe) {
        return webClient.put()
                .uri("/api/etudiants/{id}", id)
                .bodyValue(classe)
                .retrieve()
                .bodyToMono(Etudiant.class)
                .thenReturn("redirect:/etudiants");
    }

    // Supprimer une classe
    @PostMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable Long id) {
        return webClient.delete()
                .uri("/api/classes/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn("redirect:/classes");
    }
}