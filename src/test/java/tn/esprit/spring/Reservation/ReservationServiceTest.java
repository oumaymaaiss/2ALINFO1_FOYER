package tn.esprit.spring.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Reservation.ReservationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("R1");
        reservation.setAnneeUniversitaire(LocalDate.now());
        reservation.setEstValide(true);
        reservation.setEtudiants(new ArrayList<>());

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation saved = reservationService.addOrUpdate(reservation);
        assertNotNull(saved);
        assertEquals("R1", saved.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testFindAll() {
        Reservation r1 = new Reservation();
        r1.setIdReservation("R1");
        r1.setAnneeUniversitaire(LocalDate.now());
        r1.setEstValide(true);
        r1.setEtudiants(new ArrayList<>());

        Reservation r2 = new Reservation();
        r2.setIdReservation("R2");
        r2.setAnneeUniversitaire(LocalDate.now());
        r2.setEstValide(true);
        r2.setEtudiants(new ArrayList<>());

        List<Reservation> list = Arrays.asList(r1, r2);

        when(reservationRepository.findAll()).thenReturn(list);

        List<Reservation> result = reservationService.findAll();
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("R1");
        reservation.setEstValide(true);
        reservation.setEtudiants(new ArrayList<>());

        when(reservationRepository.findById("R1")).thenReturn(Optional.of(reservation));

        Reservation found = reservationService.findById("R1");
        assertNotNull(found);
        assertEquals("R1", found.getIdReservation());
        verify(reservationRepository, times(1)).findById("R1");
    }

    @Test
    void testDeleteById() {
        reservationService.deleteById("R1");
        verify(reservationRepository, times(1)).deleteById("R1");
    }

    @Test
    void testDelete() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("R1");

        reservationService.delete(reservation);
        verify(reservationRepository, times(1)).delete(reservation);
    }
}
