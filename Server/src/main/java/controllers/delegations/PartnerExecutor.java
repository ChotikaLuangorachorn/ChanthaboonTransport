package controllers.delegations;

import controllers.assistants.UpdateExecutionAssistant;
import models.Partner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PartnerExecutor {
    private String url;

    public PartnerExecutor(String url) {
        this.url = url;
    }

    public void editPartner(Partner partner) {
        String sql = String.format("update partner " +
                        "set name='%s'," +
                        "company='%s'," +
                        "company_phone='%s' " +
                        "where id='%s'",
                partner.getName(), partner.getCompany(),
                partner.getCompanyPhone(), partner.getId());
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
    public void deletePartner(int partnerId) {
        System.out.println("request deletePartner");
        String sql = "delete from partner where id='" + partnerId + "'";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        assistant.execute(sql);
    }
}
