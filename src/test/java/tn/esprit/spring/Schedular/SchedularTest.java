package tn.esprit.spring.Schedular;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tn.esprit.spring.Services.Chambre.IChambreService;
import tn.esprit.spring.Services.Reservation.IReservationService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
public class SchedularTest {

    @Autowired
    private Schedular schedular;

    @MockBean
    private IChambreService chambreService;

    @MockBean
    private IReservationService reservationService;

    @Test
    public void testService1_shouldCallListeChambresParBloc() {
        schedular.service1();
        verify(chambreService, times(1)).listeChambresParBloc();
    }

//    @Test
//    public void testService2_shouldCallPourcentageChambreParTypeChambre() {
//        schedular.service2();
//        verify(chambreService, times(1)).pourcentageChambreParTypeChambre();
//    }
//
//    @Test
//    public void testService3_shouldCallNbPlacesDisponibleParChambreAnneeEnCours() {
//        schedular.service3();
//        verify(chambreService, times(1)).nbPlacesDisponibleParChambreAnneeEnCours();
//    }
//
//    @Test
//    public void testService4_shouldCallAnnulerReservations() {
//        schedular.service4();
//        verify(reservationService, times(1)).annulerReservations();
//    }
}
