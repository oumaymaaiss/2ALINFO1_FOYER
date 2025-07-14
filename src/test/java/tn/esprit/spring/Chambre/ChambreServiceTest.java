package tn.esprit.spring.Chambre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.Services.Bloc.BlocService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlocServiceTest {

    @InjectMocks
    private BlocService blocService;

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");
        List<Chambre> chambres = List.of(new Chambre(), new Chambre());
        bloc.setChambres(chambres);

        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Bloc savedBloc = blocService.addOrUpdate(bloc);

        verify(chambreRepository, times(2)).save(any(Chambre.class));
        assertEquals("BlocA", savedBloc.getNomBloc());
    }

    @Test
    void testFindAll() {
        when(blocRepository.findAll()).thenReturn(List.of(new Bloc(), new Bloc()));

        List<Bloc> blocs = blocService.findAll();

        assertEquals(2, blocs.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc found = blocService.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getIdBloc());
    }

    @Test
    void testDeleteById() {
        Bloc bloc = new Bloc();
        bloc.setChambres(List.of(new Chambre(), new Chambre()));

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        blocService.deleteById(1L);

        verify(chambreRepository, times(1)).deleteAll(bloc.getChambres());
        verify(blocRepository, times(1)).delete(bloc);
    }

    @Test
    void testAffecterChambresABloc() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocX");

        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();

        when(blocRepository.findByNomBloc("BlocX")).thenReturn(bloc);
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre1);
        when(chambreRepository.findByNumeroChambre(102L)).thenReturn(chambre2);

        Bloc result = blocService.affecterChambresABloc(List.of(101L, 102L), "BlocX");

        verify(chambreRepository, times(2)).save(any(Chambre.class));
        assertEquals(bloc, result);
    }

    @Test
    void testAffecterBlocAFoyer() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc1");

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("FoyerA");

        when(blocRepository.findByNomBloc("Bloc1")).thenReturn(bloc);
        when(foyerRepository.findByNomFoyer("FoyerA")).thenReturn(foyer);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.affecterBlocAFoyer("Bloc1", "FoyerA");

        assertEquals(foyer, result.getFoyer());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testAjouterBlocEtAffecterAFoyer() {
        Bloc bloc = new Bloc();
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F1");

        when(foyerRepository.findByNomFoyer("F1")).thenReturn(foyer);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc saved = blocService.ajouterBlocEtAffecterAFoyer(bloc, "F1");

        assertEquals(foyer, saved.getFoyer());
        verify(blocRepository).save(bloc);
    }
}
