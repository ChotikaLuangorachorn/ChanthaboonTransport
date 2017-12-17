package controllers.services;

import models.Destination;
import models.PriceFactor;
import models.Van;
import services.PriceService;
import services.VanService;

import java.util.*;

public class PriceSQLiteService extends SQLiteService implements PriceService{
    public PriceSQLiteService(String url) {
        super(url);
    }

    @Override
    public PriceFactor getPriceFactor() {
        String sql = "select * from price_rate";
        return executeQuery(sql, resultSet -> {
            PriceFactor factor = new PriceFactor();
            while(resultSet.next()){
                String rtype = resultSet.getString("reserve_type");
                String vtype = resultSet.getString("van_type");
                double rate = resultSet.getDouble("rate");
                double base = resultSet.getDouble("base");
                double free = resultSet.getDouble("free_range");

                if ("day".equals(rtype)){
                    if ("VIP".equals(vtype)){
                        factor.setFactor(PriceFactor.DAY, PriceFactor.VIP, PriceFactor.RATE, rate);
                        factor.setFactor(PriceFactor.DAY, PriceFactor.VIP, PriceFactor.BASE, base);
                        factor.setFactor(PriceFactor.DAY, PriceFactor.VIP, PriceFactor.FREE, free);
                    }else{
                        factor.setFactor(PriceFactor.DAY, PriceFactor.NORMAL, PriceFactor.RATE, rate);
                        factor.setFactor(PriceFactor.DAY, PriceFactor.NORMAL, PriceFactor.BASE, base);
                        factor.setFactor(PriceFactor.DAY, PriceFactor.NORMAL, PriceFactor.FREE, free);
                    }
                }else{
                    if ("VIP".equals(vtype)){
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.VIP, PriceFactor.RATE, rate);
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.VIP, PriceFactor.BASE, base);
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.VIP, PriceFactor.FREE, free);
                    }else{
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.NORMAL, PriceFactor.RATE, rate);
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.NORMAL, PriceFactor.BASE, base);
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.NORMAL, PriceFactor.FREE, free);
                    }
                }
            }
            return factor;
        }, null);
    }

    @Override
    public boolean updatePriceFactor(PriceFactor factor) {
        System.out.println("request update price factor");
        String[] rtypes = {"day", "distance"};
        String[] vtype = {"VIP", "NORMAL"};
        boolean isSuccess = true;
        for (int i=0; i<2; i++){
            for (int j=0; j<2; j++){
                double base = factor.getFactor(i+1, (j+1)*10, 100);
                double rate = factor.getFactor(i+1, (j+1)*10, 200);
                double free = factor.getFactor(i+1, (j+1)*10, 300);
                String sql = createUpdatePriceFactorQuery(rtypes[i], vtype[j], base, rate, free);
                System.out.println(sql);
                int result = executeUpdate(sql);
                isSuccess = isSuccess && (result > 0);
            }
        }
        return isSuccess;
    }

    @Override
    public Date getMinimumDate(Destination destination, Date startDate) {
        // TODO edit formula
        double distance = getDistance(destination);
        int days = (int) Math.ceil(distance/720);
        days = days - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    @Override
    public double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate) {
        System.out.println("request getPrice");
        System.out.println("params " + vanAmt + " startDate " + startDate + " endDate " + endDate);

        int vipAmt = vanAmt.get(VanService.VIP);
        int normalAmt = vanAmt.get(VanService.NORMAL);
        long diff = endDate.getTime() - startDate.getTime();
        int days = (int) diff / (24*60*60*1000);
        String sql = "select * from price_rate where price_rate.reserve_type = \"day\"";

        return executeQuery(sql, (resultSet -> {
            Map<String, Double> rate = new HashMap<String, Double>();
            Map<String, Double> base = new HashMap<String, Double>();
            Map<String, Double> freeRage = new HashMap<String, Double>();

            while (resultSet.next()){
                rate.put(resultSet.getString("van_type"), resultSet.getDouble("rate"));
                base.put(resultSet.getString("van_type"), resultSet.getDouble("base"));
                freeRage.put(resultSet.getString("van_type"), resultSet.getDouble("free_range"));
            }

            double normalPrice = base.get(VanService.NORMAL) + rate.get(VanService.NORMAL)*((days < freeRage.get(VanService.NORMAL))?0:(days-freeRage.get(VanService.NORMAL)));
            double vipPrice = base.get(VanService.VIP) + rate.get(VanService.VIP)*((days < freeRage.get(VanService.VIP))?0:(days-freeRage.get(VanService.VIP)));

            return normalPrice*normalAmt + vipPrice*vipAmt;
        }), 0.0);
    }

    @Override
    public double getPrice(Map<String, Integer> vanAmt, Destination destination) {
        System.out.println("request getPrice");
        System.out.println("params " + vanAmt + " destination " + destination);
        int vipAmt = vanAmt.get(VanService.VIP);
        int normalAmt = vanAmt.get(VanService.NORMAL);
        String sql = "select * from price_rate where price_rate.reserve_type = \"distance\"";

        return executeQuery(sql, resultSet -> {
            double distance = getDistance(destination);
            Map<String, Double> rate = new HashMap<String, Double>();
            Map<String, Double> base = new HashMap<String, Double>();
            Map<String, Double> freeRage = new HashMap<String, Double>();
            while (resultSet.next()){
                rate.put(resultSet.getString("van_type"), resultSet.getDouble("rate"));
                base.put(resultSet.getString("van_type"), resultSet.getDouble("base"));
                freeRage.put(resultSet.getString("van_type"), resultSet.getDouble("free_range"));
            }

            double normalPrice = base.get(VanService.NORMAL) + rate.get(VanService.NORMAL)*((distance < freeRage.get(VanService.NORMAL))?0:(distance-freeRage.get(VanService.NORMAL)));
            double vipPrice = base.get(VanService.VIP) + rate.get(VanService.VIP)*((distance < freeRage.get(VanService.VIP))?0:(distance-freeRage.get(VanService.VIP)));
            System.out.println("base = " + base);
            System.out.println("rate = " + rate);
            System.out.println("freeRage = " + freeRage);
            System.out.println("normalPrice = " + normalPrice);
            System.out.println("vipPrice = " + vipPrice);
            return normalPrice*normalAmt + vipPrice*vipAmt;
        }, 0.0);
    }

    @Override
    public List<String> getProvinces() {
        List<String> provinces = new ArrayList<>();
        String sql = "select distinct province from distance";
        return executeQuery(sql, resultSet -> {
            while(resultSet.next()){
                String province = resultSet.getString("province");
                provinces.add(province);
            }
            return provinces;
        }, provinces);
    }

    @Override
    public List<String> getDistricts(String province) {
        List<String> districts = new ArrayList<>();
        String sql = "select district from distance where province='" + province + "'";
        return executeQuery(sql, resultSet -> {
            while (resultSet.next()){
                String district = resultSet.getString("district");
                districts.add(district);
            }
            return districts;
        }, districts);
    }

    private String createUpdatePriceFactorQuery(String rtype, String vtype, double base, double rate,  double free){
        return String.format("update price_rate set rate='%f', base='%f', free_range='%f' where reserve_type='%s' and van_type='%s'",
                rate, base, free, rtype, vtype);
    }
    private double getDistance(Destination destination){
        String sql = "select distance from distance where province='" + destination.getProvince() + "' and district='" + destination.getDistrict() + "'";
        return executeQuery(sql, resultSet -> {
            if (resultSet.next())
                return resultSet.getDouble("distance");
            return 0.0;
        },0.0);
    }
}

