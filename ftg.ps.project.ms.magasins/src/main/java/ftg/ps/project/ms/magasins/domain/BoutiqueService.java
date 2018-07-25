package ftg.ps.project.ms.magasins.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BoutiqueService.
 */
@Entity
@Table(name = "boutique_service")
public class BoutiqueService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "boutiqueService")
    private Set<Service> boutiqueServiceS = new HashSet<>();

    @OneToMany(mappedBy = "boutiqueService")
    private Set<Boutique> boutiqueServiceBS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Service> getBoutiqueServiceS() {
        return boutiqueServiceS;
    }

    public BoutiqueService boutiqueServiceS(Set<Service> services) {
        this.boutiqueServiceS = services;
        return this;
    }

    public BoutiqueService addBoutiqueServiceS(Service service) {
        this.boutiqueServiceS.add(service);
        service.setBoutiqueService(this);
        return this;
    }

    public BoutiqueService removeBoutiqueServiceS(Service service) {
        this.boutiqueServiceS.remove(service);
        service.setBoutiqueService(null);
        return this;
    }

    public void setBoutiqueServiceS(Set<Service> services) {
        this.boutiqueServiceS = services;
    }

    public Set<Boutique> getBoutiqueServiceBS() {
        return boutiqueServiceBS;
    }

    public BoutiqueService boutiqueServiceBS(Set<Boutique> boutiques) {
        this.boutiqueServiceBS = boutiques;
        return this;
    }

    public BoutiqueService addBoutiqueServiceB(Boutique boutique) {
        this.boutiqueServiceBS.add(boutique);
        boutique.setBoutiqueService(this);
        return this;
    }

    public BoutiqueService removeBoutiqueServiceB(Boutique boutique) {
        this.boutiqueServiceBS.remove(boutique);
        boutique.setBoutiqueService(null);
        return this;
    }

    public void setBoutiqueServiceBS(Set<Boutique> boutiques) {
        this.boutiqueServiceBS = boutiques;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoutiqueService boutiqueService = (BoutiqueService) o;
        if (boutiqueService.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boutiqueService.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoutiqueService{" +
            "id=" + getId() +
            "}";
    }
}
