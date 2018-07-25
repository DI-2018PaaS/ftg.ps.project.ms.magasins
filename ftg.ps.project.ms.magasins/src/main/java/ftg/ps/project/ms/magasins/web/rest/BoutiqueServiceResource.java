package ftg.ps.project.ms.magasins.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.magasins.domain.BoutiqueService;
import ftg.ps.project.ms.magasins.repository.BoutiqueServiceRepository;
import ftg.ps.project.ms.magasins.web.rest.errors.BadRequestAlertException;
import ftg.ps.project.ms.magasins.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BoutiqueService.
 */
@RestController
@RequestMapping("/api")
public class BoutiqueServiceResource {

    private final Logger log = LoggerFactory.getLogger(BoutiqueServiceResource.class);

    private static final String ENTITY_NAME = "boutiqueService";

    private final BoutiqueServiceRepository boutiqueServiceRepository;

    public BoutiqueServiceResource(BoutiqueServiceRepository boutiqueServiceRepository) {
        this.boutiqueServiceRepository = boutiqueServiceRepository;
    }

    /**
     * POST  /boutique-services : Create a new boutiqueService.
     *
     * @param boutiqueService the boutiqueService to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boutiqueService, or with status 400 (Bad Request) if the boutiqueService has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boutique-services")
    @Timed
    public ResponseEntity<BoutiqueService> createBoutiqueService(@RequestBody BoutiqueService boutiqueService) throws URISyntaxException {
        log.debug("REST request to save BoutiqueService : {}", boutiqueService);
        if (boutiqueService.getId() != null) {
            throw new BadRequestAlertException("A new boutiqueService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoutiqueService result = boutiqueServiceRepository.save(boutiqueService);
        return ResponseEntity.created(new URI("/api/boutique-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boutique-services : Updates an existing boutiqueService.
     *
     * @param boutiqueService the boutiqueService to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boutiqueService,
     * or with status 400 (Bad Request) if the boutiqueService is not valid,
     * or with status 500 (Internal Server Error) if the boutiqueService couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boutique-services")
    @Timed
    public ResponseEntity<BoutiqueService> updateBoutiqueService(@RequestBody BoutiqueService boutiqueService) throws URISyntaxException {
        log.debug("REST request to update BoutiqueService : {}", boutiqueService);
        if (boutiqueService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BoutiqueService result = boutiqueServiceRepository.save(boutiqueService);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, boutiqueService.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boutique-services : get all the boutiqueServices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of boutiqueServices in body
     */
    @GetMapping("/boutique-services")
    @Timed
    public List<BoutiqueService> getAllBoutiqueServices() {
        log.debug("REST request to get all BoutiqueServices");
        return boutiqueServiceRepository.findAll();
    }

    /**
     * GET  /boutique-services/:id : get the "id" boutiqueService.
     *
     * @param id the id of the boutiqueService to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boutiqueService, or with status 404 (Not Found)
     */
    @GetMapping("/boutique-services/{id}")
    @Timed
    public ResponseEntity<BoutiqueService> getBoutiqueService(@PathVariable Long id) {
        log.debug("REST request to get BoutiqueService : {}", id);
        Optional<BoutiqueService> boutiqueService = boutiqueServiceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(boutiqueService);
    }

    /**
     * DELETE  /boutique-services/:id : delete the "id" boutiqueService.
     *
     * @param id the id of the boutiqueService to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boutique-services/{id}")
    @Timed
    public ResponseEntity<Void> deleteBoutiqueService(@PathVariable Long id) {
        log.debug("REST request to delete BoutiqueService : {}", id);

        boutiqueServiceRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
