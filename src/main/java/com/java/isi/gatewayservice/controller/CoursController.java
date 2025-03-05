package com.java.isi.gatewayservice.controller;

import com.java.isi.gatewayservice.dto.Classe;
import com.java.isi.gatewayservice.dto.Cours;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/cours")
public class CoursController {

    private final WebClient webClient;

    public CoursController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    @GetMapping
    public Mono<String> listeDesCours(Model model) {
        return webClient.get()
                .uri("/api/cours")
                .retrieve()
                .bodyToFlux(Cours.class)
                .collectList()
                .defaultIfEmpty(List.of())
                .doOnNext(cours -> model.addAttribute("cours", cours))
                .thenReturn("cours/list"); // Renvoie le nom de la vue
    }


    // Afficher le formulaire d'ajout d'un cours
    @GetMapping("/ajout")
    public Mono<String> formulaireAjout(Model model) {
        Mono<List<Classe>> classesMono = webClient.get()
                .uri("/api/classes") // Récupère les classes du microservice classes
                .retrieve()
                .bodyToFlux(Classe.class)
                .collectList();

        return classesMono.doOnNext(classes -> {
            model.addAttribute("cours", new Cours());
            model.addAttribute("classes", classes); // Ajoute les classes au modèle
        }).thenReturn("cours/add");
    }

    // Ajouter un cours
    @PostMapping("/ajout")
    public Mono<String> ajouterCours(@ModelAttribute Cours cours) {
        return webClient.post()
                .uri("/api/cours")
                .bodyValue(cours)
                .retrieve()
                .bodyToMono(Cours.class)
                .thenReturn("redirect:/cours"); // Redirection vers la liste
    }

    // Afficher le formulaire d'édition
    @GetMapping("/edit/{id}")
    public Mono<String> formulaireModification(@PathVariable Long id, Model model) {
        return webClient.get()
                .uri("/api/classes") // Récupère les classes du microservice classes
                .retrieve()
                .bodyToFlux(Classe.class)
                .collectList()
                .zipWith(
                        webClient.get()
                                .uri("/api/cours/{id}", id)
                                .retrieve()
                                .bodyToMono(Cours.class)
                )
                .doOnNext(tuple -> {
                    model.addAttribute("classes", tuple.getT1()); // Liste des classes
                    model.addAttribute("cour", tuple.getT2()); // Cours à modifier
                })
                .thenReturn("cours/edit");
    }

    // Modifier un cours
    @PostMapping("/edit/{id}")
    public Mono<String> modifierCours(@PathVariable Long id, @ModelAttribute Cours cours) {
        return webClient.post()
                .uri("/api/cours/edit/{id}", id)
                .bodyValue(cours)
                .retrieve()
                .bodyToMono(Cours.class)
                .thenReturn("redirect:/cours");
    }

    // Supprimer un cours
    @PostMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable Long id) {
        return webClient.post()
                .uri("/api/cours/delete/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn("redirect:/cours");
    }


    @GetMapping("/classe/{classeId}")
    public Mono<String> getCoursByClasse(@PathVariable Long classeId, Model model) {
        return webClient.get()
                .uri("/api/cours/classe/{classeId}", classeId)
                .retrieve()
                .bodyToFlux(Cours.class)
                .collectList()
                .defaultIfEmpty(List.of())
                .doOnNext(cours -> model.addAttribute("cours", cours))
                .thenReturn("cours/list");
    }



}
