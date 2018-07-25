package ftg.ps.project.ms.magasins.web.rest;

import ftg.ps.project.ms.magasins.PsMagasinApp;

import ftg.ps.project.ms.magasins.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.magasins.domain.BonLivraison;
import ftg.ps.project.ms.magasins.repository.BonLivraisonRepository;
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
 * Test class for the BonLivraisonResource REST controller.
 *
 * @see BonLivraisonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PsMagasinApp.class})
public class BonLivraisonResourceIntTest {

    private static final Long DEFAULT_NUMERO = 1L;
    private static final Long UPDATED_NUMERO = 2L;

    @Autowired
    private BonLivraisonRepository bonLivraisonRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBonLivraisonMockMvc;

    private BonLivraison bonLivraison;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BonLivraisonResource bonLivraisonResource = new BonLivraisonResource(bonLivraisonRepository);
        this.restBonLivraisonMockMvc = MockMvcBuilders.standaloneSetup(bonLivraisonResource)
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
    public static BonLivraison createEntity(EntityManager em) {
        BonLivraison bonLivraison = new BonLivraison()
            .numero(DEFAULT_NUMERO);
        return bonLivraison;
    }

    @Before
    public void initTest() {
        bonLivraison = createEntity(em);
    }

    @Test
    @Transactional
    public void createBonLivraison() throws Exception {
        int databaseSizeBeforeCreate = bonLivraisonRepository.findAll().size();

        // Create the BonLivraison
        restBonLivraisonMockMvc.perform(post("/api/bon-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonLivraison)))
            .andExpect(status().isCreated());

        // Validate the BonLivraison in the database
        List<BonLivraison> bonLivraisonList = bonLivraisonRepository.findAll();
        assertThat(bonLivraisonList).hasSize(databaseSizeBeforeCreate + 1);
        BonLivraison testBonLivraison = bonLivraisonList.get(bonLivraisonList.size() - 1);
        assertThat(testBonLivraison.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createBonLivraisonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bonLivraisonRepository.findAll().size();

        // Create the BonLivraison with an existing ID
        bonLivraison.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonLivraisonMockMvc.perform(post("/api/bon-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonLivraison)))
            .andExpect(status().isBadRequest());

        // Validate the BonLivraison in the database
        List<BonLivraison> bonLivraisonList = bonLivraisonRepository.findAll();
        assertThat(bonLivraisonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBonLivraisons() throws Exception {
        // Initialize the database
        bonLivraisonRepository.saveAndFlush(bonLivraison);

        // Get all the bonLivraisonList
        restBonLivraisonMockMvc.perform(get("/api/bon-livraisons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonLivraison.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.intValue())));
    }
    

    @Test
    @Transactional
    public void getBonLivraison() throws Exception {
        // Initialize the database
        bonLivraisonRepository.saveAndFlush(bonLivraison);

        // Get the bonLivraison
        restBonLivraisonMockMvc.perform(get("/api/bon-livraisons/{id}", bonLivraison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bonLivraison.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingBonLivraison() throws Exception {
        // Get the bonLivraison
        restBonLivraisonMockMvc.perform(get("/api/bon-livraisons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBonLivraison() throws Exception {
        // Initialize the database
        bonLivraisonRepository.saveAndFlush(bonLivraison);

        int databaseSizeBeforeUpdate = bonLivraisonRepository.findAll().size();

        // Update the bonLivraison
        BonLivraison updatedBonLivraison = bonLivraisonRepository.findById(bonLivraison.getId()).get();
        // Disconnect from session so that the updates on updatedBonLivraison are not directly saved in db
        em.detach(updatedBonLivraison);
        updatedBonLivraison
            .numero(UPDATED_NUMERO);

        restBonLivraisonMockMvc.perform(put("/api/bon-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBonLivraison)))
            .andExpect(status().isOk());

        // Validate the BonLivraison in the database
        List<BonLivraison> bonLivraisonList = bonLivraisonRepository.findAll();
        assertThat(bonLivraisonList).hasSize(databaseSizeBeforeUpdate);
        BonLivraison testBonLivraison = bonLivraisonList.get(bonLivraisonList.size() - 1);
        assertThat(testBonLivraison.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void updateNonExistingBonLivraison() throws Exception {
        int databaseSizeBeforeUpdate = bonLivraisonRepository.findAll().size();

        // Create the BonLivraison

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBonLivraisonMockMvc.perform(put("/api/bon-livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonLivraison)))
            .andExpect(status().isBadRequest());

        // Validate the BonLivraison in the database
        List<BonLivraison> bonLivraisonList = bonLivraisonRepository.findAll();
        assertThat(bonLivraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBonLivraison() throws Exception {
        // Initialize the database
        bonLivraisonRepository.saveAndFlush(bonLivraison);

        int databaseSizeBeforeDelete = bonLivraisonRepository.findAll().size();

        // Get the bonLivraison
        restBonLivraisonMockMvc.perform(delete("/api/bon-livraisons/{id}", bonLivraison.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BonLivraison> bonLivraisonList = bonLivraisonRepository.findAll();
        assertThat(bonLivraisonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BonLivraison.class);
        BonLivraison bonLivraison1 = new BonLivraison();
        bonLivraison1.setId(1L);
        BonLivraison bonLivraison2 = new BonLivraison();
        bonLivraison2.setId(bonLivraison1.getId());
        assertThat(bonLivraison1).isEqualTo(bonLivraison2);
        bonLivraison2.setId(2L);
        assertThat(bonLivraison1).isNotEqualTo(bonLivraison2);
        bonLivraison1.setId(null);
        assertThat(bonLivraison1).isNotEqualTo(bonLivraison2);
    }
}
