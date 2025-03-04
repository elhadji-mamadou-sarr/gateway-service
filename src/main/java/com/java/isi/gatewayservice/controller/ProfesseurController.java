package com.java.isi.gatewayservice.controller;

import com.java.isi.gatewayservice.dto.Classe;
import com.java.isi.gatewayservice.dto.Matiere;
import com.java.isi.gatewayservice.dto.Professeur;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/professeurs")
public class ProfesseurController {

    private final WebClient webClient;

    public ProfesseurController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    // Afficher la liste des professeurs
    @GetMapping
    public Mono<String> listeDesProfesseurs(Model model) {
        return webClient.get()
                .uri("/api/professeurs")
                .retrieve()
                .bodyToFlux(Professeur.class)
                .collectList()
                .defaultIfEmpty(List.of())
                .zipWith(
                        webClient.get()
                                .uri("/api/matieres")
                                .retrieve()
                                .bodyToFlux(Matiere.class)
                                .collectList()
                                .defaultIfEmpty(List.of())
                )
                .doOnNext(tuple -> {
                    List<Professeur> professeurs = tuple.getT1();
                    List<Matiere> matieres = tuple.getT2();
                    model.addAttribute("professeurs", professeurs);
                    model.addAttribute("matieres", matieres);
                })
                .thenReturn("professeurs/list"); // Nom de la vue
    }


    // Afficher le formulaire d'ajout d'un professeur
    @GetMapping("/ajout")
    public String formulaireAjout(Model model) {
        model.addAttribute("professeur", new Professeur());
        return "professeurs/add";
    }

    // Ajouter un professeur
    @PostMapping("/ajout")
    public Mono<String> ajouterProfesseur(@ModelAttribute Professeur professeur) {
        return webClient.post()
                .uri("/api/professeurs")
                .bodyValue(professeur)
                .retrieve()
                .bodyToMono(Professeur.class)
                .thenReturn("redirect:/professeurs"); // Redirection vers la liste
    }

    // Afficher le formulaire d'édition
    @GetMapping("/edit/{id}")
    public Mono<String> formulaireModification(@PathVariable Long id, Model model) {
        return webClient.get()
                .uri("/api/matieres")
                .retrieve()
                .bodyToFlux(Matiere.class)
                .collectList()
                .zipWith(
                        webClient.get()
                                .uri("/api/professeurs/{id}", id)
                                .retrieve()
                                .bodyToMono(Professeur.class)
                )
                .zipWith(
                        webClient.get()
                                .uri("/api/classes")
                                .retrieve()
                                .bodyToFlux(Classe.class)
                                .collectList()
                )
                .doOnNext(tuple -> {
                    model.addAttribute("matieres", tuple.getT1().getT1());
                    model.addAttribute("professeur", tuple.getT1().getT2());
                    model.addAttribute("classes", tuple.getT2());
                })
                .thenReturn("professeurs/edit");
    }


    // Modifier un professeur
    @PostMapping("/edit/{id}")
    public Mono<String> modifierProfesseur(@PathVariable Long id, @ModelAttribute Professeur professeurDTO) {
        return webClient.put()
                .uri("/api/professeurs/{id}", id)
                .bodyValue(professeurDTO)
                .retrieve()
                .bodyToMono(Professeur.class)
                .thenReturn("redirect:/professeurs");
    }

    // Supprimer un professeur
    @PostMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable Long id) {
        return webClient.delete()
                .uri("/api/professeurs/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn("redirect:/professeurs");
    }
}