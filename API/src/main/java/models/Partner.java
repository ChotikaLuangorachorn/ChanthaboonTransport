package models;

import java.io.Serializable;

public class Partner extends Person implements Serializable {
    private String company;
    private String companyPhone;


    public Partner(String id, String firstName, String lastName, String company, String companyPhone) {
        super(id, firstName, lastName);
        this.company = company;
        this.companyPhone = companyPhone;
    }

    public String getCompany() {
        return company;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }


}
