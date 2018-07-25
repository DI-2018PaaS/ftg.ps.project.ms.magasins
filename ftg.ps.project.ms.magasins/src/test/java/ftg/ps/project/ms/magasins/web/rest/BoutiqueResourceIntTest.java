package ftg.ps.project.ms.magasins.web.rest;

import ftg.ps.project.ms.magasins.PsMagasinApp;

import ftg.ps.project.ms.magasins.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.magasins.domain.Boutique;
import ftg.ps.project.ms.magasins.repository.BoutiqueRepository;
import ftg.ps.project.ms.magasins.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static ftg.ps.project.ms.magasins.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BoutiqueResource REST controller.
 *
 * @see BoutiqueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PsMagasinApp.class})
public class BoutiqueResourceIntTest {

    private static final Long DEFAULT_N_ID_PROPRIETAIRE = 1L;
    private static final Long UPDATED_N_ID_PROPRIETAIRE = 2L;

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BoutiqueRepository boutiqueRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBoutiqueMockMvc;

    private Boutique boutique;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoutiqueResource boutiqueResource = new BoutiqueResource(boutiqueRepository);
        this.restBoutiqueMockMvc = MockMvcBuilders.standaloneSetup(boutiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boutique createEntity(EntityManager em) {
        Boutique boutique = new Boutique()
            .nIdProprietaire(DEFAULT_N_ID_PROPRIETAIRE)
            .ref(DEFAULT_REF)
            .adresse(DEFAULT_ADRESSE)
            .description(DEFAULT_DESCRIPTION);
        return boutique;
    }

    @Before
    public void initTest() {
        boutique = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoutique() throws Exception {
        int databaseSizeBeforeCreate = boutiqueRepository.findAll().size();

        // Create the Boutique
        restBoutiqueMockMvc.perform(post("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isCreated());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeCreate + 1);
        Boutique testBoutique = boutiqueList.get(boutiqueList.size() - 1);
        assertThat(testBoutique.getnIdProprietaire()).isEqualTo(DEFAULT_N_ID_PROPRIETAIRE);
        assertThat(testBoutique.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testBoutique.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testBoutique.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createBoutiqueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boutiqueRepository.findAll().size();

        // Create the Boutique with an existing ID
        boutique.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoutiqueMockMvc.perform(post("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isBadRequest());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBoutiques() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        // Get all the boutiqueList
        restBoutiqueMockMvc.perform(get("/api/boutiques?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boutique.getId().intValue())))
            .andExpect(jsonPath("$.[*].nIdProprietaire").value(hasItem(DEFAULT_N_ID_PROPRIETAIRE.intValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    

    @Test
    @Transactional
    public void getBoutique() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        // Get the boutique
        restBoutiqueMockMvc.perform(get("/api/boutiques/{id}", boutique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boutique.getId().intValue()))
            .andExpect(jsonPath("$.nIdProprietaire").value(DEFAULT_N_ID_PROPRIETAIRE.intValue()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingBoutique() throws Exception {
        // Get the boutique
        restBoutiqueMockMvc.perform(get("/api/boutiques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoutique() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();

        // Update the boutique
        Boutique updatedBoutique = boutiqueRepository.findById(boutique.getId()).get();
        // Disconnect from session so that the updates on updatedBoutique are not directly saved in db
        em.detach(updatedBoutique);
        updatedBoutique
            .nIdProprietaire(UPDATED_N_ID_PROPRIETAIRE)
            .ref(UPDATED_REF)
            .adresse(UPDATED_ADRESSE)
            .description(UPDATED_DESCRIPTION);

        restBoutiqueMockMvc.perform(put("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBoutique)))
            .andExpect(status().isOk());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
        Boutique testBoutique = boutiqueList.get(boutiqueList.size() - 1);
        assertThat(testBoutique.getnIdProprietaire()).isEqualTo(UPDATED_N_ID_PROPRIETAIRE);
        assertThat(testBoutique.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testBoutique.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testBoutique.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingBoutique() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();

        // Create the Boutique

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoutiqueMockMvc.perform(put("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isBadRequest());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBoutique() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        int databaseSizeBeforeDelete = boutiqueRepository.findAll().size();

        // Get the boutique
        restBoutiqueMockMvc.perform(delete("/api/boutiques/{id}", boutique.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Boutique.class);
        Boutique boutique1 = new Boutique();
        boutique1.setId(1L);
        Boutique boutique2 = new Boutique();
        boutique2.setId(boutique1.getId());
        assertThat(boutique1).isEqualTo(boutique2);
        boutique2.setId(2L);
        assertThat(boutique1).isNotEqualTo(boutique2);
        boutique1.setId(null);
        assertThat(boutique1).isNotEqualTo(boutique2);
    }
}
