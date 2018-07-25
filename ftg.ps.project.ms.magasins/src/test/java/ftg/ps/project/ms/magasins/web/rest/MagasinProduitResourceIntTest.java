package ftg.ps.project.ms.magasins.web.rest;

import ftg.ps.project.ms.magasins.PsMagasinApp;

import ftg.ps.project.ms.magasins.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.magasins.domain.MagasinProduit;
import ftg.ps.project.ms.magasins.repository.MagasinProduitRepository;
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
 * Test class for the MagasinProduitResource REST controller.
 *
 * @see MagasinProduitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PsMagasinApp.class})
public class MagasinProduitResourceIntTest {

    @Autowired
    private MagasinProduitRepository magasinProduitRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMagasinProduitMockMvc;

    private MagasinProduit magasinProduit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MagasinProduitResource magasinProduitResource = new MagasinProduitResource(magasinProduitRepository);
        this.restMagasinProduitMockMvc = MockMvcBuilders.standaloneSetup(magasinProduitResource)
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
    public static MagasinProduit createEntity(EntityManager em) {
        MagasinProduit magasinProduit = new MagasinProduit();
        return magasinProduit;
    }

    @Before
    public void initTest() {
        magasinProduit = createEntity(em);
    }

    @Test
    @Transactional
    public void createMagasinProduit() throws Exception {
        int databaseSizeBeforeCreate = magasinProduitRepository.findAll().size();

        // Create the MagasinProduit
        restMagasinProduitMockMvc.perform(post("/api/magasin-produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magasinProduit)))
            .andExpect(status().isCreated());

        // Validate the MagasinProduit in the database
        List<MagasinProduit> magasinProduitList = magasinProduitRepository.findAll();
        assertThat(magasinProduitList).hasSize(databaseSizeBeforeCreate + 1);
        MagasinProduit testMagasinProduit = magasinProduitList.get(magasinProduitList.size() - 1);
    }

    @Test
    @Transactional
    public void createMagasinProduitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = magasinProduitRepository.findAll().size();

        // Create the MagasinProduit with an existing ID
        magasinProduit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMagasinProduitMockMvc.perform(post("/api/magasin-produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magasinProduit)))
            .andExpect(status().isBadRequest());

        // Validate the MagasinProduit in the database
        List<MagasinProduit> magasinProduitList = magasinProduitRepository.findAll();
        assertThat(magasinProduitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMagasinProduits() throws Exception {
        // Initialize the database
        magasinProduitRepository.saveAndFlush(magasinProduit);

        // Get all the magasinProduitList
        restMagasinProduitMockMvc.perform(get("/api/magasin-produits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magasinProduit.getId().intValue())));
    }
    

    @Test
    @Transactional
    public void getMagasinProduit() throws Exception {
        // Initialize the database
        magasinProduitRepository.saveAndFlush(magasinProduit);

        // Get the magasinProduit
        restMagasinProduitMockMvc.perform(get("/api/magasin-produits/{id}", magasinProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(magasinProduit.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingMagasinProduit() throws Exception {
        // Get the magasinProduit
        restMagasinProduitMockMvc.perform(get("/api/magasin-produits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMagasinProduit() throws Exception {
        // Initialize the database
        magasinProduitRepository.saveAndFlush(magasinProduit);

        int databaseSizeBeforeUpdate = magasinProduitRepository.findAll().size();

        // Update the magasinProduit
        MagasinProduit updatedMagasinProduit = magasinProduitRepository.findById(magasinProduit.getId()).get();
        // Disconnect from session so that the updates on updatedMagasinProduit are not directly saved in db
        em.detach(updatedMagasinProduit);

        restMagasinProduitMockMvc.perform(put("/api/magasin-produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMagasinProduit)))
            .andExpect(status().isOk());

        // Validate the MagasinProduit in the database
        List<MagasinProduit> magasinProduitList = magasinProduitRepository.findAll();
        assertThat(magasinProduitList).hasSize(databaseSizeBeforeUpdate);
        MagasinProduit testMagasinProduit = magasinProduitList.get(magasinProduitList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingMagasinProduit() throws Exception {
        int databaseSizeBeforeUpdate = magasinProduitRepository.findAll().size();

        // Create the MagasinProduit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMagasinProduitMockMvc.perform(put("/api/magasin-produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magasinProduit)))
            .andExpect(status().isBadRequest());

        // Validate the MagasinProduit in the database
        List<MagasinProduit> magasinProduitList = magasinProduitRepository.findAll();
        assertThat(magasinProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMagasinProduit() throws Exception {
        // Initialize the database
        magasinProduitRepository.saveAndFlush(magasinProduit);

        int databaseSizeBeforeDelete = magasinProduitRepository.findAll().size();

        // Get the magasinProduit
        restMagasinProduitMockMvc.perform(delete("/api/magasin-produits/{id}", magasinProduit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MagasinProduit> magasinProduitList = magasinProduitRepository.findAll();
        assertThat(magasinProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MagasinProduit.class);
        MagasinProduit magasinProduit1 = new MagasinProduit();
        magasinProduit1.setId(1L);
        MagasinProduit magasinProduit2 = new MagasinProduit();
        magasinProduit2.setId(magasinProduit1.getId());
        assertThat(magasinProduit1).isEqualTo(magasinProduit2);
        magasinProduit2.setId(2L);
        assertThat(magasinProduit1).isNotEqualTo(magasinProduit2);
        magasinProduit1.setId(null);
        assertThat(magasinProduit1).isNotEqualTo(magasinProduit2);
    }
}
