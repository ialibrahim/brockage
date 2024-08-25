package org.ialibrahim.brokerage.security;

import jakarta.annotation.PostConstruct;
import org.ialibrahim.brokerage.dao.CustomerRepository;
import org.ialibrahim.brokerage.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerUserInitializer {
    @Autowired
    private CustomerRepository customerRepository;

    @PostConstruct
    public void init() {
        CustomerEntity testCustomer = new CustomerEntity();
        testCustomer.setId(123L);
        testCustomer.setName("Ahmet");
        testCustomer.setSurname("Korkmaz");
        testCustomer.setUsername("customer1");
        testCustomer.setPassword("23828149e135e251fbe9eeeef635209f2e710a26eb92b3e4951741102d28ed5e725e4b36");
        customerRepository.save(testCustomer);
    }
}
