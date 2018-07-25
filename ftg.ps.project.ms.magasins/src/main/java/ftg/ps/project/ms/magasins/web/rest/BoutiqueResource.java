package ftg.ps.project.ms.magasins.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.magasins.domain.Boutique;
import ftg.ps.project.ms.magasins.repository.BoutiqueRepository;
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
 * REST controller for managing Boutique.
 */
@RestController
@RequestMapping("/api")
public class BoutiqueResource {

    private final Logger log = LoggerFactory.getLogger(BoutiqueResource.class);

    private static final String ENTITY_NAME = "boutique";

    private final BoutiqueRepository boutiqueRepository;

    public BoutiqueResource(BoutiqueRepository boutiqueRepository) {
        this.boutiqueRepository = boutiqueRepository;
    }

    /**
     * POST  /boutiques : Create a new boutique.
     *
     * @param boutique the boutique to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boutique, or with status 400 (Bad Request) if the boutique has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boutiques")
    @Timed
    public ResponseEntity<Boutique> createBoutique(@RequestBody Boutique boutique) throws URISyntaxException {
        log.debug("REST request to save Boutique : {}", boutique);
        if (boutique.getId() != null) {
            throw new BadRequestAlertException("A new boutique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Boutique result = boutiqueRepository.save(boutique);
        return ResponseEntity.created(new URI("/api/boutiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boutiques : Updates an existing boutique.
     *
     * @param boutique the boutique to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boutique,
     * or with status 400 (Bad Request) if the boutique is not valid,
     * or with status 500 (Internal Server Error) if the boutique couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boutiques")
    @Timed
    public ResponseEntity<Boutique> updateBoutique(@RequestBody Boutique boutique) throws URISyntaxException {
        log.debug("REST request to update Boutique : {}", boutique);
        if (boutique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Boutique result = boutiqueRepository.save(boutique);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, boutique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boutiques : get all the boutiques.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of boutiques in body
     */
    @GetMapping("/boutiques")
    @Timed
    public List<Boutique> getAllBoutiques() {
        log.debug("REST request to get all Boutiques");
        return boutiqueRepository.findAll();
    }

    /**
     * GET  /boutiques/:id : get the "id" boutique.
     *
     * @param id the id of the boutique to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boutique, or with status 404 (Not Found)
     */
    @GetMapping("/boutiques/{id}")
    @Timed
    public ResponseEntity<Boutique> getBoutique(@PathVariable Long id) {
        log.debug("REST request to get Boutique : {}", id);
        Optional<Boutique> boutique = boutiqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(boutique);
    }

    /**
     * DELETE  /boutiques/:id : delete the "id" boutique.
     *
     * @param id the id of the boutique to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boutiques/{id}")
    @Timed
    public ResponseEntity<Void> deleteBoutique(@PathVariable Long id) {
        log.debug("REST request to delete Boutique : {}", id);

        boutiqueRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
