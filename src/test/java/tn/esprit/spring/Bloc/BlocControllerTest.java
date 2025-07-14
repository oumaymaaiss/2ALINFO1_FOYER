package tn.esprit.spring.Bloc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.RestControllers.BlocRestController;
import tn.esprit.spring.Services.Bloc.IBlocService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocRestController.class)
class BlocRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlocService blocService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddOrUpdate() throws Exception {
        Bloc bloc = new Bloc(1L, "Bloc A", 10, null, null);
        Mockito.when(blocService.addOrUpdate(any(Bloc.class))).thenReturn(bloc);

        mockMvc.perform(post("/bloc/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBloc").value(1))
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testFindAll() throws Exception {
        List<Bloc> blocs = Arrays.asList(
                new Bloc(1L, "Bloc A", 10, null, null),
                new Bloc(2L, "Bloc B", 15, null, null)
        );
        Mockito.when(blocService.findAll()).thenReturn(blocs);

        mockMvc.perform(get("/bloc/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFindById() throws Exception {
        Bloc bloc = new Bloc(1L, "Bloc A", 10, null, null);
        Mockito.when(blocService.findById(1L)).thenReturn(bloc);

        mockMvc.perform(get("/bloc/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/bloc/deleteById").param("id", "1"))
                .andExpect(status().isOk());
        Mockito.verify(blocService).deleteById(1L);
    }

    @Test
    void testAffecterChambresABloc() throws Exception {
        List<Long> chambres = List.of(101L, 102L);
        Bloc bloc = new Bloc(1L, "Bloc A", 10, null, null);

        Mockito.when(blocService.affecterChambresABloc(eq(chambres), eq("Bloc A"))).thenReturn(bloc);

        mockMvc.perform(put("/bloc/affecterChambresABloc")
                        .param("nomBloc", "Bloc A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chambres)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testAffecterBlocAFoyer() throws Exception {
        Bloc bloc = new Bloc(1L, "Bloc A", 10, null, null);

        Mockito.when(blocService.affecterBlocAFoyer("Bloc A", "Foyer X")).thenReturn(bloc);

        mockMvc.perform(put("/bloc/affecterBlocAFoyer")
                        .param("nomBloc", "Bloc A")
                        .param("nomFoyer", "Foyer X"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testAjouterBlocEtSesChambres() throws Exception {
        Bloc bloc = new Bloc(1L, "Bloc A", 10, null, null);

        Mockito.when(blocService.ajouterBlocEtSesChambres(any(Bloc.class))).thenReturn(bloc);

        mockMvc.perform(post("/bloc/ajouterBlocEtSesChambres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testAjouterBlocEtAffecterAFoyer() throws Exception {
        Bloc bloc = new Bloc(1L, "Bloc A", 10, null, null);

        Mockito.when(blocService.ajouterBlocEtAffecterAFoyer(any(Bloc.class), eq("Foyer X"))).thenReturn(bloc);

        mockMvc.perform(post("/bloc/ajouterBlocEtAffecterAFoyer/Foyer X")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }
}
