package ftg.ps.project.ms.magasins.repository;

import ftg.ps.project.ms.magasins.domain.BoutiqueProduit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BoutiqueProduit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoutiqueProduitRepository extends JpaRepository<BoutiqueProduit, Long> {

}
