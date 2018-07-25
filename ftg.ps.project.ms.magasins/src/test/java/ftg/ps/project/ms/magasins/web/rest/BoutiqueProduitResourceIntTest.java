package ftg.ps.project.ms.magasins.web.rest;

import ftg.ps.project.ms.magasins.PsMagasinApp;

import ftg.ps.project.ms.magasins.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.magasins.domain.BoutiqueProduit;
import ftg.ps.project.ms.magasins.repository.BoutiqueProduitRepository;
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
 * Test class for the BoutiqueProduitResource REST controller.
 *
 * @see BoutiqueProduitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PsMagasinApp.class})
public class BoutiqueProduitResourceIntTest {

    @Autowired
    private BoutiqueProduitRepository boutiqueProduitRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBoutiqueProduitMockMvc;

    private BoutiqueProduit boutiqueProduit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoutiqueProduitResource boutiqueProduitResource = new BoutiqueProduitResource(boutiqueProduitRepository);
        this.restBoutiqueProduitMockMvc = MockMvcBuilders.standaloneSetup(boutiqueProduitResource)
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
    public static BoutiqueProduit createEntity(EntityManager em) {
        BoutiqueProduit boutiqueProduit = new BoutiqueProduit();
        return boutiqueProduit;
    }

    @Before
    public void initTest() {
        boutiqueProduit = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoutiqueProduit() throws Exception {
        int databaseSizeBeforeCreate = boutiqueProduitRepository.findAll().size();

        // Create the BoutiqueProduit
        restBoutiqueProduitMockMvc.perform(post("/api/boutique-produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutiqueProduit)))
            .andExpect(status().isCreated());

        // Validate the BoutiqueProduit in the database
        List<BoutiqueProduit> boutiqueProduitList = boutiqueProduitRepository.findAll();
        assertThat(boutiqueProduitList).hasSize(databaseSizeBeforeCreate + 1);
        BoutiqueProduit testBoutiqueProduit = boutiqueProduitList.get(boutiqueProduitList.size() - 1);
    }

    @Test
    @Transactional
    public void createBoutiqueProduitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boutiqueProduitRepository.findAll().size();

        // Create the BoutiqueProduit with an existing ID
        boutiqueProduit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoutiqueProduitMockMvc.perform(post("/api/boutique-produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutiqueProduit)))
            .andExpect(status().isBadRequest());

        // Validate the BoutiqueProduit in the database
        List<BoutiqueProduit> boutiqueProduitList = boutiqueProduitRepository.findAll();
        assertThat(boutiqueProduitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBoutiqueProduits() throws Exception {
        // Initialize the database
        boutiqueProduitRepository.saveAndFlush(boutiqueProduit);

        // Get all the boutiqueProduitList
        restBoutiqueProduitMockMvc.perform(get("/api/boutique-produits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boutiqueProduit.getId().intValue())));
    }
    

    @Test
    @Transactional
    public void getBoutiqueProduit() throws Exception {
        // Initialize the database
        boutiqueProduitRepository.saveAndFlush(boutiqueProduit);

        // Get the boutiqueProduit
        restBoutiqueProduitMockMvc.perform(get("/api/boutique-produits/{id}", boutiqueProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boutiqueProduit.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingBoutiqueProduit() throws Exception {
        // Get the boutiqueProduit
        restBoutiqueProduitMockMvc.perform(get("/api/boutique-produits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoutiqueProduit() throws Exception {
        // Initialize the database
        boutiqueProduitRepository.saveAndFlush(boutiqueProduit);

        int databaseSizeBeforeUpdate = boutiqueProduitRepository.findAll().size();

        // Update the boutiqueProduit
        BoutiqueProduit updatedBoutiqueProduit = boutiqueProduitRepository.findById(boutiqueProduit.getId()).get();
        // Disconnect from session so that the updates on updatedBoutiqueProduit are not directly saved in db
        em.detach(updatedBoutiqueProduit);

        restBoutiqueProduitMockMvc.perform(put("/api/boutique-produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBoutiqueProduit)))
            .andExpect(status().isOk());

        // Validate the BoutiqueProduit in the database
        List<BoutiqueProduit> boutiqueProduitList = boutiqueProduitRepository.findAll();
        assertThat(boutiqueProduitList).hasSize(databaseSizeBeforeUpdate);
        BoutiqueProduit testBoutiqueProduit = boutiqueProduitList.get(boutiqueProduitList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingBoutiqueProduit() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueProduitRepository.findAll().size();

        // Create the BoutiqueProduit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoutiqueProduitMockMvc.perform(put("/api/boutique-produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutiqueProduit)))
            .andExpect(status().isBadRequest());

        // Validate the BoutiqueProduit in the database
        List<BoutiqueProduit> boutiqueProduitList = boutiqueProduitRepository.findAll();
        assertThat(boutiqueProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBoutiqueProduit() throws Exception {
        // Initialize the database
        boutiqueProduitRepository.saveAndFlush(boutiqueProduit);

        int databaseSizeBeforeDelete = boutiqueProduitRepository.findAll().size();

        // Get the boutiqueProduit
        restBoutiqueProduitMockMvc.perform(delete("/api/boutique-produits/{id}", boutiqueProduit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BoutiqueProduit> boutiqueProduitList = boutiqueProduitRepository.findAll();
        assertThat(boutiqueProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoutiqueProduit.class);
        BoutiqueProduit boutiqueProduit1 = new BoutiqueProduit();
        boutiqueProduit1.setId(1L);
        BoutiqueProduit boutiqueProduit2 = new BoutiqueProduit();
        boutiqueProduit2.setId(boutiqueProduit1.getId());
        assertThat(boutiqueProduit1).isEqualTo(boutiqueProduit2);
        boutiqueProduit2.setId(2L);
        assertThat(boutiqueProduit1).isNotEqualTo(boutiqueProduit2);
        boutiqueProduit1.setId(null);
        assertThat(boutiqueProduit1).isNotEqualTo(boutiqueProduit2);
    }
}
