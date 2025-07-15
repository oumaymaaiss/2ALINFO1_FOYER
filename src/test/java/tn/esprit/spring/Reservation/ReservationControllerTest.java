package tn.esprit.spring.Reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.RestControllers.ReservationRestController;
import tn.esprit.spring.Services.Reservation.IReservationService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationRestController.class)
class ReservationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("res1");
        reservation.setEstValide(true);
    }

    @Test
    void testAddOrUpdate() throws Exception {
        when(reservationService.addOrUpdate(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/reservation/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("res1"));
    }

    @Test
    void testFindAll() throws Exception {
        when(reservationService.findAll()).thenReturn(List.of(reservation));

        mockMvc.perform(get("/reservation/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value("res1"));
    }

    @Test
    void testFindById() throws Exception {
        when(reservationService.findById("res1")).thenReturn(reservation);

        mockMvc.perform(get("/reservation/findById")
                        .param("id", "res1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("res1"));
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(reservationService).deleteById("res1");

        mockMvc.perform(delete("/reservation/deleteById/res1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(reservationService).delete(any(Reservation.class));

        mockMvc.perform(delete("/reservation/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk());
    }

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant() throws Exception {
        when(reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(1L, 12345678L))
                .thenReturn(reservation);

        mockMvc.perform(post("/reservation/ajouterReservationEtAssignerAChambreEtAEtudiant")
                        .param("numChambre", "1")
                        .param("cin", "12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("res1"));
    }

    @Test
    void testGetReservationParAnneeUniversitaire() throws Exception {
        LocalDate debut = LocalDate.of(2024, 9, 1);
        LocalDate fin = LocalDate.of(2025, 6, 30);
        when(reservationService.getReservationParAnneeUniversitaire(debut, fin)).thenReturn(42L);

        mockMvc.perform(get("/reservation/getReservationParAnneeUniversitaire")
                        .param("debutAnnee", debut.toString())
                        .param("finAnnee", fin.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("42"));
    }

    @Test
    void testAnnulerReservation() throws Exception {
        when(reservationService.annulerReservation(12345678L)).thenReturn("Reservation annulée");

        mockMvc.perform(delete("/reservation/annulerReservation")
                        .param("cinEtudiant", "12345678"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation annulée"));
    }
}
