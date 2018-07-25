package ftg.ps.project.ms.magasins.repository;

import ftg.ps.project.ms.magasins.domain.Magasin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Magasin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MagasinRepository extends JpaRepository<Magasin, Long> {

}
