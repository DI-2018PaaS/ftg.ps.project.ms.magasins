package ftg.ps.project.ms.magasins.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BoutiqueProduit.
 */
@Entity
@Table(name = "boutique_produit")
public class BoutiqueProduit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "boutiqueProduit")
    private Set<Boutique> boutiqueProduitBS = new HashSet<>();

    @OneToMany(mappedBy = "boutiqueProduit")
    private Set<Produit> boutiqueProduitPS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Boutique> getBoutiqueProduitBS() {
        return boutiqueProduitBS;
    }

    public BoutiqueProduit boutiqueProduitBS(Set<Boutique> boutiques) {
        this.boutiqueProduitBS = boutiques;
        return this;
    }

    public BoutiqueProduit addBoutiqueProduitB(Boutique boutique) {
        this.boutiqueProduitBS.add(boutique);
        boutique.setBoutiqueProduit(this);
        return this;
    }

    public BoutiqueProduit removeBoutiqueProduitB(Boutique boutique) {
        this.boutiqueProduitBS.remove(boutique);
        boutique.setBoutiqueProduit(null);
        return this;
    }

    public void setBoutiqueProduitBS(Set<Boutique> boutiques) {
        this.boutiqueProduitBS = boutiques;
    }

    public Set<Produit> getBoutiqueProduitPS() {
        return boutiqueProduitPS;
    }

    public BoutiqueProduit boutiqueProduitPS(Set<Produit> produits) {
        this.boutiqueProduitPS = produits;
        return this;
    }

    public BoutiqueProduit addBoutiqueProduitP(Produit produit) {
        this.boutiqueProduitPS.add(produit);
        produit.setBoutiqueProduit(this);
        return this;
    }

    public BoutiqueProduit removeBoutiqueProduitP(Produit produit) {
        this.boutiqueProduitPS.remove(produit);
        produit.setBoutiqueProduit(null);
        return this;
    }

    public void setBoutiqueProduitPS(Set<Produit> produits) {
        this.boutiqueProduitPS = produits;
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
        BoutiqueProduit boutiqueProduit = (BoutiqueProduit) o;
        if (boutiqueProduit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boutiqueProduit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoutiqueProduit{" +
            "id=" + getId() +
            "}";
    }
}
