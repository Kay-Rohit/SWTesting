package com.org.spemajorbackend.service;

import com.org.spemajorbackend.dro.AddMenuRequest;
import com.org.spemajorbackend.dto.CustomerProfileResponse;
import com.org.spemajorbackend.entity.*;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.Test;
import com.org.spemajorbackend.repository.ReviewRepository;

import java.util.List;

import com.org.spemajorbackend.repository.CustomerRepository;
import com.org.spemajorbackend.dro.ForgetPasswordRequest;
import com.org.spemajorbackend.repository.MenuRepository;
import com.org.spemajorbackend.dro.AddReviewRequest;
import com.org.spemajorbackend.repository.JoiningRequestRepository;
import com.org.spemajorbackend.repository.AuthMasterRepository;
import com.org.spemajorbackend.repository.MessRepository;

import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Disabled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CustomerService.class})
@ExtendWith(SpringExtension.class)
class CustomerServiceTest2 {
    @MockBean
    private  MessRepository messRepository;
    @MockBean
    private  MenuRepository menuRepository;
    @MockBean
    private  ReviewRepository reviewRepository;
    @MockBean
    private  CustomerRepository customerRepository;
    @MockBean
    private  AuthMasterRepository authMasterRepository;
    @MockBean
    private  JoiningRequestRepository joiningRequestRepository;

    @Autowired
    private CustomerService customerService;

//    @Disabled()
    @Test()
    void getMessListWhenMessesIsNotEmpty() {
        Mess mess = new Mess();
        mess.setAboutSundays("About Sundays");
        mess.setAddress("42 Main St");
        mess.setBreakfast(true);
        mess.setCustomers(new ArrayList<>());
        mess.setFirstname("Jane");
        mess.setLastname("Doe");
        mess.setLatitude("Latitude");
        mess.setLongitude("Longitude");
        mess.setMenus(new ArrayList<>());
        mess.setMessname("Messname");
        mess.setPhone("6625550144");
        mess.setPricing("Pricing");
        mess.setReviews(new ArrayList<>());
        mess.setService("Service");
        mess.setTrial(true);
        mess.setType("Type");
        mess.setUsername("janedoe");

        Mess mess2 = new Mess();
        mess2.setAboutSundays("About Sundays");
        mess2.setAddress("42 Main St");
        mess2.setBreakfast(true);
        mess2.setCustomers(new ArrayList<>());
        mess2.setFirstname("Jane");
        mess2.setLastname("Doe");
        mess2.setLatitude("Latitude");
        mess2.setLongitude("Longitude");
        mess2.setMenus(new ArrayList<>());
        mess2.setMessname("Messname");
        mess2.setPhone("6625550144");
        mess2.setPricing("Pricing");
        mess2.setReviews(new ArrayList<>());
        mess2.setService("Service");
        mess2.setTrial(true);
        mess2.setType("Type");
        mess2.setUsername("janedoe");

        List<Mess> list = new ArrayList<>();
        list.add(mess);
        list.add(mess2);

        doReturn(list).when(messRepository).findAll();
        List<Menu> mlist= new ArrayList<>();
        doReturn(mlist).when(menuRepository).findByMess_Username("ownerId");
        List<Review> rlist = new ArrayList<>();
        doReturn(rlist).when(reviewRepository).findByMess_Username("ownerId");

        List<Mess> result = customerService.getMessList();
        assertEquals(2, result.size());

        for(Mess m: result) {
            assertEquals(0, m.getMenus().size());
            assertEquals(0, m.getReviews().size());
            assertNull(m.getCustomers());
        }
//        verify(messRepository.findAll());
//        verify(reviewRepository.findByMess_Username("ownerId"));
//        verify(menuRepository.findByMess_Username("ownerId"));
    }


    @Test()
    void sendJoinRequestTest() throws Exception {
        Customer customerMock = mock(Customer.class);
        doReturn(Optional.of(customerMock)).when(customerRepository).findById("customerId");
        doReturn(Optional.empty()).when(messRepository).findById("ownerId");

        boolean result = customerService.sendJoinRequest("customerId", "ownerId");

        assertAll("result", () -> {
            assertThat(result, equalTo(Boolean.FALSE));
            verify(customerRepository).findById("customerId");
            verify(messRepository).findById("ownerId");
        });
    }

    @Disabled
    @Test
    void TestSendJoinRequestUsernameNotFoundException(){
        Customer customerMock = mock(Customer.class);
        doReturn(Optional.of(customerMock)).when(customerRepository).findById("customerId");
        Optional<Mess> emptyResult = Optional.empty();
        when(messRepository.findById("ownerId")).thenReturn(emptyResult);

        assertThrows(UsernameNotFoundException.class, () -> customerService.sendJoinRequest("customerId", "ownerId"));
        verify(messRepository).findById("ownerId");
        verify(customerRepository).findById("customerId");
    }

    @Test()
    void addReviewThrowsUsernameNotFoundException() {
        doReturn(Optional.empty()).when(messRepository).findById("ownerId");
        AddReviewRequest addReviewRequest = new AddReviewRequest();
        addReviewRequest.setMess_owner_username("ownerId");
        final UsernameNotFoundException result = assertThrows(UsernameNotFoundException.class, () -> {
            customerService.addReview(addReviewRequest);
        });
        assertAll("result", () -> {
            assertThat(result, is(notNullValue()));
            verify(messRepository).findById("ownerId");
        });
    }

    @Test
    void TestAddReviewWhenListIsNotEmpty()
    {
        Customer customerMock = mock(Customer.class);
        Mess messMock = mock(Mess.class);
        Review review = mock(Review.class);
        AddReviewRequest addReviewRequest = mock(AddReviewRequest.class);

        List<Review> rlist = new ArrayList<>();
        rlist.add(review);

        when(messRepository.findById(Mockito.any())).thenReturn(Optional.of(messMock));
        when(customerRepository.findById(Mockito.any())).thenReturn(Optional.of(customerMock));
        when(reviewRepository.findByCustomer_UsernameAndMess_Username(Mockito.any(), Mockito.any()))
                .thenReturn(rlist);

        when(reviewRepository.updateReviewByMessAndCustomerUsernames(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1);

        assertEquals(Boolean.TRUE, customerService.addReview(addReviewRequest));

//        verify(messRepository).findById(Mockito.any());
//        verify(customerRepository.findById(Mockito.any()));
//        verify(reviewRepository).findByCustomer_UsernameAndMess_Username(Mockito.any(), Mockito.any());
//        verify(reviewRepository.updateReviewByMessAndCustomerUsernames(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()));
    }
    @Test()
    void getProfileThrowsUsernameNotFoundException() {
        doReturn(Optional.empty()).when(customerRepository).findById("customerId");
        final UsernameNotFoundException result = assertThrows(UsernameNotFoundException.class, () -> {
            customerService.getProfile("customerId");
        });
        assertAll("result", () -> {
            assertThat(result, is(notNullValue()));
            verify(customerRepository).findById("customerId");
        });
    }

//    @Disabled
    @Test()
    void getProfileWhenMessIsEmpty() {
        Customer customer = new Customer();
        customer.setEmail("jane.doe@example.org");
        customer.setFirstname("Jane");
        customer.setLastname("Doe");
        customer.setMess(ArgumentMatchers.<Mess>notNull());
        customer.setPhone("6625550144");
        customer.setUsername("customerId");

        customer.setMess(null);
        when(customerRepository.findById("customerId")).thenReturn(Optional.of(customer));

        CustomerProfileResponse result = customerService.getProfile("customerId");
        assertNull(result.getMessname());
        verify(customerRepository).findById("customerId");
//        only used inside if
//        verify(messRepository).findById("messId");

        assertEquals("6625550144", result.getPhone());
        assertEquals("Doe", result.getLastname());
        assertEquals("Jane", result.getFirstname());
        assertEquals("jane.doe@example.org", result.getEmail());
        assertEquals("customerId", result.getUsername());
    }

    @Test()
    void TestResetPasswordThrowsUsernameNotFoundException() {
        doReturn(Optional.empty()).when(authMasterRepository).findById("id");
        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
        forgetPasswordRequest.setUsername("id");

        assertThrows(UsernameNotFoundException.class, ()->customerService.resetPassword(forgetPasswordRequest));
        verify(authMasterRepository).findById("id");
    }

    @Test
    void TestResetPassword(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
        forgetPasswordRequest.setUsername("customerid");
        forgetPasswordRequest.setOldPassword("123");
        forgetPasswordRequest.setNewPassword("456");

        AuthMaster user = new AuthMaster();
        user.setPassword(passwordEncoder.encode("123"));
        user.setUsername("customerid");
        user.setRole("customer");
        Optional<AuthMaster> ofResult = Optional.of(user);

        when(authMasterRepository.findById("customerid")).thenReturn(ofResult);

        assertTrue(passwordEncoder.matches("123", user.getPassword()));
        assertTrue(customerService.resetPassword(forgetPasswordRequest));

        verify(authMasterRepository).findById("customerid");
    }
}
