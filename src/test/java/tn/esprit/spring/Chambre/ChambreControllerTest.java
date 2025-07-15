package tn.esprit.spring.Chambre;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.RestControllers.ChambreRestController;
import tn.esprit.spring.Services.Chambre.IChambreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChambreRestController.class)
class ChambreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChambreService chambreService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddOrUpdate() throws Exception {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);

        when(chambreService.addOrUpdate(any(Chambre.class))).thenReturn(chambre);

        mockMvc.perform(post("/chambre/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chambre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChambre").value(1));
    }

    @Test
    void testFindAll() throws Exception {
        List<Chambre> chambres = List.of(new Chambre(), new Chambre());
        when(chambreService.findAll()).thenReturn(chambres);

        mockMvc.perform(get("/chambre/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFindById() throws Exception {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        when(chambreService.findById(1L)).thenReturn(chambre);

        mockMvc.perform(get("/chambre/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChambre").value(1));
    }

    @Test
    void testDelete() throws Exception {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);

        doNothing().when(chambreService).delete(any(Chambre.class));

        mockMvc.perform(delete("/chambre/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chambre)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(chambreService).deleteById(1L);

        mockMvc.perform(delete("/chambre/deleteById")
                        .param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetChambresParNomBloc() throws Exception {
        when(chambreService.getChambresParNomBloc("BlocA"))
                .thenReturn(List.of(new Chambre(), new Chambre()));

        mockMvc.perform(get("/chambre/getChambresParNomBloc")
                        .param("nomBloc", "BlocA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testNbChambreParTypeEtBloc() throws Exception {
        when(chambreService.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, 1L)).thenReturn(5L);

        mockMvc.perform(get("/chambre/nbChambreParTypeEtBloc")
                        .param("type", "SIMPLE")
                        .param("idBloc", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testGetChambresNonReserveParNomFoyerEtTypeChambre() throws Exception {
        when(chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("FoyerX", TypeChambre.DOUBLE))
                .thenReturn(List.of(new Chambre(), new Chambre()));

        mockMvc.perform(get("/chambre/getChambresNonReserveParNomFoyerEtTypeChambre")
                        .param("nomFoyer", "FoyerX")
                        .param("type", "DOUBLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}

