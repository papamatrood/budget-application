package com.cratechnologie.budget.domain;

import com.cratechnologie.budget.domain.enumeration.FamilySituationEnum;
import com.cratechnologie.budget.domain.enumeration.GenderEnum;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "account_status")
    private Boolean accountStatus;

    @Column(name = "last_date_update")
    private Instant lastDateUpdate;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private Instant birthDate;

    @Column(name = "birth_place")
    private String birthPlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "family_situation")
    private FamilySituationEnum familySituation;

    @Column(name = "position")
    private String position;

    @Column(name = "address")
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAccountStatus() {
        return this.accountStatus;
    }

    public AppUser accountStatus(Boolean accountStatus) {
        this.setAccountStatus(accountStatus);
        return this;
    }

    public void setAccountStatus(Boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Instant getLastDateUpdate() {
        return this.lastDateUpdate;
    }

    public AppUser lastDateUpdate(Instant lastDateUpdate) {
        this.setLastDateUpdate(lastDateUpdate);
        return this;
    }

    public void setLastDateUpdate(Instant lastDateUpdate) {
        this.lastDateUpdate = lastDateUpdate;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public AppUser dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public AppUser firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public AppUser lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public AppUser phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getBirthDate() {
        return this.birthDate;
    }

    public AppUser birthDate(Instant birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public AppUser birthPlace(String birthPlace) {
        this.setBirthPlace(birthPlace);
        return this;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public GenderEnum getGender() {
        return this.gender;
    }

    public AppUser gender(GenderEnum gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public FamilySituationEnum getFamilySituation() {
        return this.familySituation;
    }

    public AppUser familySituation(FamilySituationEnum familySituation) {
        this.setFamilySituation(familySituation);
        return this;
    }

    public void setFamilySituation(FamilySituationEnum familySituation) {
        this.familySituation = familySituation;
    }

    public String getPosition() {
        return this.position;
    }

    public AppUser position(String position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return this.address;
    }

    public AppUser address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AppUser user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return getId() != null && getId().equals(((AppUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", accountStatus='" + getAccountStatus() + "'" +
            ", lastDateUpdate='" + getLastDateUpdate() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", birthPlace='" + getBirthPlace() + "'" +
            ", gender='" + getGender() + "'" +
            ", familySituation='" + getFamilySituation() + "'" +
            ", position='" + getPosition() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
