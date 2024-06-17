package service;

import model.Customer;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class CustomerService {

    public static CustomerService customerService = new CustomerService();
    private final Map<String, Customer> customers = new HashMap<>();

    public CustomerService() {}
    public static CustomerService getCustomerService() {
        return customerService;
    }

public void addCustomer(String email, String firstName, String lastName) {
        customers.put(email, new Customer(email, firstName, lastName));
    }

    public Customer getCustomer(String email) {
        return customers.get(email);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
