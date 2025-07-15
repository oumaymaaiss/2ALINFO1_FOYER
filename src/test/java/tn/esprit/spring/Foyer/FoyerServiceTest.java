package tn.esprit.spring.Foyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.*;
import tn.esprit.spring.Services.Foyer.FoyerService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FoyerServiceTest {

    @InjectMocks
    private FoyerService foyerService;

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private BlocRepository blocRepository;

    private Foyer foyer;
    private Universite universite;
    private Bloc bloc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("Bloc A");

        foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer Test");
        foyer.setBlocs(new ArrayList<>(List.of(bloc)));

        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("ENIT");
    }

    @Test
    void testAddOrUpdate() {
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer saved = foyerService.addOrUpdate(foyer);

        assertThat(saved).isEqualTo(foyer);
        verify(foyerRepository).save(foyer);
    }

    @Test
    void testFindAll() {
        when(foyerRepository.findAll()).thenReturn(List.of(foyer));

        List<Foyer> result = foyerService.findAll();

        assertThat(result).hasSize(1).contains(foyer);
        verify(foyerRepository).findAll();
    }

    @Test
    void testFindById() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.findById(1L);

        assertThat(result).isEqualTo(foyer);
        verify(foyerRepository).findById(1L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(foyerRepository).deleteById(1L);

        foyerService.deleteById(1L);

        verify(foyerRepository).deleteById(1L);
    }

    @Test
    void testDelete() {
        doNothing().when(foyerRepository).delete(foyer);

        foyerService.delete(foyer);

        verify(foyerRepository).delete(foyer);
    }

    @Test
    void testAffecterFoyerAUniversite_byName() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findByNomUniversite("ENIT")).thenReturn(universite);
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(1L, "ENIT");

        assertThat(result.getFoyer()).isEqualTo(foyer);
        verify(universiteRepository).save(universite);
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        when(foyerRepository.save(foyer)).thenReturn(foyer);
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 1L);

        assertThat(result).isEqualTo(foyer);
        verify(blocRepository, times(1)).save(any(Bloc.class));
        verify(universiteRepository).save(any(Universite.class));
    }

    @Test
    void testAjoutFoyerEtBlocs() {
        when(foyerRepository.save(foyer)).thenReturn(foyer);
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Foyer result = foyerService.ajoutFoyerEtBlocs(foyer);

        assertThat(result).isEqualTo(foyer);
        verify(blocRepository, times(1)).save(any(Bloc.class));
    }

    @Test
    void testAffecterFoyerAUniversite_byIds() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(1L, 1L);

        assertThat(result.getFoyer()).isEqualTo(foyer);
        verify(universiteRepository).save(universite);
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        universite.setFoyer(foyer);
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.desaffecterFoyerAUniversite(1L);

        assertThat(result.getFoyer()).isNull();
        verify(universiteRepository).save(universite);
    }
}
