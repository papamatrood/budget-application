package com.cratechnologie.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Supplier.
 */
@Entity
@Table(name = "supplier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "nif_number")
    private String nifNumber;

    @Column(name = "commercial_register")
    private String commercialRegister;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "mandating_establishment")
    private String mandatingEstablishment;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "description")
    private String description;

    @Column(name = "contact_firstname")
    private String contactFirstname;

    @Column(name = "contactlastname")
    private String contactlastname;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annexDecision", "supplier", "engagement", "purchaseOrderItems" }, allowSetters = true)
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Supplier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Supplier companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return this.address;
    }

    public Supplier address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public Supplier phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNifNumber() {
        return this.nifNumber;
    }

    public Supplier nifNumber(String nifNumber) {
        this.setNifNumber(nifNumber);
        return this;
    }

    public void setNifNumber(String nifNumber) {
        this.nifNumber = nifNumber;
    }

    public String getCommercialRegister() {
        return this.commercialRegister;
    }

    public Supplier commercialRegister(String commercialRegister) {
        this.setCommercialRegister(commercialRegister);
        return this;
    }

    public void setCommercialRegister(String commercialRegister) {
        this.commercialRegister = commercialRegister;
    }

    public String getBankAccount() {
        return this.bankAccount;
    }

    public Supplier bankAccount(String bankAccount) {
        this.setBankAccount(bankAccount);
        return this;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getMandatingEstablishment() {
        return this.mandatingEstablishment;
    }

    public Supplier mandatingEstablishment(String mandatingEstablishment) {
        this.setMandatingEstablishment(mandatingEstablishment);
        return this;
    }

    public void setMandatingEstablishment(String mandatingEstablishment) {
        this.mandatingEstablishment = mandatingEstablishment;
    }

    public String getEmail() {
        return this.email;
    }

    public Supplier email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return this.website;
    }

    public Supplier website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return this.description;
    }

    public Supplier description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactFirstname() {
        return this.contactFirstname;
    }

    public Supplier contactFirstname(String contactFirstname) {
        this.setContactFirstname(contactFirstname);
        return this;
    }

    public void setContactFirstname(String contactFirstname) {
        this.contactFirstname = contactFirstname;
    }

    public String getContactlastname() {
        return this.contactlastname;
    }

    public Supplier contactlastname(String contactlastname) {
        this.setContactlastname(contactlastname);
        return this;
    }

    public void setContactlastname(String contactlastname) {
        this.contactlastname = contactlastname;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        if (this.purchaseOrders != null) {
            this.purchaseOrders.forEach(i -> i.setSupplier(null));
        }
        if (purchaseOrders != null) {
            purchaseOrders.forEach(i -> i.setSupplier(this));
        }
        this.purchaseOrders = purchaseOrders;
    }

    public Supplier purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.setPurchaseOrders(purchaseOrders);
        return this;
    }

    public Supplier addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        purchaseOrder.setSupplier(this);
        return this;
    }

    public Supplier removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        purchaseOrder.setSupplier(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplier)) {
            return false;
        }
        return getId() != null && getId().equals(((Supplier) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Supplier{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", nifNumber='" + getNifNumber() + "'" +
            ", commercialRegister='" + getCommercialRegister() + "'" +
            ", bankAccount='" + getBankAccount() + "'" +
            ", mandatingEstablishment='" + getMandatingEstablishment() + "'" +
            ", email='" + getEmail() + "'" +
            ", website='" + getWebsite() + "'" +
            ", description='" + getDescription() + "'" +
            ", contactFirstname='" + getContactFirstname() + "'" +
            ", contactlastname='" + getContactlastname() + "'" +
            "}";
    }
}
