package org.example.serviceapplication.Cusotmer.service;


import org.example.serviceapplication.Cusotmer.Exception.NotFoundCustomer;
import org.example.serviceapplication.Cusotmer.model.Customer;
import org.example.serviceapplication.Cusotmer.repository.CustomerRepository;
import org.example.serviceapplication.enumPackage.Role;
import org.example.serviceapplication.enumPackage.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
/*
@ActiveProfiles("test")
@SpringBootTest
class CustomerServiceTest {
    @Autowired
    private CustomerService service;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void create_new_customer() {

        Customer newCustomer = getCustomer();
        service.createCustomer(newCustomer);
        assertNotNull(newCustomer.getId(), "Customer ID should not be null after saving");
        assertEquals("ali", newCustomer.getName(), "Customer name should match");
        assertEquals("ali@gmail.com", newCustomer.getEmail(), "Customer email should match");
        assertNotNull(newCustomer.getProfileImage(), "Customer profile image should not be null");
    }

    @Test
    void get_customer_by_id() {
        Customer customer = getCustomer();
        Customer customer1 = service.createCustomer(customer);
        Customer customerById = service.findById(customer1.getId());
        assertEquals(customerById.getId(), customer1.getId());
    }

    @Test
    void should_return_exception_if_customer_not_found() {
        Customer customer = getCustomer();
        service.createCustomer(customer);
        NotFoundCustomer notFoundCustomer = assertThrows(NotFoundCustomer.class, () -> service.findById(30L));
        assertEquals("Customer not found", notFoundCustomer.getMessage());

    }

    @Test
    void get_customer_by_Name() {
        Customer customer = getCustomer();
        Customer customer1 = service.createCustomer(customer);
        Customer customerByName = service.getWorkByName(customer1.getName());
        assertEquals(customerByName.getName(), customer1.getName());
    }


    @Test
    void should_return_all_works() {
        Customer customer1 = getCustomer();
        Customer customer2 = getCustomer();
        service.createCustomer(customer1);
        service.createCustomer(customer2);
        List<Customer> allCustomers = service.getAllCustomers();
        assertEquals(2, allCustomers.size());


    }

    @Test
    void update_customer() {
        Customer customer = getCustomer();
        Customer customer1 = service.createCustomer(customer);
        customer1.setName("asghar");
        service.createCustomer(customer1);
        assertEquals("asghar", customer1.getName());

    }


    private Customer getCustomer() {
        byte[] imageBytes = "dummy image content".getBytes(StandardCharsets.UTF_8);
        MultipartFile imageFile = new MockMultipartFile("image", "image.jpg",
                "image/jpeg", imageBytes);

        return new Customer("ali", "ali@gmail.com", "+989125478563",
                "3813", "123456", true, Role.Customer, Status.newJoiner,
                imageBytes);
    }


    @AfterEach
    void delete_table_After_each_test() {
        customerRepository.deleteAll();
    }

}
