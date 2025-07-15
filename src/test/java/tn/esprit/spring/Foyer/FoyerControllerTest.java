package tn.esprit.spring.Foyer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.RestControllers.FoyerRestController;
import tn.esprit.spring.Services.Foyer.IFoyerService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoyerRestController.class)
public class FoyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFoyerService foyerService;

    private Foyer foyer;
    private Universite universite;

    @BeforeEach
    void setUp() {
        foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer A");

        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("ENIS");
    }

    @Test
    void testAddOrUpdate() throws Exception {
        when(foyerService.addOrUpdate(any(Foyer.class))).thenReturn(foyer);

        mockMvc.perform(post("/foyer/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(foyer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(1L));

        verify(foyerService).addOrUpdate(any(Foyer.class));
    }

    @Test
    void testFindAll() throws Exception {
        when(foyerService.findAll()).thenReturn(List.of(foyer));

        mockMvc.perform(get("/foyer/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomFoyer").value("Foyer A"));

        verify(foyerService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        when(foyerService.findById(1L)).thenReturn(foyer);

        mockMvc.perform(get("/foyer/findById?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomFoyer").value("Foyer A"));

        verify(foyerService).findById(1L);
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(foyerService).delete(any(Foyer.class));

        mockMvc.perform(delete("/foyer/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(foyer)))
                .andExpect(status().isOk());

        verify(foyerService).delete(any(Foyer.class));
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(foyerService).deleteById(1L);

        mockMvc.perform(delete("/foyer/deleteById?id=1"))
                .andExpect(status().isOk());

        verify(foyerService).deleteById(1L);
    }

    @Test
    void testAffecterFoyerAUniversite_ByName() throws Exception {
        when(foyerService.affecterFoyerAUniversite(1L, "ENIS")).thenReturn(universite);

        mockMvc.perform(put("/foyer/affecterFoyerAUniversite?idFoyer=1&nomUniversite=ENIS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("ENIS"));

        verify(foyerService).affecterFoyerAUniversite(1L, "ENIS");
    }

    @Test
    void testDesaffecterFoyerAUniversite() throws Exception {
        when(foyerService.desaffecterFoyerAUniversite(1L)).thenReturn(universite);

        mockMvc.perform(put("/foyer/desaffecterFoyerAUniversite?idUniversite=1"))
                .andExpect(status().isOk());

        verify(foyerService).desaffecterFoyerAUniversite(1L);
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() throws Exception {
        when(foyerService.ajouterFoyerEtAffecterAUniversite(any(Foyer.class), eq(1L))).thenReturn(foyer);

        mockMvc.perform(post("/foyer/ajouterFoyerEtAffecterAUniversite?idUniversite=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(foyer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomFoyer").value("Foyer A"));

        verify(foyerService).ajouterFoyerEtAffecterAUniversite(any(Foyer.class), eq(1L));
    }

    @Test
    void testAffecterFoyerAUniversite_ByIds() throws Exception {
        when(foyerService.affecterFoyerAUniversite(1L, 2L)).thenReturn(universite);

        mockMvc.perform(put("/foyer/affecterFoyerAUniversite/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUniversite").value(1L));

        verify(foyerService).affecterFoyerAUniversite(1L, 2L);
    }
}

