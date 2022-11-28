package org.smartregister.anc.library.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OpenSRP ANC client definition customized to comply with localized requirements.
 */
public class Client extends org.smartregister.clientandeventmodel.Client {

    /** Constants for additional fields */
    public static class Constants {
        /** Indonesian National Registration ID Number */
        public static final String NIK = "nik";
        /** Indonesian Sosial Security Number */
        public static final String BPJS = "bpjs";
    }

    @JsonProperty
    private String nik = "";
    @JsonProperty
    private String bpjs = "";

    public Client(String baseEntityId) {
        super(baseEntityId);
    }

    /** Create localized Client instance from base Client data. */
    public Client(org.smartregister.clientandeventmodel.Client baseClient) {
        // Create instance using baseEntityId
        super(baseClient.getBaseEntityId());
        // Set the fields value from org.smartregister.clientandeventmodel.Client class
        this.setFirstName(baseClient.getFirstName());
        this.setMiddleName(baseClient.getMiddleName());
        this.setLastName(baseClient.getLastName());
        this.setBirthdate(baseClient.getBirthdate());
        this.setBirthdateApprox(baseClient.getBirthdateApprox());
        this.setDeathdate(baseClient.getDeathdate());
        this.setDeathdateApprox(baseClient.getDeathdateApprox());
        this.setGender(baseClient.getGender());
        this.setRelationships(baseClient.getRelationships());
        // Set the fields value from org.smartregister.clientandeventmodel.BaseEntity class
        this.setIdentifiers(baseClient.getIdentifiers());
        this.setAddresses(baseClient.getAddresses());
        this.setAttributes(baseClient.getAttributes());
    }

    public String getNik() {
        return this.nik;
    }

    public String getBpjs() {
        return this.bpjs;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public void setBpjs(String bpjs) {
        this.bpjs = bpjs;
    }

    public Client withNik(String nik) {
        this.nik = nik;
        return this;
    }

    public Client withBpjs(String bpjs) {
        this.bpjs = bpjs;
        return this;
    }

}