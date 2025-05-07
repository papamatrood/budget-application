// src/main/java/com/yourcompany/service/dto/UserAppUserDTO.java
package com.cratechnologie.budget.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import com.cratechnologie.budget.domain.AppUser;
import com.cratechnologie.budget.domain.enumeration.FamilySituationEnum;
import com.cratechnologie.budget.domain.enumeration.GenderEnum;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

/**
 * A DTO for creating both User and AppUser in a single request.
 */
public class UserAppUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // User fields
    @NotBlank(message = "Login ne doit pas être vide")
    @Size(min = 1, max = 50, message = "La longueur du login doit être entre 1 et 50 caractères")
    private String login;

    @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères")
    private String firstName;

    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    private String lastName;

    @Email(message = "L'email doit être une adresse valide")
    @Size(min = 5, max = 254, message = "La longueur de l'email doit être entre 5 et 254 caractères")
    private String email;

    private boolean activated = true;

    @Size(min = 2, max = 10, message = "La langue doit contenir entre 2 et 10 caractères")
    private String langKey = "fr";

    @NotNull(message = "Au moins un rôle doit être spécifié")
    private Set<String> authorities;

    // AppUser fields
    private Boolean accountStatus;

    @Size(max = 20, message = "Le numéro de téléphone ne doit pas dépasser 20 caractères")
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s./0-9]*$", message = "Format de téléphone invalide")
    private String phoneNumber;

    private Instant birthDate;

    @Size(max = 100, message = "Le lieu de naissance ne doit pas dépasser 100 caractères")
    private String birthPlace;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Enumerated(EnumType.STRING)
    private FamilySituationEnum familySituation;

    @Size(max = 100, message = "Le poste ne doit pas dépasser 100 caractères")
    private String position;

    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    private String address;

    @Size(max = 255, message = "L'image ne doit pas dépasser 255 caractères")
    private String imageUrl;

    private Long jhipsterUserId;

    // Default constructor
    public UserAppUserDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Boolean getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public GenderEnum getGender() {
        return this.gender;
    }

    public UserAppUserDTO gender(GenderEnum gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public FamilySituationEnum getFamilySituation() {
        return this.familySituation;
    }

    public UserAppUserDTO familySituation(FamilySituationEnum familySituation) {
        this.setFamilySituation(familySituation);
        return this;
    }

    public void setFamilySituation(FamilySituationEnum familySituation) {
        this.familySituation = familySituation;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getJhipsterUserId() {
        return jhipsterUserId;
    }

    public void setJhipsterUserId(Long jhipsterUserId) {
        this.jhipsterUserId = jhipsterUserId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserAppUserDTO that = (UserAppUserDTO) o;
        return activated == that.activated &&
                Objects.equals(login, that.login) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(langKey, that.langKey) &&
                Objects.equals(authorities, that.authorities) &&
                Objects.equals(accountStatus, that.accountStatus) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(birthDate, that.birthDate) &&
                Objects.equals(birthPlace, that.birthPlace) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(familySituation, that.familySituation) &&
                Objects.equals(position, that.position) &&
                Objects.equals(address, that.address) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                login, firstName, lastName, email, activated, langKey, authorities,
                accountStatus, phoneNumber, birthDate, birthPlace, gender,
                familySituation, position, address, imageUrl);
    }

    // toString
    @Override
    public String toString() {
        return "UserAppUserDTO{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", activated=" + activated +
                ", langKey='" + langKey + '\'' +
                ", authorities=" + authorities +
                ", accountStatus=" + accountStatus +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate=" + birthDate +
                ", birthPlace='" + birthPlace + '\'' +
                ", gender='" + gender + '\'' +
                ", familySituation='" + familySituation + '\'' +
                ", position='" + position + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    // Builder pattern (optionnel mais recommandé)
    // public static Builder builder() {
    // return new Builder();
    // }

    // public static final class Builder {
    // private String login;
    // private String firstName;
    // private String lastName;
    // private String email;
    // private boolean activated = true;
    // private String langKey = "fr";
    // private Set<String> authorities;
    // private Boolean accountStatus;
    // private String phoneNumber;
    // private Instant birthDate;
    // private String birthPlace;
    // private String gender;
    // private String familySituation;
    // private String position;
    // private String address;

    // private Builder() {
    // }

    // public Builder login(String login) {
    // this.login = login;
    // return this;
    // }

    // public Builder firstName(String firstName) {
    // this.firstName = firstName;
    // return this;
    // }

    // public Builder lastName(String lastName) {
    // this.lastName = lastName;
    // return this;
    // }

    // public Builder email(String email) {
    // this.email = email;
    // return this;
    // }

    // public Builder activated(boolean activated) {
    // this.activated = activated;
    // return this;
    // }

    // public Builder langKey(String langKey) {
    // this.langKey = langKey;
    // return this;
    // }

    // public Builder authorities(Set<String> authorities) {
    // this.authorities = authorities;
    // return this;
    // }

    // public Builder accountStatus(Boolean accountStatus) {
    // this.accountStatus = accountStatus;
    // return this;
    // }

    // public Builder phoneNumber(String phoneNumber) {
    // this.phoneNumber = phoneNumber;
    // return this;
    // }

    // public Builder birthDate(Instant birthDate) {
    // this.birthDate = birthDate;
    // return this;
    // }

    // public Builder birthPlace(String birthPlace) {
    // this.birthPlace = birthPlace;
    // return this;
    // }

    // public Builder gender(String gender) {
    // this.gender = gender;
    // return this;
    // }

    // public Builder familySituation(String familySituation) {
    // this.familySituation = familySituation;
    // return this;
    // }

    // public Builder position(String position) {
    // this.position = position;
    // return this;
    // }

    // public Builder address(String address) {
    // this.address = address;
    // return this;
    // }

    // public UserAppUserDTO build() {
    // UserAppUserDTO dto = new UserAppUserDTO();
    // dto.setLogin(login);
    // dto.setFirstName(firstName);
    // dto.setLastName(lastName);
    // dto.setEmail(email);
    // dto.setActivated(activated);
    // dto.setLangKey(langKey);
    // dto.setAuthorities(authorities);
    // dto.setAccountStatus(accountStatus);
    // dto.setPhoneNumber(phoneNumber);
    // dto.setBirthDate(birthDate);
    // dto.setBirthPlace(birthPlace);
    // dto.setGender(gender);
    // dto.setFamilySituation(familySituation);
    // dto.setPosition(position);
    // dto.setAddress(address);
    // return dto;
    // }
    // }
}