package com.org.spemajorbackend.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.spemajorbackend.dro.AddMenuRequest;
import com.org.spemajorbackend.dro.UpdateMessDetails;
import com.org.spemajorbackend.dto.AmountBalancesResponse;
import com.org.spemajorbackend.entity.Mess;
import com.org.spemajorbackend.service.CalculatorService;
import com.org.spemajorbackend.service.MessService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {MessController.class})
@ExtendWith(SpringExtension.class)
class MessControllerTest {
    @MockBean
    private CalculatorService calculatorService;
    @Autowired
    private MessController messController;
    @MockBean
    private MessService messService;

    @Test
    void testRejectRequest() throws Exception {
        Mockito.<ResponseEntity<?>>when(messService.rejectRequest(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/mess/reject-request/{owner_id}/{customer_id}", "Owner id", "Customer id");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(messController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    @Test
    void testRejectRequest2() throws Exception {
        Mockito.<ResponseEntity<?>>when(messService.rejectRequest(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(messController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testAcceptRequest() throws Exception {
        Mockito.<ResponseEntity<?>>when(messService.acceptRequest(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/mess/accept-request/{owner_id}/{customer_id}", "Owner id", "Customer id");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(messController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    @Test
    void testAcceptRequest2() throws Exception {
        Mockito.<ResponseEntity<?>>when(messService.acceptRequest(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(messController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdateOwnerDetails() throws Exception {
        Mockito.<ResponseEntity<?>>when(messService.updateOwnerDetails(Mockito.<String>any(), Mockito.<UpdateMessDetails>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        UpdateMessDetails updateMessDetails = new UpdateMessDetails();
        updateMessDetails.setAddress("42 Main St");
        updateMessDetails.setBreakfast(true);
        updateMessDetails.setLatitude("Latitude");
        updateMessDetails.setLongitude("Longitude");
        updateMessDetails.setMessname("Messname");
        updateMessDetails.setPhone("6625550144");
        updateMessDetails.setPricing("Pricing");
        updateMessDetails.setService("Service");
        updateMessDetails.setTrial(true);
        updateMessDetails.setType("Type");
        String content = (new ObjectMapper()).writeValueAsString(updateMessDetails);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/mess/updateOwner-details/{owner_id}", "Owner id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(messController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }
    @Test
    void testSeeJoiningRequests() throws Exception {
        when(messService.seeJoiningRequests(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mess/requests/{owner_id}", "Owner id");
        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testSeeJoiningRequests2() throws Exception {
        when(messService.seeJoiningRequests(Mockito.<String>any()))
                .thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mess/requests/{owner_id}", "Owner id");
        requestBuilder.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testAddMenu() throws Exception {
        when(messService.addMenuItems(Mockito.<List<AddMenuRequest>>any(), Mockito.<String>any()))
                .thenReturn(true);

        AddMenuRequest addMenuRequest = new AddMenuRequest();
        addMenuRequest.setBreakfast("Breakfast");
        addMenuRequest.setDay("Day");
        addMenuRequest.setDinner("Dinner");
        addMenuRequest.setLunch("Lunch");

        ArrayList<AddMenuRequest> addMenuRequestList = new ArrayList<>();
        addMenuRequestList.add(addMenuRequest);
        String content = (new ObjectMapper()).writeValueAsString(addMenuRequestList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/mess/add-menu/{mess_owner_username}", "janedoe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Added Successfully"));
    }
    @Test
    void testAddMenu2() throws Exception {
        when(messService.addMenuItems(Mockito.<List<AddMenuRequest>>any(), Mockito.<String>any()))
                .thenReturn(false);

        AddMenuRequest addMenuRequest = new AddMenuRequest();
        addMenuRequest.setBreakfast("Breakfast");
        addMenuRequest.setDay("Day");
        addMenuRequest.setDinner("Dinner");
        addMenuRequest.setLunch("Lunch");

        ArrayList<AddMenuRequest> addMenuRequestList = new ArrayList<>();
        addMenuRequestList.add(addMenuRequest);
        String content = (new ObjectMapper()).writeValueAsString(addMenuRequestList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/mess/add-menu/{mess_owner_username}", "janedoe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(messController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(422));
    }
    @Test
    void testAmountBalance() throws Exception {
        when(calculatorService.calculateTotal(Mockito.<String>any())).thenReturn(new AmountBalancesResponse());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mess/total-balance/{owner_id}",
                "Owner id");
        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"paid\":null,\"remaining\":null,\"total\":null}"));
    }

    @Test
    void testAmountBalance2() throws Exception {
        when(calculatorService.calculateTotal(Mockito.<String>any())).thenReturn(new AmountBalancesResponse(2000.0, 500.0, 1500.0));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mess/total-balance/{owner_id}",
                "Owner id");
        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"paid\":500.0,\"remaining\":1500.0,\"total\":2000.0}"));
    }

    @Test
    void testAmountBalanceWhenException() throws Exception {
        when(calculatorService.calculateTotal(Mockito.<String>any()))
                .thenThrow(new UsernameNotFoundException("No mess exist owned by:ownerId"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mess/total-balance/{owner_id}",
                "ownerId");
        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("No mess exist owned by:ownerId"));
    }

    @Test
    void testSendOwnerDetails() throws Exception {
        Mockito.<ResponseEntity<?>>when(messService.getOwnerDetails(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mess/owner-details/{owner_id}",
                "Owner id");

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(messController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    @Test
    void testSendOwnerDetails2() throws Exception {
        Mockito.<ResponseEntity<?>>when(messService.getOwnerDetails(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(messController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testSendOwnerDetails3() throws Exception {

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
        mess.setPricing("2000");
        mess.setReviews(new ArrayList<>());
        mess.setService("Service");
        mess.setTrial(true);
        mess.setType("Type");
        mess.setUsername("OwnerId");

        ResponseEntity<?> res = ResponseEntity.ok().body(mess);
        Mockito.<ResponseEntity<?>>when(messService.getOwnerDetails("OwnerId"))
                .thenReturn(res);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mess/owner-details/{owner_id}",
                "OwnerId");

        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"username\":\"OwnerId\",\"firstname\":\"Jane\",\"lastname\":\"Doe\",\"phone\":"
                                        + "\"6625550144\",\"messname\":\"Messname\",\"address\":\"42 Main St\",\"latitude\":\"Latitude\",\"longitude\""
                                        + ":\"Longitude\",\"service\":\"Service\",\"type\":\"Type\",\"trial\":true,"
                                        + "\"breakfast\":true,\"aboutSundays\":\"About Sundays\",\"pricing\":\"2000\",\"customers\":[],\"menus\":[],\"reviews\":[]}"
                        ));
    }

    @Test()
    void TestSendOwnerDetailsUserNotFoundException() throws Exception {
        when(messService.getOwnerDetails("ownerId"))
                .thenThrow(new UsernameNotFoundException("Sorry no mess found owned by:ownerId"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mess/owner-details/{owner_id}", "ownerId")
                .contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Sorry no mess found owned by:ownerId"));
    }

    @Test()
    void TestUpdateOwnerDetailsUserNotFoundException() throws Exception {
        when(messService.updateOwnerDetails("ownerId", new UpdateMessDetails()))
                .thenThrow(new UsernameNotFoundException("Sorry no mess found owned by:ownerId"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/mess/updateOwner-details/{owner_id}", "ownerId")
                .contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
//                .andExpect(MockMvcResultMatchers.content().string("Sorry no mess found owned by:ownerId"));
    }

    @Test()
//    get instead of post
    void TestBadRequestException() throws Exception {
        when(messService.updateOwnerDetails("ownerId", new UpdateMessDetails()))
                .thenThrow(new UsernameNotFoundException("Sorry no mess found owned by:ownerId"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/mess/updateOwner-details/{owner_id}", "ownerId")
                .contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(messController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(405));
    }


}
