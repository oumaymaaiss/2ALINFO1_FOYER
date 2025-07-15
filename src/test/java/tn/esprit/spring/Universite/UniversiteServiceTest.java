package tn.esprit.spring.Universite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Universite.UniversiteService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("ESPRIT");

        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite saved = universiteService.addOrUpdate(universite);
        assertNotNull(saved);
        assertEquals("ESPRIT", saved.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testFindAll() {
        Universite u1 = new Universite();
        u1.setIdUniversite(1L);
        u1.setNomUniversite("ESPRIT");

        Universite u2 = new Universite();
        u2.setIdUniversite(2L);
        u2.setNomUniversite("ENIS");

        when(universiteRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<Universite> list = universiteService.findAll();
        assertEquals(2, list.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Universite u = new Universite();
        u.setIdUniversite(1L);
        u.setNomUniversite("ISG");

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(u));

        Universite result = universiteService.findById(1L);
        assertNotNull(result);
        assertEquals("ISG", result.getNomUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        universiteService.deleteById(1L);
        verify(universiteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete() {
        Universite u = new Universite();
        u.setIdUniversite(1L);

        universiteService.delete(u);
        verify(universiteRepository, times(1)).delete(u);
    }

    @Test
    void testAjouterUniversiteEtSonFoyer() {
        Universite u = new Universite();
        u.setIdUniversite(1L);
        u.setNomUniversite("IHEC");

        when(universiteRepository.save(u)).thenReturn(u);

        Universite result = universiteService.ajouterUniversiteEtSonFoyer(u);
        assertNotNull(result);
        assertEquals("IHEC", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(u);
    }
}
