package tn.esprit.spring.Etudiant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudiantServiceTest {

    @InjectMocks
    private EtudiantService etudiantService;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Etudiant e = new Etudiant();
        e.setNomEt("Ali");

        when(etudiantRepository.save(e)).thenReturn(e);

        Etudiant result = etudiantService.addOrUpdate(e);
        assertNotNull(result);
        assertEquals("Ali", result.getNomEt());
        verify(etudiantRepository).save(e);
    }

    @Test
    void testFindAll() {
        List<Etudiant> list = List.of(new Etudiant(), new Etudiant());
        when(etudiantRepository.findAll()).thenReturn(list);

        List<Etudiant> result = etudiantService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        Etudiant e = new Etudiant();
        e.setIdEtudiant(1L);

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(e));

        Etudiant result = etudiantService.findById(1L);
        assertEquals(1L, result.getIdEtudiant());
    }

    @Test
    void testDeleteById() {
        doNothing().when(etudiantRepository).deleteById(1L);
        etudiantService.deleteById(1L);
        verify(etudiantRepository).deleteById(1L);
    }

    @Test
    void testDelete() {
        Etudiant e = new Etudiant();
        doNothing().when(etudiantRepository).delete(e);
        etudiantService.delete(e);
        verify(etudiantRepository).delete(e);
    }

    @Test
    void testSelectJPQL() {
        List<Etudiant> list = List.of(new Etudiant());
        when(etudiantRepository.selectJPQL("Ali")).thenReturn(list);

        List<Etudiant> result = etudiantService.selectJPQL("Ali");
        assertEquals(1, result.size());
    }

    @Test
    void testAffecterReservationAEtudiant() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("RES123");

        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("Ali");
        etudiant.setPrenomEt("Ben Salah");
        etudiant.setReservations(new ArrayList<>());

        when(reservationRepository.findById("RES123")).thenReturn(Optional.of(reservation));
        when(etudiantRepository.getByNomEtAndPrenomEt("Ali", "Ben Salah")).thenReturn(etudiant);
        when(etudiantRepository.save(any())).thenReturn(etudiant);

        etudiantService.affecterReservationAEtudiant("RES123", "Ali", "Ben Salah");

        assertTrue(etudiant.getReservations().contains(reservation));
        verify(etudiantRepository).save(etudiant);
    }

    @Test
    void testDesaffecterReservationAEtudiant() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("RES456");

        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("Sami");
        etudiant.setPrenomEt("Bouaziz");
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        etudiant.setReservations(reservations);

        when(reservationRepository.findById("RES456")).thenReturn(Optional.of(reservation));
        when(etudiantRepository.getByNomEtAndPrenomEt("Sami", "Bouaziz")).thenReturn(etudiant);
        when(etudiantRepository.save(any())).thenReturn(etudiant);

        etudiantService.desaffecterReservationAEtudiant("RES456", "Sami", "Bouaziz");

        assertFalse(etudiant.getReservations().contains(reservation));
        verify(etudiantRepository).save(etudiant);
    }
}

