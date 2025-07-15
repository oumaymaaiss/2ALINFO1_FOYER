package tn.esprit.spring.Schedular;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class _4SE1ClassTest {

    @Autowired
    private _4SE1Class scheduler;

    @Test
    public void testFixedDelayMethod() {
        // Appel direct à la méthode schedulée pour vérifier le log
        scheduler.fixedDelayMethod();
        // Vous devez voir dans la console : "Hello fixedDelay ..."
    }

    @Test
    public void testFixedRateMethod() {
        // Appel direct à la méthode schedulée pour vérifier le log
        scheduler.fixedRateMethod();
        // Vous devez voir dans la console : "Hello fixedRate ..."
    }
}
