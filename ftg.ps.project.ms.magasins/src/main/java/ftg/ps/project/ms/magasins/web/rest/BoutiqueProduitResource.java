package ftg.ps.project.ms.magasins.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.magasins.domain.BoutiqueProduit;
import ftg.ps.project.ms.magasins.repository.BoutiqueProduitRepository;
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
 * REST controller for managing BoutiqueProduit.
 */
@RestController
@RequestMapping("/api")
public class BoutiqueProduitResource {

    private final Logger log = LoggerFactory.getLogger(BoutiqueProduitResource.class);

    private static final String ENTITY_NAME = "boutiqueProduit";

    private final BoutiqueProduitRepository boutiqueProduitRepository;

    public BoutiqueProduitResource(BoutiqueProduitRepository boutiqueProduitRepository) {
        this.boutiqueProduitRepository = boutiqueProduitRepository;
    }

    /**
     * POST  /boutique-produits : Create a new boutiqueProduit.
     *
     * @param boutiqueProduit the boutiqueProduit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boutiqueProduit, or with status 400 (Bad Request) if the boutiqueProduit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boutique-produits")
    @Timed
    public ResponseEntity<BoutiqueProduit> createBoutiqueProduit(@RequestBody BoutiqueProduit boutiqueProduit) throws URISyntaxException {
        log.debug("REST request to save BoutiqueProduit : {}", boutiqueProduit);
        if (boutiqueProduit.getId() != null) {
            throw new BadRequestAlertException("A new boutiqueProduit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoutiqueProduit result = boutiqueProduitRepository.save(boutiqueProduit);
        return ResponseEntity.created(new URI("/api/boutique-produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boutique-produits : Updates an existing boutiqueProduit.
     *
     * @param boutiqueProduit the boutiqueProduit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boutiqueProduit,
     * or with status 400 (Bad Request) if the boutiqueProduit is not valid,
     * or with status 500 (Internal Server Error) if the boutiqueProduit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boutique-produits")
    @Timed
    public ResponseEntity<BoutiqueProduit> updateBoutiqueProduit(@RequestBody BoutiqueProduit boutiqueProduit) throws URISyntaxException {
        log.debug("REST request to update BoutiqueProduit : {}", boutiqueProduit);
        if (boutiqueProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BoutiqueProduit result = boutiqueProduitRepository.save(boutiqueProduit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, boutiqueProduit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boutique-produits : get all the boutiqueProduits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of boutiqueProduits in body
     */
    @GetMapping("/boutique-produits")
    @Timed
    public List<BoutiqueProduit> getAllBoutiqueProduits() {
        log.debug("REST request to get all BoutiqueProduits");
        return boutiqueProduitRepository.findAll();
    }

    /**
     * GET  /boutique-produits/:id : get the "id" boutiqueProduit.
     *
     * @param id the id of the boutiqueProduit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boutiqueProduit, or with status 404 (Not Found)
     */
    @GetMapping("/boutique-produits/{id}")
    @Timed
    public ResponseEntity<BoutiqueProduit> getBoutiqueProduit(@PathVariable Long id) {
        log.debug("REST request to get BoutiqueProduit : {}", id);
        Optional<BoutiqueProduit> boutiqueProduit = boutiqueProduitRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(boutiqueProduit);
    }

    /**
     * DELETE  /boutique-produits/:id : delete the "id" boutiqueProduit.
     *
     * @param id the id of the boutiqueProduit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boutique-produits/{id}")
    @Timed
    public ResponseEntity<Void> deleteBoutiqueProduit(@PathVariable Long id) {
        log.debug("REST request to delete BoutiqueProduit : {}", id);

        boutiqueProduitRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
