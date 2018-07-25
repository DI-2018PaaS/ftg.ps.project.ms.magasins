package ftg.ps.project.ms.magasins.web.rest;

import ftg.ps.project.ms.magasins.PsMagasinApp;

import ftg.ps.project.ms.magasins.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.magasins.domain.Magasin;
import ftg.ps.project.ms.magasins.repository.MagasinRepository;
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
 * Test class for the MagasinResource REST controller.
 *
 * @see MagasinResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PsMagasinApp.class})
public class MagasinResourceIntTest {

    private static final Long DEFAULT_N_ID_PROPRIETAIRE = 1L;
    private static final Long UPDATED_N_ID_PROPRIETAIRE = 2L;

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private MagasinRepository magasinRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMagasinMockMvc;

    private Magasin magasin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MagasinResource magasinResource = new MagasinResource(magasinRepository);
        this.restMagasinMockMvc = MockMvcBuilders.standaloneSetup(magasinResource)
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
    public static Magasin createEntity(EntityManager em) {
        Magasin magasin = new Magasin()
            .nIdProprietaire(DEFAULT_N_ID_PROPRIETAIRE)
            .ref(DEFAULT_REF)
            .adresse(DEFAULT_ADRESSE)
            .description(DEFAULT_DESCRIPTION);
        return magasin;
    }

    @Before
    public void initTest() {
        magasin = createEntity(em);
    }

    @Test
    @Transactional
    public void createMagasin() throws Exception {
        int databaseSizeBeforeCreate = magasinRepository.findAll().size();

        // Create the Magasin
        restMagasinMockMvc.perform(post("/api/magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magasin)))
            .andExpect(status().isCreated());

        // Validate the Magasin in the database
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeCreate + 1);
        Magasin testMagasin = magasinList.get(magasinList.size() - 1);
        assertThat(testMagasin.getnIdProprietaire()).isEqualTo(DEFAULT_N_ID_PROPRIETAIRE);
        assertThat(testMagasin.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testMagasin.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testMagasin.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createMagasinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = magasinRepository.findAll().size();

        // Create the Magasin with an existing ID
        magasin.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMagasinMockMvc.perform(post("/api/magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magasin)))
            .andExpect(status().isBadRequest());

        // Validate the Magasin in the database
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMagasins() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);

        // Get all the magasinList
        restMagasinMockMvc.perform(get("/api/magasins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magasin.getId().intValue())))
            .andExpect(jsonPath("$.[*].nIdProprietaire").value(hasItem(DEFAULT_N_ID_PROPRIETAIRE.intValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    

    @Test
    @Transactional
    public void getMagasin() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);

        // Get the magasin
        restMagasinMockMvc.perform(get("/api/magasins/{id}", magasin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(magasin.getId().intValue()))
            .andExpect(jsonPath("$.nIdProprietaire").value(DEFAULT_N_ID_PROPRIETAIRE.intValue()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingMagasin() throws Exception {
        // Get the magasin
        restMagasinMockMvc.perform(get("/api/magasins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMagasin() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);

        int databaseSizeBeforeUpdate = magasinRepository.findAll().size();

        // Update the magasin
        Magasin updatedMagasin = magasinRepository.findById(magasin.getId()).get();
        // Disconnect from session so that the updates on updatedMagasin are not directly saved in db
        em.detach(updatedMagasin);
        updatedMagasin
            .nIdProprietaire(UPDATED_N_ID_PROPRIETAIRE)
            .ref(UPDATED_REF)
            .adresse(UPDATED_ADRESSE)
            .description(UPDATED_DESCRIPTION);

        restMagasinMockMvc.perform(put("/api/magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMagasin)))
            .andExpect(status().isOk());

        // Validate the Magasin in the database
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeUpdate);
        Magasin testMagasin = magasinList.get(magasinList.size() - 1);
        assertThat(testMagasin.getnIdProprietaire()).isEqualTo(UPDATED_N_ID_PROPRIETAIRE);
        assertThat(testMagasin.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testMagasin.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testMagasin.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingMagasin() throws Exception {
        int databaseSizeBeforeUpdate = magasinRepository.findAll().size();

        // Create the Magasin

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMagasinMockMvc.perform(put("/api/magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magasin)))
            .andExpect(status().isBadRequest());

        // Validate the Magasin in the database
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMagasin() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);

        int databaseSizeBeforeDelete = magasinRepository.findAll().size();

        // Get the magasin
        restMagasinMockMvc.perform(delete("/api/magasins/{id}", magasin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Magasin.class);
        Magasin magasin1 = new Magasin();
        magasin1.setId(1L);
        Magasin magasin2 = new Magasin();
        magasin2.setId(magasin1.getId());
        assertThat(magasin1).isEqualTo(magasin2);
        magasin2.setId(2L);
        assertThat(magasin1).isNotEqualTo(magasin2);
        magasin1.setId(null);
        assertThat(magasin1).isNotEqualTo(magasin2);
    }
}
