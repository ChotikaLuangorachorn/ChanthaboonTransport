package models;

public class Partner {
    private int id;
    private String name;
    private String company;
    private String companyPhone;

    public Partner(int id, String name, String company, String companyPhone) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.companyPhone = companyPhone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    @Override
    public String toString() {
        return "Partner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                '}';
    }
}
