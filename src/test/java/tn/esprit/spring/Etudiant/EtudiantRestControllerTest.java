package tn.esprit.spring.Etudiant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.RestControllers.EtudiantRestController;
import tn.esprit.spring.Services.Etudiant.IEtudiantService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EtudiantRestController.class)
public class EtudiantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEtudiantService etudiantService;

    @Autowired
    private ObjectMapper objectMapper;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("Ahmed");
        etudiant.setPrenomEt("Ali");
    }

    @Test
    void testAddOrUpdate() throws Exception {
        Mockito.when(etudiantService.addOrUpdate(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(post("/etudiant/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEt").value("Ahmed"));
    }

    @Test
    void testFindAll() throws Exception {
        List<Etudiant> list = Arrays.asList(etudiant, new Etudiant());
        Mockito.when(etudiantService.findAll()).thenReturn(list);

        mockMvc.perform(get("/etudiant/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testFindById() throws Exception {
        Mockito.when(etudiantService.findById(1L)).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/findById?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEt").value("Ahmed"));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(etudiantService).delete(any(Etudiant.class));

        mockMvc.perform(delete("/etudiant/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(etudiantService).deleteById(1L);

        mockMvc.perform(delete("/etudiant/deleteById?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testSelectJPQL() throws Exception {
        List<Etudiant> list = List.of(etudiant);
        Mockito.when(etudiantService.selectJPQL("Ahmed")).thenReturn(list);

        mockMvc.perform(get("/etudiant/selectJPQL?nom=Ahmed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomEt").value("Ahmed"));
    }
}
