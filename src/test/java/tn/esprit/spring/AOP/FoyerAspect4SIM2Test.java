package tn.esprit.spring.AOP;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.Services.Foyer.IFoyerService;

@SpringBootTest
@Slf4j
public class FoyerAspect4SIM2Test {

    @Autowired
    IFoyerService foyerService;

    @Test
    public void testAspectForAjouterMethod() {
        // Appelle une méthode commencer par 'ajouter' pour tester le beforeAdvice2
        foyerService.ajouterFoyerEtAffecterAUniversite(null, 0L);

        // Attendez-vous à voir dans la console :
        // "ranni d5alt lil méthode ajouterFoyerEtAffecterAUniversite"
        // "Ranni méthode ajouter"
        // "ranni 5rajt mil méthode ajouterFoyerEtAffecterAUniversite"
        // "Method execution time: ... milliseconds."
    }

    @Test
    public void testAspectForGeneralMethod() {
        // Appelle une méthode qui ne commence pas par 'ajouter'
        foyerService.findAll();

        // Attendez-vous à voir dans la console :
        // "ranni d5alt lil méthode findAll"
        // "ranni 5rajt mil méthode findAll"
        // "Method execution time: ... milliseconds."
    }
}
