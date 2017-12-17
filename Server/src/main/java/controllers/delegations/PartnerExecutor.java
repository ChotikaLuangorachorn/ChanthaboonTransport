package controllers.delegations;

import controllers.assistants.QueryExecutionAssistant;
import controllers.assistants.UpdateExecutionAssistant;
import models.Partner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PartnerExecutor {
    private String url;

    public PartnerExecutor(String url) {
        this.url = url;
    }

    public List<Partner> getPartners() {
        List<Partner> partners = new ArrayList<>();
        String sql = "select * from partner";
        QueryExecutionAssistant<List<Partner>> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, resultSet -> {
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String company = resultSet.getString("company");
                String companyPhone = resultSet.getString("company_phone");
                // TODO partner last name
                Partner partner = new Partner(id, name, "", company, companyPhone);
                partners.add(partner);
            }
            return partners;
        }, partners);
    }
    public void editPartner(Partner partner) {
        // TODO partner last name
        String sql = String.format("update partner " +
                        "set name='%s'," +
                        "company='%s'," +
                        "company_phone='%s' " +
                        "where id='%s'",
                partner.getFirstName(), partner.getCompany(),
                partner.getCompanyPhone(), partner.getId());
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
    public void deletePartner(String partnerId) {
        System.out.println("request deletePartner");
        String sql = "delete from partner where id='" + partnerId + "'";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        assistant.execute(sql);
    }
}
