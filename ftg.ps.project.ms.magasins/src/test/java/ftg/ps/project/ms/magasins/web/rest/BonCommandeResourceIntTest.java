package ftg.ps.project.ms.magasins.web.rest;

import ftg.ps.project.ms.magasins.PsMagasinApp;

import ftg.ps.project.ms.magasins.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.magasins.domain.BonCommande;
import ftg.ps.project.ms.magasins.repository.BonCommandeRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static ftg.ps.project.ms.magasins.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BonCommandeResource REST controller.
 *
 * @see BonCommandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PsMagasinApp.class})
public class BonCommandeResourceIntTest {

    private static final Long DEFAULT_NUMERO = 1L;
    private static final Long UPDATED_NUMERO = 2L;

    private static final LocalDate DEFAULT_DATE_EMISSION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EMISSION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_REGLEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REGLEMENT = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ACHETEUR_ID = 1L;
    private static final Long UPDATED_ACHETEUR_ID = 2L;

    @Autowired
    private BonCommandeRepository bonCommandeRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBonCommandeMockMvc;

    private BonCommande bonCommande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BonCommandeResource bonCommandeResource = new BonCommandeResource(bonCommandeRepository);
        this.restBonCommandeMockMvc = MockMvcBuilders.standaloneSetup(bonCommandeResource)
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
    public static BonCommande createEntity(EntityManager em) {
        BonCommande bonCommande = new BonCommande()
            .numero(DEFAULT_NUMERO)
            .dateEmission(DEFAULT_DATE_EMISSION)
            .dateReglement(DEFAULT_DATE_REGLEMENT)
            .acheteurId(DEFAULT_ACHETEUR_ID);
        return bonCommande;
    }

    @Before
    public void initTest() {
        bonCommande = createEntity(em);
    }

    @Test
    @Transactional
    public void createBonCommande() throws Exception {
        int databaseSizeBeforeCreate = bonCommandeRepository.findAll().size();

        // Create the BonCommande
        restBonCommandeMockMvc.perform(post("/api/bon-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonCommande)))
            .andExpect(status().isCreated());

        // Validate the BonCommande in the database
        List<BonCommande> bonCommandeList = bonCommandeRepository.findAll();
        assertThat(bonCommandeList).hasSize(databaseSizeBeforeCreate + 1);
        BonCommande testBonCommande = bonCommandeList.get(bonCommandeList.size() - 1);
        assertThat(testBonCommande.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testBonCommande.getDateEmission()).isEqualTo(DEFAULT_DATE_EMISSION);
        assertThat(testBonCommande.getDateReglement()).isEqualTo(DEFAULT_DATE_REGLEMENT);
        assertThat(testBonCommande.getAcheteurId()).isEqualTo(DEFAULT_ACHETEUR_ID);
    }

    @Test
    @Transactional
    public void createBonCommandeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bonCommandeRepository.findAll().size();

        // Create the BonCommande with an existing ID
        bonCommande.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonCommandeMockMvc.perform(post("/api/bon-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonCommande)))
            .andExpect(status().isBadRequest());

        // Validate the BonCommande in the database
        List<BonCommande> bonCommandeList = bonCommandeRepository.findAll();
        assertThat(bonCommandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBonCommandes() throws Exception {
        // Initialize the database
        bonCommandeRepository.saveAndFlush(bonCommande);

        // Get all the bonCommandeList
        restBonCommandeMockMvc.perform(get("/api/bon-commandes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.intValue())))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].dateReglement").value(hasItem(DEFAULT_DATE_REGLEMENT.toString())))
            .andExpect(jsonPath("$.[*].acheteurId").value(hasItem(DEFAULT_ACHETEUR_ID.intValue())));
    }
    

    @Test
    @Transactional
    public void getBonCommande() throws Exception {
        // Initialize the database
        bonCommandeRepository.saveAndFlush(bonCommande);

        // Get the bonCommande
        restBonCommandeMockMvc.perform(get("/api/bon-commandes/{id}", bonCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bonCommande.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.intValue()))
            .andExpect(jsonPath("$.dateEmission").value(DEFAULT_DATE_EMISSION.toString()))
            .andExpect(jsonPath("$.dateReglement").value(DEFAULT_DATE_REGLEMENT.toString()))
            .andExpect(jsonPath("$.acheteurId").value(DEFAULT_ACHETEUR_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingBonCommande() throws Exception {
        // Get the bonCommande
        restBonCommandeMockMvc.perform(get("/api/bon-commandes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBonCommande() throws Exception {
        // Initialize the database
        bonCommandeRepository.saveAndFlush(bonCommande);

        int databaseSizeBeforeUpdate = bonCommandeRepository.findAll().size();

        // Update the bonCommande
        BonCommande updatedBonCommande = bonCommandeRepository.findById(bonCommande.getId()).get();
        // Disconnect from session so that the updates on updatedBonCommande are not directly saved in db
        em.detach(updatedBonCommande);
        updatedBonCommande
            .numero(UPDATED_NUMERO)
            .dateEmission(UPDATED_DATE_EMISSION)
            .dateReglement(UPDATED_DATE_REGLEMENT)
            .acheteurId(UPDATED_ACHETEUR_ID);

        restBonCommandeMockMvc.perform(put("/api/bon-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBonCommande)))
            .andExpect(status().isOk());

        // Validate the BonCommande in the database
        List<BonCommande> bonCommandeList = bonCommandeRepository.findAll();
        assertThat(bonCommandeList).hasSize(databaseSizeBeforeUpdate);
        BonCommande testBonCommande = bonCommandeList.get(bonCommandeList.size() - 1);
        assertThat(testBonCommande.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testBonCommande.getDateEmission()).isEqualTo(UPDATED_DATE_EMISSION);
        assertThat(testBonCommande.getDateReglement()).isEqualTo(UPDATED_DATE_REGLEMENT);
        assertThat(testBonCommande.getAcheteurId()).isEqualTo(UPDATED_ACHETEUR_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingBonCommande() throws Exception {
        int databaseSizeBeforeUpdate = bonCommandeRepository.findAll().size();

        // Create the BonCommande

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBonCommandeMockMvc.perform(put("/api/bon-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonCommande)))
            .andExpect(status().isBadRequest());

        // Validate the BonCommande in the database
        List<BonCommande> bonCommandeList = bonCommandeRepository.findAll();
        assertThat(bonCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBonCommande() throws Exception {
        // Initialize the database
        bonCommandeRepository.saveAndFlush(bonCommande);

        int databaseSizeBeforeDelete = bonCommandeRepository.findAll().size();

        // Get the bonCommande
        restBonCommandeMockMvc.perform(delete("/api/bon-commandes/{id}", bonCommande.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BonCommande> bonCommandeList = bonCommandeRepository.findAll();
        assertThat(bonCommandeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BonCommande.class);
        BonCommande bonCommande1 = new BonCommande();
        bonCommande1.setId(1L);
        BonCommande bonCommande2 = new BonCommande();
        bonCommande2.setId(bonCommande1.getId());
        assertThat(bonCommande1).isEqualTo(bonCommande2);
        bonCommande2.setId(2L);
        assertThat(bonCommande1).isNotEqualTo(bonCommande2);
        bonCommande1.setId(null);
        assertThat(bonCommande1).isNotEqualTo(bonCommande2);
    }
}
