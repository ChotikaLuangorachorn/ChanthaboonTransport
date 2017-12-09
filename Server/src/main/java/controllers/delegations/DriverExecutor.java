package controllers.delegations;

import controllers.assistants.UpdateExecutionAssistant;
import models.Driver;

import java.util.ArrayList;
import java.util.List;

public class DriverExecutor {
    private String url;

    public DriverExecutor(String url) {
        this.url = url;
    }
    public void assignDriver(List<Driver> drivers, String reservationId) {
        String sql = String.format("insert into driver_reserve_schedule " +
                "select driver.citizen_id, '%s' " +
                "from driver " +
                "where driver.citizen_id in ", reservationId);
        List<String> driverIds = new ArrayList<>();
        for (Driver driver:drivers)
            driverIds.add("'" + driver.getCitizenId() + "'");
        sql += "(" + String.join(",", driverIds) + ")";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
}
