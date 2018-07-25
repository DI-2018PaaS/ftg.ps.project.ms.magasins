package ftg.ps.project.ms.magasins.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BonCommande.
 */
@Entity
@Table(name = "bon_commande")
public class BonCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "numero")
    private Long numero;

    @Column(name = "date_emission")
    private LocalDate dateEmission;

    @Column(name = "date_reglement")
    private LocalDate dateReglement;

    @Column(name = "acheteur_id")
    private Long acheteurId;

    @OneToOne
    @JoinColumn(unique = true)
    private BonLivraison livraisonBon;

    @OneToMany(mappedBy = "bonCommande")
    private Set<Produit> articlePS = new HashSet<>();

    @OneToMany(mappedBy = "bonCommande")
    private Set<Service> articleS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumero() {
        return numero;
    }

    public BonCommande numero(Long numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public BonCommande dateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
        return this;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDate getDateReglement() {
        return dateReglement;
    }

    public BonCommande dateReglement(LocalDate dateReglement) {
        this.dateReglement = dateReglement;
        return this;
    }

    public void setDateReglement(LocalDate dateReglement) {
        this.dateReglement = dateReglement;
    }

    public Long getAcheteurId() {
        return acheteurId;
    }

    public BonCommande acheteurId(Long acheteurId) {
        this.acheteurId = acheteurId;
        return this;
    }

    public void setAcheteurId(Long acheteurId) {
        this.acheteurId = acheteurId;
    }

    public BonLivraison getLivraisonBon() {
        return livraisonBon;
    }

    public BonCommande livraisonBon(BonLivraison bonLivraison) {
        this.livraisonBon = bonLivraison;
        return this;
    }

    public void setLivraisonBon(BonLivraison bonLivraison) {
        this.livraisonBon = bonLivraison;
    }

    public Set<Produit> getArticlePS() {
        return articlePS;
    }

    public BonCommande articlePS(Set<Produit> produits) {
        this.articlePS = produits;
        return this;
    }

    public BonCommande addArticleP(Produit produit) {
        this.articlePS.add(produit);
        produit.setBonCommande(this);
        return this;
    }

    public BonCommande removeArticleP(Produit produit) {
        this.articlePS.remove(produit);
        produit.setBonCommande(null);
        return this;
    }

    public void setArticlePS(Set<Produit> produits) {
        this.articlePS = produits;
    }

    public Set<Service> getArticleS() {
        return articleS;
    }

    public BonCommande articleS(Set<Service> services) {
        this.articleS = services;
        return this;
    }

    public BonCommande addArticleS(Service service) {
        this.articleS.add(service);
        service.setBonCommande(this);
        return this;
    }

    public BonCommande removeArticleS(Service service) {
        this.articleS.remove(service);
        service.setBonCommande(null);
        return this;
    }

    public void setArticleS(Set<Service> services) {
        this.articleS = services;
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
        BonCommande bonCommande = (BonCommande) o;
        if (bonCommande.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bonCommande.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BonCommande{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", dateEmission='" + getDateEmission() + "'" +
            ", dateReglement='" + getDateReglement() + "'" +
            ", acheteurId=" + getAcheteurId() +
            "}";
    }
}
