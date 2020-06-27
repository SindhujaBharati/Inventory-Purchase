package com.cognizant.purchase.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.purchase.dto.PurchaseOrderDto;
import com.cognizant.purchase.entity.PurchaseOrderEntity;
import com.cognizant.purchase.exception.ResourceNotFoundException;
import com.cognizant.purchase.service.PurchaseOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.springframework.stereotype.Component;
import java.util.function.Function;
@RestController
@RequestMapping("/cognizant/purchase")
@Api(value = "Purchase Management", description = "Operations pertaining to Purchase in Inventory Management System")
public class PurchaseController  implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
	@Autowired
	private PurchaseOrderService puchaseOrderService;

	@Override
	public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent t) {
		 	APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
	        responseEvent.setStatusCode(200);
	        responseEvent.setBody("Hello! Reached the Purchase Innventory Management:");
	        return responseEvent;
		
	}
	@ApiOperation(value = "Add a purchase", response = PurchaseOrderEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created Purchase Order"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PostMapping("/addPurchase/v1")
	public PurchaseOrderEntity createOrder(@RequestBody PurchaseOrderDto purchaseOrderDto) {
		return puchaseOrderService.createOrder(purchaseOrderDto);

	}

	@ApiOperation(value = "Add a purchase where type is NA", response = PurchaseOrderEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully created Purchase Order Without Purchase Type"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PostMapping("/addPurchase/v2")
	public PurchaseOrderEntity createOrderWithoutPurchaseType(@RequestBody PurchaseOrderDto purchaseOrderDto) {
		return puchaseOrderService.createOrderWithoutPurchaseType(purchaseOrderDto);

	}

	@ApiOperation(value = "View a list of Purchases done", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Purchase list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/getPurchase/v1")
	//@Cacheable(value = "purchase")
	public List<PurchaseOrderEntity> getAllPurchase() {
		return puchaseOrderService.fetchAllPurchases();
	}

	@ApiOperation(value = "Get a purchase by Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully got Purchase Order By Id"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/getPurchase/v2/{id}")
	//@Cacheable(value = "purchase", key = "#purchaseId")
	public ResponseEntity<PurchaseOrderEntity> getPurchaseById(@PathVariable(value = "id") Long purchaseId)
			throws ResourceNotFoundException {
		PurchaseOrderEntity purchase = puchaseOrderService.getPurchaseById(purchaseId);
		return ResponseEntity.ok().body(purchase);
	}

	@ApiOperation(value = "Update a purchase")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated Purchase Order"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/updatePurchase/v1/{id}")
	//@CachePut(value = "purchase", key = "#purchaseId")
	public ResponseEntity<PurchaseOrderEntity> updatePurchase(@PathVariable(value = "id") Long purchaseId,
			@RequestBody PurchaseOrderDto purchaseDetails) throws ResourceNotFoundException {
		final PurchaseOrderEntity updatedPurchase = puchaseOrderService.updatePurchase(purchaseId, purchaseDetails);
		return ResponseEntity.ok(updatedPurchase);
	}

	@ApiOperation(value = "Delete a purchase")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully deleted Purchase Order"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@DeleteMapping("/deletePurchase/v1/{id}")
	//@CacheEvict(value = "purchases", allEntries = true)
	public Map<String, Boolean> deletePurchase(@PathVariable(value = "id") Long purchaseId)
			throws ResourceNotFoundException {
		puchaseOrderService.deletePurchase(purchaseId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	
}
