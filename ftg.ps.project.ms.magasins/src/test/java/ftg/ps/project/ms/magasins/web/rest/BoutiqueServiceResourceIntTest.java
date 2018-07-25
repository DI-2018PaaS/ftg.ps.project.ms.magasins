package ftg.ps.project.ms.magasins.web.rest;

import ftg.ps.project.ms.magasins.PsMagasinApp;

import ftg.ps.project.ms.magasins.config.SecurityBeanOverrideConfiguration;

import ftg.ps.project.ms.magasins.domain.BoutiqueService;
import ftg.ps.project.ms.magasins.repository.BoutiqueServiceRepository;
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
 * Test class for the BoutiqueServiceResource REST controller.
 *
 * @see BoutiqueServiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PsMagasinApp.class})
public class BoutiqueServiceResourceIntTest {

    @Autowired
    private BoutiqueServiceRepository boutiqueServiceRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBoutiqueServiceMockMvc;

    private BoutiqueService boutiqueService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoutiqueServiceResource boutiqueServiceResource = new BoutiqueServiceResource(boutiqueServiceRepository);
        this.restBoutiqueServiceMockMvc = MockMvcBuilders.standaloneSetup(boutiqueServiceResource)
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
    public static BoutiqueService createEntity(EntityManager em) {
        BoutiqueService boutiqueService = new BoutiqueService();
        return boutiqueService;
    }

    @Before
    public void initTest() {
        boutiqueService = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoutiqueService() throws Exception {
        int databaseSizeBeforeCreate = boutiqueServiceRepository.findAll().size();

        // Create the BoutiqueService
        restBoutiqueServiceMockMvc.perform(post("/api/boutique-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutiqueService)))
            .andExpect(status().isCreated());

        // Validate the BoutiqueService in the database
        List<BoutiqueService> boutiqueServiceList = boutiqueServiceRepository.findAll();
        assertThat(boutiqueServiceList).hasSize(databaseSizeBeforeCreate + 1);
        BoutiqueService testBoutiqueService = boutiqueServiceList.get(boutiqueServiceList.size() - 1);
    }

    @Test
    @Transactional
    public void createBoutiqueServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boutiqueServiceRepository.findAll().size();

        // Create the BoutiqueService with an existing ID
        boutiqueService.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoutiqueServiceMockMvc.perform(post("/api/boutique-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutiqueService)))
            .andExpect(status().isBadRequest());

        // Validate the BoutiqueService in the database
        List<BoutiqueService> boutiqueServiceList = boutiqueServiceRepository.findAll();
        assertThat(boutiqueServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBoutiqueServices() throws Exception {
        // Initialize the database
        boutiqueServiceRepository.saveAndFlush(boutiqueService);

        // Get all the boutiqueServiceList
        restBoutiqueServiceMockMvc.perform(get("/api/boutique-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boutiqueService.getId().intValue())));
    }
    

    @Test
    @Transactional
    public void getBoutiqueService() throws Exception {
        // Initialize the database
        boutiqueServiceRepository.saveAndFlush(boutiqueService);

        // Get the boutiqueService
        restBoutiqueServiceMockMvc.perform(get("/api/boutique-services/{id}", boutiqueService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boutiqueService.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingBoutiqueService() throws Exception {
        // Get the boutiqueService
        restBoutiqueServiceMockMvc.perform(get("/api/boutique-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoutiqueService() throws Exception {
        // Initialize the database
        boutiqueServiceRepository.saveAndFlush(boutiqueService);

        int databaseSizeBeforeUpdate = boutiqueServiceRepository.findAll().size();

        // Update the boutiqueService
        BoutiqueService updatedBoutiqueService = boutiqueServiceRepository.findById(boutiqueService.getId()).get();
        // Disconnect from session so that the updates on updatedBoutiqueService are not directly saved in db
        em.detach(updatedBoutiqueService);

        restBoutiqueServiceMockMvc.perform(put("/api/boutique-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBoutiqueService)))
            .andExpect(status().isOk());

        // Validate the BoutiqueService in the database
        List<BoutiqueService> boutiqueServiceList = boutiqueServiceRepository.findAll();
        assertThat(boutiqueServiceList).hasSize(databaseSizeBeforeUpdate);
        BoutiqueService testBoutiqueService = boutiqueServiceList.get(boutiqueServiceList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingBoutiqueService() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueServiceRepository.findAll().size();

        // Create the BoutiqueService

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoutiqueServiceMockMvc.perform(put("/api/boutique-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutiqueService)))
            .andExpect(status().isBadRequest());

        // Validate the BoutiqueService in the database
        List<BoutiqueService> boutiqueServiceList = boutiqueServiceRepository.findAll();
        assertThat(boutiqueServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBoutiqueService() throws Exception {
        // Initialize the database
        boutiqueServiceRepository.saveAndFlush(boutiqueService);

        int databaseSizeBeforeDelete = boutiqueServiceRepository.findAll().size();

        // Get the boutiqueService
        restBoutiqueServiceMockMvc.perform(delete("/api/boutique-services/{id}", boutiqueService.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BoutiqueService> boutiqueServiceList = boutiqueServiceRepository.findAll();
        assertThat(boutiqueServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoutiqueService.class);
        BoutiqueService boutiqueService1 = new BoutiqueService();
        boutiqueService1.setId(1L);
        BoutiqueService boutiqueService2 = new BoutiqueService();
        boutiqueService2.setId(boutiqueService1.getId());
        assertThat(boutiqueService1).isEqualTo(boutiqueService2);
        boutiqueService2.setId(2L);
        assertThat(boutiqueService1).isNotEqualTo(boutiqueService2);
        boutiqueService1.setId(null);
        assertThat(boutiqueService1).isNotEqualTo(boutiqueService2);
    }
}
