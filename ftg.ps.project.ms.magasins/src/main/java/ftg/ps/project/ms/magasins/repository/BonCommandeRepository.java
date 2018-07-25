package ftg.ps.project.ms.magasins.repository;

import ftg.ps.project.ms.magasins.domain.BonCommande;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BonCommande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BonCommandeRepository extends JpaRepository<BonCommande, Long> {

}
