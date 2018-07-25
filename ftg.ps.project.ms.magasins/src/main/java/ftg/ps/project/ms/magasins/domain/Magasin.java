package ftg.ps.project.ms.magasins.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Magasin.
 */
@Entity
@Table(name = "magasin")
public class Magasin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "n_id_proprietaire")
    private Long nIdProprietaire;

    @Column(name = "jhi_ref")
    private String ref;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("magasinPrduitMS")
    private MagasinProduit magasinProduit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getnIdProprietaire() {
        return nIdProprietaire;
    }

    public Magasin nIdProprietaire(Long nIdProprietaire) {
        this.nIdProprietaire = nIdProprietaire;
        return this;
    }

    public void setnIdProprietaire(Long nIdProprietaire) {
        this.nIdProprietaire = nIdProprietaire;
    }

    public String getRef() {
        return ref;
    }

    public Magasin ref(String ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAdresse() {
        return adresse;
    }

    public Magasin adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return description;
    }

    public Magasin description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MagasinProduit getMagasinProduit() {
        return magasinProduit;
    }

    public Magasin magasinProduit(MagasinProduit magasinProduit) {
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
        Magasin magasin = (Magasin) o;
        if (magasin.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), magasin.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Magasin{" +
            "id=" + getId() +
            ", nIdProprietaire=" + getnIdProprietaire() +
            ", ref='" + getRef() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
