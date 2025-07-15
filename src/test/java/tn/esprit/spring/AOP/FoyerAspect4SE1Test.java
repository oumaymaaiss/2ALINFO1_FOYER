package tn.esprit.spring.AOP;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.Services.Foyer.IFoyerService;

@SpringBootTest
@Slf4j
public class FoyerAspect4SE1Test {

    @Autowired
    IFoyerService foyerService;

    @Test
    public void testAspectExecution() {
        // Appel d'une méthode du service pour déclencher les aspects
        foyerService.findAll(); // Devrait logguer via @Before, @After et @Around

        // Vous pouvez vérifier manuellement dans la console :
        // - "Hello from findAll"
        // - "Out of method findAll"
        // - "Method execution time: ..."
    }
}
