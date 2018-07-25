package ftg.ps.project.ms.magasins.repository;

import ftg.ps.project.ms.magasins.domain.MagasinProduit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MagasinProduit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MagasinProduitRepository extends JpaRepository<MagasinProduit, Long> {

}
