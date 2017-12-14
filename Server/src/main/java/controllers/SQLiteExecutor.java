package controllers;

import controllers.assistants.QueryExecutionAssistant;
import controllers.assistants.UpdateExecutionAssistant;
import controllers.delegations.*;
import managers.CustomerDatabaseManager;
import managers.ManagerDatabaseManager;
import models.*;
import models.Driver;
import org.springframework.lang.Nullable;
import utils.ReservationDateFormatter;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class SQLiteExecutor implements CustomerDatabaseManager, ManagerDatabaseManager   {
    private String url = "vanScheduler.db";
    private SimpleDateFormat formatter = ReservationDateFormatter.getInstance().getDbFormatter();
    private VanExecutor vanExecutor;
    private PriceExecutor priceExecutor;
    private DriverExecutor driverExecutor;
    private CustomerExecutor customerExecutor;
    private PartnerExecutor partnerExecutor;
    private ReservationExecutor reservationExecutor;

    public SQLiteExecutor() {
        vanExecutor = new VanExecutor(url);
        driverExecutor = new DriverExecutor(url);
        customerExecutor = new CustomerExecutor(url);
        partnerExecutor = new PartnerExecutor(url);
        reservationExecutor = new ReservationExecutor(url);
        priceExecutor = new PriceExecutor(url);
    }

    public void editCustomerInfo(Customer customer) {
        customerExecutor.editCustomerInfo(customer);
    }
    @Nullable
    public Customer getCustomer(String citizenId, String pwd) {
        return customerExecutor.getCustomer(citizenId, pwd);
    }
    @Nullable
    public Map<String, Integer> getVanAvailableAmount(Destination destination, Date startDate, Date endDate) {
        return vanExecutor.getVanAvailableAmount(destination, startDate, endDate);
    }
    public Van getVan(String vanId){
        return vanExecutor.getVan(vanId);
    }
    public List<Schedule> getVanSchedule(Van van) {
        return getVanSchedule(van.getRegisNumber());
    }
    public List<Schedule> getVanSchedule(String regisNumber) {
        return vanExecutor.getVanSchedule(regisNumber);
    }
    public void deleteVanSchedule(Schedule schedule) {
        vanExecutor.deleteVanSchedule(schedule);
    }
    public void editVanSchedule(Schedule oldSchedule, Schedule newSchedule) {
        vanExecutor.editVanSchedule(oldSchedule, newSchedule);
    }
    public void assignVan(List<Van> vans, Reservation reservation) {
        assignVan(vans, reservation.getReserveId());
    }
    public void assignVan(List<Van> vans, String reservationId) {
        reservationExecutor.assignVan(vans, reservationId);
    }
    public List<JobType> getVanJobTypes() {
        return vanExecutor.getVanJobTypes();
    }
    public void addVanJob(Van van, Date startDate, Date endDate, JobType type) {
        vanExecutor.addVanJob(van, startDate, endDate, type);
    }
    public double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate) {
        return priceExecutor.getPrice(vanAmt, startDate, endDate);
    }
    public double getPrice(Map<String, Integer> vanAmt, Destination destination) {
        return priceExecutor.getPrice(vanAmt, destination);
    }
    public void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, Date reserveDate, double price, double deposit) {
        reservationExecutor.addReservation(customerId, vanAmt, destination, startDate, endDate, reserveDate, price, deposit);
    }
    public void deleteReservation(Reservation reservation) {
        deleteReservation(reservation.getReserveId());
    }
    public void deleteReservation(String reservationId){
        reservationExecutor.deleteReservation(reservationId);
    }
    public void assignDriver(List<Driver> drivers, Reservation reservation) {
        assignDriver(drivers, reservation.getReserveId());
    }
    public void assignDriver(List<Driver> drivers, String reservationId) {
        reservationExecutor.assignDriver(drivers, reservationId);
    }
    public List<JobType> getDriverJobTypes() {
        return driverExecutor.getDriverJobTypes();
    }
    public List<Schedule> getDriverSchedule(Driver driver) {
        return getDriverSchedule(driver.getCitizenId());
    }
    public List<Schedule> getDriverSchedule(String citizenId) {
        return driverExecutor.getDriverSchedule(citizenId);
    }
    public void deleteDriverSchedule(Schedule schedule) {
        driverExecutor.deleteDriverSchedule(schedule);
    }
    public void editDriverSchedule(Schedule oldSchedule, Schedule newSchedule) {
        driverExecutor.editDriverSchedule(oldSchedule, newSchedule);
    }

    @Override
    public void addDriverJob(Driver driver, Date startDate, Date endDate, JobType type) {
        driverExecutor.addDriverJob(driver, startDate, endDate, type);
    }

    public void addMeeting(String meetingPlace, Date meetingTime, Reservation reservation) {
        addMeeting(meetingPlace, meetingTime, reservation.getReserveId());
    }
    public void addMeeting(String meetingPlace, Date meetingTime, String reservationId) {
        reservationExecutor.addMeeting(meetingPlace, meetingTime, reservationId);
    }
    public void confirmDeposit(Reservation reservation, Date depositDate) {
        confirmDeposit(reservation.getReserveId(), depositDate);
    }
    public void confirmDeposit(String reservationId, Date depositDate) {
        reservationExecutor.confirmDeposit(reservationId, depositDate);
    }
    public List<Reservation> getReservations() {
        return reservationExecutor.getReservations();
    }
    public Reservation getReservation(String reserveId) {
        // TODO getReservation
        return null;
    }
    public List<Reservation> getHistoryReservation(String citizenId) {
        return reservationExecutor.getHistoryReservation(citizenId);
    }
    public List<String> getProvinces() {
        return priceExecutor.getProvinces();
    }
    public List<String> getDistricts(String province) {
        return priceExecutor.getDistricts(province);
    }
    public List<Van> getVans() {
        return vanExecutor.getVans();
    }
    public Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate) {
        return vanExecutor.getVanAvailable(startDate, endDate);
    }
    public void editVan(Van van) {
        vanExecutor.editVan(van);
    }
    public void deleteVan(Van van) {
        deleteVan(van.getRegisNumber());
    }
    public void deleteVan(String regisNumber) {
        vanExecutor.deleteVan(regisNumber);
    }
    public List<Partner> getPartners() {
        return partnerExecutor.getPartners();
    }
    public void editPartner(Partner partner) {
        partnerExecutor.editPartner(partner);
    }
    public void deletePartner(Partner partner) {
        deletePartner(partner.getId());
    }
    public void deletePartner(int partnerId) {
        partnerExecutor.deletePartner(partnerId);
    }
    public List<Driver> getDriverAvailable(Date startDate, Date endDate) {
        return driverExecutor.getDriverAvailable(startDate, endDate);
    }
    public List<Driver> getDrivers() {
        return driverExecutor.getDrivers();
    }
    public void editDriver(Driver driver) {
        driverExecutor.editDriver(driver);
    }
    public void deleteDriver(Driver driver) {
        deleteDriver(driver.getCitizenId());
    }
    public void deleteDriver(String citizenId) {
        driverExecutor.deleteDriver(citizenId);
    }
    public void editReservation(Reservation reservation) {
        // TODO edit reservation
    }
    public boolean changeCustomerPassword(String citizenId, String oldPwd, String newPwd) {
        return changeCustomerPassword(citizenId, oldPwd, newPwd);
    }
    public PriceFactor getPriceFactor() {
        return priceExecutor.getPriceFactor();
    }
    @Override
    public Date getMinimumDate(Destination destination, Date startDate) {
        return vanExecutor.getMinimumDate(destination, startDate);
    }
    public void updatePriceFactor(PriceFactor factor) {
        priceExecutor.updatePriceFactor(factor);
    }
}
