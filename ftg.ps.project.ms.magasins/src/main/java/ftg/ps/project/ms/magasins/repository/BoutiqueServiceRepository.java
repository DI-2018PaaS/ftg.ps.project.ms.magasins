package ftg.ps.project.ms.magasins.repository;

import ftg.ps.project.ms.magasins.domain.BoutiqueService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BoutiqueService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoutiqueServiceRepository extends JpaRepository<BoutiqueService, Long> {

}
