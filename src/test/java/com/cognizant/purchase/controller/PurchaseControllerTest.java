package com.cognizant.purchase.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.cognizant.purchase.dto.PurchaseOrderDto;
import com.cognizant.purchase.entity.PurchaseOrderEntity;
import com.cognizant.purchase.service.PurchaseOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(PurchaseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PurchaseControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	WebApplicationContext webApplicationContext;
	@MockBean
	private PurchaseOrderService puchaseOrderService;
	private ObjectMapper objectMapper = new ObjectMapper();
	private String purchaseOrderRequest;

	@Before
	public void setUp() throws JsonProcessingException {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setpurchaseAmtperCount(1);
		purchaseOrderDto.setpurchaseAmtTotal(10);
		purchaseOrderDto.setPurchaseCount(1);
		purchaseOrderDto.setPurchaseId(1);
		purchaseOrderDto.setPurchaseName("testPurchase");
		purchaseOrderDto.setPurchaseType("TestType");
		purchaseOrderRequest = objectMapper.writeValueAsString(purchaseOrderDto);
		PurchaseOrderEntity purchaseOrderEntity = new PurchaseOrderEntity();
		Mockito.when(puchaseOrderService.createOrder(Mockito.any())).thenReturn(purchaseOrderEntity);

	}

	@Test
	public void createOrderTest() throws Exception {

		this.mockMvc.perform(post("/cognizant/purchase/addPurchase/v1").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(purchaseOrderRequest)).andExpect(status().isOk());
	}

	@Test
	public void createOrderWithoutPurchaseType() throws Exception {

		this.mockMvc.perform(post("/cognizant/purchase/addPurchase/v2").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(purchaseOrderRequest)).andExpect(status().isOk());
	}

	@Test
	public void getAllPurchasesTest() throws Exception {
		this.mockMvc.perform(get("/cognizant/purchase/getPurchase/v1")).andExpect(status().isOk());
	}
	
	
}
