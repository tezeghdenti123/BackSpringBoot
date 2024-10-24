package com.bezkoder.springjwt.DTO;

import com.bezkoder.springjwt.models.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConsultantPublicDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String phone;
    private String title;
    private String linkedIn;
    private List<Langues> languesList;
    private List<Formations> formationsList;
    private List<Experience> experienceList;
    private List<Skill> skillList;
    private List<Affectation> affectationList ;
    private List<Certificats> certificatsList ;
    private Long commercialeId;
    private Long gestionnaireId;
    private Boolean isAvailable;
    private Set<Role> roles = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Experience> getExperienceList() {
        return experienceList;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Long getCommercialeId() {
        return commercialeId;
    }

    public void setCommercialeId(Long commercialeId) {
        this.commercialeId = commercialeId;
    }

    public Long getGestionnaireId() {
        return gestionnaireId;
    }

    public void setGestionnaireId(Long gestionnaireId) {
        this.gestionnaireId = gestionnaireId;
    }

    public List<Certificats> getCertificatsList() {
        return certificatsList;
    }

    public void setCertificatsList(List<Certificats> certificatsList) {
        this.certificatsList = certificatsList;
    }

    public List<Affectation> getAffectationList() {
        return affectationList;
    }

    public void setAffectationList(List<Affectation> affectationList) {
        this.affectationList = affectationList;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }

    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    public List<Formations> getFormationsList() {
        return formationsList;
    }

    public void setFormationsList(List<Formations> formationsList) {
        this.formationsList = formationsList;
    }

    public List<Langues> getLanguesList() {
        return languesList;
    }

    public void setLanguesList(List<Langues> languesList) {
        this.languesList = languesList;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
