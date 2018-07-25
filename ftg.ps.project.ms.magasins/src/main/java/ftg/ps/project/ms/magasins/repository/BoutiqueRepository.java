package ftg.ps.project.ms.magasins.repository;

import ftg.ps.project.ms.magasins.domain.Boutique;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Boutique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoutiqueRepository extends JpaRepository<Boutique, Long> {

}
