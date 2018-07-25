package ftg.ps.project.ms.magasins.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftg.ps.project.ms.magasins.domain.MagasinProduit;
import ftg.ps.project.ms.magasins.repository.MagasinProduitRepository;
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
 * REST controller for managing MagasinProduit.
 */
@RestController
@RequestMapping("/api")
public class MagasinProduitResource {

    private final Logger log = LoggerFactory.getLogger(MagasinProduitResource.class);

    private static final String ENTITY_NAME = "magasinProduit";

    private final MagasinProduitRepository magasinProduitRepository;

    public MagasinProduitResource(MagasinProduitRepository magasinProduitRepository) {
        this.magasinProduitRepository = magasinProduitRepository;
    }

    /**
     * POST  /magasin-produits : Create a new magasinProduit.
     *
     * @param magasinProduit the magasinProduit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new magasinProduit, or with status 400 (Bad Request) if the magasinProduit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/magasin-produits")
    @Timed
    public ResponseEntity<MagasinProduit> createMagasinProduit(@RequestBody MagasinProduit magasinProduit) throws URISyntaxException {
        log.debug("REST request to save MagasinProduit : {}", magasinProduit);
        if (magasinProduit.getId() != null) {
            throw new BadRequestAlertException("A new magasinProduit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MagasinProduit result = magasinProduitRepository.save(magasinProduit);
        return ResponseEntity.created(new URI("/api/magasin-produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /magasin-produits : Updates an existing magasinProduit.
     *
     * @param magasinProduit the magasinProduit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated magasinProduit,
     * or with status 400 (Bad Request) if the magasinProduit is not valid,
     * or with status 500 (Internal Server Error) if the magasinProduit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/magasin-produits")
    @Timed
    public ResponseEntity<MagasinProduit> updateMagasinProduit(@RequestBody MagasinProduit magasinProduit) throws URISyntaxException {
        log.debug("REST request to update MagasinProduit : {}", magasinProduit);
        if (magasinProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MagasinProduit result = magasinProduitRepository.save(magasinProduit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, magasinProduit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /magasin-produits : get all the magasinProduits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of magasinProduits in body
     */
    @GetMapping("/magasin-produits")
    @Timed
    public List<MagasinProduit> getAllMagasinProduits() {
        log.debug("REST request to get all MagasinProduits");
        return magasinProduitRepository.findAll();
    }

    /**
     * GET  /magasin-produits/:id : get the "id" magasinProduit.
     *
     * @param id the id of the magasinProduit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the magasinProduit, or with status 404 (Not Found)
     */
    @GetMapping("/magasin-produits/{id}")
    @Timed
    public ResponseEntity<MagasinProduit> getMagasinProduit(@PathVariable Long id) {
        log.debug("REST request to get MagasinProduit : {}", id);
        Optional<MagasinProduit> magasinProduit = magasinProduitRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(magasinProduit);
    }

    /**
     * DELETE  /magasin-produits/:id : delete the "id" magasinProduit.
     *
     * @param id the id of the magasinProduit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/magasin-produits/{id}")
    @Timed
    public ResponseEntity<Void> deleteMagasinProduit(@PathVariable Long id) {
        log.debug("REST request to delete MagasinProduit : {}", id);

        magasinProduitRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
