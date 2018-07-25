package ftg.ps.project.ms.magasins.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.magasins.domain.BonLivraison;
import ftg.ps.project.ms.magasins.repository.BonLivraisonRepository;
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
 * REST controller for managing BonLivraison.
 */
@RestController
@RequestMapping("/api")
public class BonLivraisonResource {

    private final Logger log = LoggerFactory.getLogger(BonLivraisonResource.class);

    private static final String ENTITY_NAME = "bonLivraison";

    private final BonLivraisonRepository bonLivraisonRepository;

    public BonLivraisonResource(BonLivraisonRepository bonLivraisonRepository) {
        this.bonLivraisonRepository = bonLivraisonRepository;
    }

    /**
     * POST  /bon-livraisons : Create a new bonLivraison.
     *
     * @param bonLivraison the bonLivraison to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bonLivraison, or with status 400 (Bad Request) if the bonLivraison has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bon-livraisons")
    @Timed
    public ResponseEntity<BonLivraison> createBonLivraison(@RequestBody BonLivraison bonLivraison) throws URISyntaxException {
        log.debug("REST request to save BonLivraison : {}", bonLivraison);
        if (bonLivraison.getId() != null) {
            throw new BadRequestAlertException("A new bonLivraison cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BonLivraison result = bonLivraisonRepository.save(bonLivraison);
        return ResponseEntity.created(new URI("/api/bon-livraisons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bon-livraisons : Updates an existing bonLivraison.
     *
     * @param bonLivraison the bonLivraison to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bonLivraison,
     * or with status 400 (Bad Request) if the bonLivraison is not valid,
     * or with status 500 (Internal Server Error) if the bonLivraison couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bon-livraisons")
    @Timed
    public ResponseEntity<BonLivraison> updateBonLivraison(@RequestBody BonLivraison bonLivraison) throws URISyntaxException {
        log.debug("REST request to update BonLivraison : {}", bonLivraison);
        if (bonLivraison.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BonLivraison result = bonLivraisonRepository.save(bonLivraison);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bonLivraison.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bon-livraisons : get all the bonLivraisons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bonLivraisons in body
     */
    @GetMapping("/bon-livraisons")
    @Timed
    public List<BonLivraison> getAllBonLivraisons() {
        log.debug("REST request to get all BonLivraisons");
        return bonLivraisonRepository.findAll();
    }

    /**
     * GET  /bon-livraisons/:id : get the "id" bonLivraison.
     *
     * @param id the id of the bonLivraison to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bonLivraison, or with status 404 (Not Found)
     */
    @GetMapping("/bon-livraisons/{id}")
    @Timed
    public ResponseEntity<BonLivraison> getBonLivraison(@PathVariable Long id) {
        log.debug("REST request to get BonLivraison : {}", id);
        Optional<BonLivraison> bonLivraison = bonLivraisonRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bonLivraison);
    }

    /**
     * DELETE  /bon-livraisons/:id : delete the "id" bonLivraison.
     *
     * @param id the id of the bonLivraison to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bon-livraisons/{id}")
    @Timed
    public ResponseEntity<Void> deleteBonLivraison(@PathVariable Long id) {
        log.debug("REST request to delete BonLivraison : {}", id);

        bonLivraisonRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
