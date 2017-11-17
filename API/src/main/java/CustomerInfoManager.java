public class CustomerInfoManager {
    private static CustomerInfoManager customerInfoManager;
    private Customer customer;

    private CustomerInfoManager() {
    }

    public static CustomerInfoManager getInstance(){
        if (customerInfoManager == null){
            customerInfoManager = new CustomerInfoManager();
        }
        return customerInfoManager;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
