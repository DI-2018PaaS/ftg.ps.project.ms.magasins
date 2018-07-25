package ftg.ps.project.ms.magasins.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MagasinProduit.
 */
@Entity
@Table(name = "magasin_produit")
public class MagasinProduit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "magasinProduit")
    private Set<Magasin> magasinPrduitMS = new HashSet<>();

    @OneToMany(mappedBy = "magasinProduit")
    private Set<Produit> magasinPrduitBS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Magasin> getMagasinPrduitMS() {
        return magasinPrduitMS;
    }

    public MagasinProduit magasinPrduitMS(Set<Magasin> magasins) {
        this.magasinPrduitMS = magasins;
        return this;
    }

    public MagasinProduit addMagasinPrduitM(Magasin magasin) {
        this.magasinPrduitMS.add(magasin);
        magasin.setMagasinProduit(this);
        return this;
    }

    public MagasinProduit removeMagasinPrduitM(Magasin magasin) {
        this.magasinPrduitMS.remove(magasin);
        magasin.setMagasinProduit(null);
        return this;
    }

    public void setMagasinPrduitMS(Set<Magasin> magasins) {
        this.magasinPrduitMS = magasins;
    }

    public Set<Produit> getMagasinPrduitBS() {
        return magasinPrduitBS;
    }

    public MagasinProduit magasinPrduitBS(Set<Produit> produits) {
        this.magasinPrduitBS = produits;
        return this;
    }

    public MagasinProduit addMagasinPrduitB(Produit produit) {
        this.magasinPrduitBS.add(produit);
        produit.setMagasinProduit(this);
        return this;
    }

    public MagasinProduit removeMagasinPrduitB(Produit produit) {
        this.magasinPrduitBS.remove(produit);
        produit.setMagasinProduit(null);
        return this;
    }

    public void setMagasinPrduitBS(Set<Produit> produits) {
        this.magasinPrduitBS = produits;
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
        MagasinProduit magasinProduit = (MagasinProduit) o;
        if (magasinProduit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), magasinProduit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MagasinProduit{" +
            "id=" + getId() +
            "}";
    }
}
