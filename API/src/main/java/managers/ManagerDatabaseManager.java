package managers;

import models.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ManagerDatabaseManager {
    String VIP = "VIP";
    String NORMAL = "NORMAL";

    Customer getCustomer(String citizenId, String pwd);
    Map<String, Integer> getVanAvailableAmount(Destination destination, Date startDate, Date endDate);
    double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate);
    double getPrice(Map<String, Integer> vanAmt, Destination destination);
    void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, Date reserveDate, double price, double deposit);
    List<Reservation> getHistoryReservation(String citizenId);
    List<String> getProvinces();
    List<String> getDistricts(String province);
    List<Van> getVans();

    // manager extension
    Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate);

    void editVan(Van van);
    void deleteVan(Van van);
    void deleteVan(String regisNumber);

    List<Partner> getPartners();
    void editPartner(Partner partner);
    void deletePartner(Partner partner);
    void deletePartner(int partnerId);

    List<Driver> getDriverAvailable(Date startDate, Date endDate);
    List<Driver> getDrivers();
    void editDriver(Driver driver);
    void deleteDriver(Driver driver);

    void deleteDriver(String citizenId);

    void editReservation(Reservation reservation);
    void deleteReservation(Reservation reservation);

    void deleteReservation(String reservationId);
    void assignVan(List<Van> vans, Reservation reservation);

    void assignDriver(List<Driver> drivers, Reservation reservation);
    List<JobType> getVanJobs();
    void addVanJob(Van van, Date startDate, Date endDate, JobType type);

    void deleteVanJob(Van van, Date startDate, Date endDate);
    List<JobType> getDriverJobs();
    void addDriverJob(Van van, Date startDate, Date endDate, JobType type);

    void deleteDriverJob(Van van, Date startDate, Date endDate);

    void addMeeting(String meetingPlace, Date meetingTime);
    void confirmDeposit(Reservation reservation, Date depositDate);

    void confirmDeposit(String reservationId, Date depositDate);
    List<Reservation> getReservations();

    Reservation getReservation(String reserveId);
}
