package ftg.ps.project.ms.magasins.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Produit.
 */
@Entity
@Table(name = "produit")
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "designation")
    private String designation;

    @Column(name = "prix_unitaire", precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "description_produit")
    private String descriptionProduit;

    @ManyToOne
    @JsonIgnoreProperties("boutiqueProduitPS")
    private BoutiqueProduit boutiqueProduit;

    @ManyToOne
    @JsonIgnoreProperties("articlePS")
    private BonCommande bonCommande;

    @ManyToOne
    @JsonIgnoreProperties("magasinPrduitBS")
    private MagasinProduit magasinProduit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Produit code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public Produit designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public Produit prixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        return this;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getDescriptionProduit() {
        return descriptionProduit;
    }

    public Produit descriptionProduit(String descriptionProduit) {
        this.descriptionProduit = descriptionProduit;
        return this;
    }

    public void setDescriptionProduit(String descriptionProduit) {
        this.descriptionProduit = descriptionProduit;
    }

    public BoutiqueProduit getBoutiqueProduit() {
        return boutiqueProduit;
    }

    public Produit boutiqueProduit(BoutiqueProduit boutiqueProduit) {
        this.boutiqueProduit = boutiqueProduit;
        return this;
    }

    public void setBoutiqueProduit(BoutiqueProduit boutiqueProduit) {
        this.boutiqueProduit = boutiqueProduit;
    }

    public BonCommande getBonCommande() {
        return bonCommande;
    }

    public Produit bonCommande(BonCommande bonCommande) {
        this.bonCommande = bonCommande;
        return this;
    }

    public void setBonCommande(BonCommande bonCommande) {
        this.bonCommande = bonCommande;
    }

    public MagasinProduit getMagasinProduit() {
        return magasinProduit;
    }

    public Produit magasinProduit(MagasinProduit magasinProduit) {
        this.magasinProduit = magasinProduit;
        return this;
    }

    public void setMagasinProduit(MagasinProduit magasinProduit) {
        this.magasinProduit = magasinProduit;
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
        Produit produit = (Produit) o;
        if (produit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), produit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", descriptionProduit='" + getDescriptionProduit() + "'" +
            "}";
    }
}
