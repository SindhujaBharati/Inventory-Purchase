package com.cognizant.purchase.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.*;

import com.cognizant.purchase.domain.PurchaseOrder;
import com.cognizant.purchase.domain.Stock;
import com.cognizant.purchase.exception.ResourceNotFoundException;
import com.cognizant.purchase.repository.PurchaseRepository;
import com.cognizant.purchase.service.StockUpdate;


@RestController
@RequestMapping("/cognizant/purchase")
@Api(value = "Purchase Management", description = "Operations pertaining to Purchase in Inventory Management System")
public class PurchaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
	
    @Autowired
    private RestTemplate restTemplate;
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private PurchaseOrder purchaseOrder;
	
	@Autowired
	private StockUpdate stockUpdate;
	
	@Autowired
	private Stock stock;
		
	final String  defaultPurchaseType="Not Applicable";
	
	private static final String HOST = "localhost";
	private static final String SCHEME = "http";
	private int stockPort=8086;
	private String stockName;
	private long stockId;
	private int stockCount;	
	private Stock stockResult;
	
	@ApiOperation(value = "Add a purchase",response=PurchaseOrder.class)
	@ApiResponses(value = {
		        @ApiResponse(code = 200, message = "Successfully created Purchase Order"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
		    })
	@PostMapping("/addPurchase/v1")
    public PurchaseOrder createOrder(@RequestBody PurchaseOrder Order) {
		
		logger.debug("PurchaseController::createOrder::entry()");
	
		purchaseOrder.setPurchaseName(Order.getPurchaseName());
		purchaseOrder.setPurchaseType(Order.getPurchaseType());
		purchaseOrder.setPurchaseCount(Order.getPurchaseCount());
		purchaseOrder.setpurchaseAmtperCount(Order.getpurchaseAmtperCount());
		purchaseOrder.setpurchaseAmtTotal(Order.getpurchaseAmtperCount()*Order.getPurchaseCount());
		
		stockName=Order.getPurchaseName();
		
		String url = SCHEME + "://" + HOST + ":" + stockPort + "/" + "cognizant/stock/getstock/v3/" + stockName;
        
        stock.setStockName(Order.getPurchaseName());
        stock.setStockType(Order.getPurchaseType());
        stock.setStockCount(Order.getPurchaseCount());
        
        stockResult=restTemplate.getForObject(url, Stock.class);
        
        if(stockResult==null) {        	          
            stockUpdate.addStock(stock);
       }
        else {        	
        	stockCount=stockResult.getStockCount();
        	stockId=stockResult.getStockId();
        	stockUpdate.updateStock(stock,stockCount,stockId);
        }
        
		logger.debug("PurchaseController::createOrder::exit()");
		
        return purchaseRepository.save(purchaseOrder);
        
    }
	
	@ApiOperation(value = "Add a purchase where type is NA",response=PurchaseOrder.class)
	@ApiResponses(value = {
		        @ApiResponse(code = 200, message = "Successfully created Purchase Order Without Purchase Type"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
		    })
	@PostMapping("/addPurchase/v2")
    public PurchaseOrder createOrderWithoutPurchaseType(@RequestBody PurchaseOrder Order) {
		
		logger.debug("PurchaseController::createOrderWithoutPurchaseType::entry()");
	
		purchaseOrder.setPurchaseName(Order.getPurchaseName());
		purchaseOrder.setPurchaseType(defaultPurchaseType);
		purchaseOrder.setPurchaseCount(Order.getPurchaseCount());
		purchaseOrder.setpurchaseAmtperCount(Order.getpurchaseAmtperCount());
		purchaseOrder.setpurchaseAmtTotal(Order.getpurchaseAmtperCount()*Order.getPurchaseCount());
		
		logger.debug("PurchaseController::createOrderWithoutPurchaseType::exit()");
		
        return purchaseRepository.save(purchaseOrder);
        
    }
		
	@ApiOperation(value = "View a list of Purchases done", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved Purchase list"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })	
	@GetMapping("/getPurchase/v1")
	@Cacheable(value = "purchase")
    public List<PurchaseOrder> getAllPurchase() {
		
		logger.debug("PurchaseController::getAllPurchase::entry()");
		
		logger.debug("PurchaseController::getAllPurchase::exit()");
		
        return purchaseRepository.findAll();
    }
	
	
	@ApiOperation(value = "Get a purchase by Id")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully got Purchase Order By Id"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@GetMapping("/getPurchase/v2/{id}")
	@Cacheable(value = "purchase", key = "#purchaseId")
    public ResponseEntity<PurchaseOrder> getPurchaseById(@PathVariable(value = "id") Long purchaseId)
        throws ResourceNotFoundException {
		
		logger.debug("PurchaseController::getPurchaseById v2::entry()");
		
		PurchaseOrder purchase = purchaseRepository.findById(purchaseId)
          .orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + purchaseId));
		
		logger.debug("PurchaseController::getPurchaseById v2::exit()");
		
        return ResponseEntity.ok().body(purchase);
    }	
	
	@ApiOperation(value = "Update a purchase")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully updated Purchase Order"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@PutMapping("/updatePurchase/v1/{id}")
	@CachePut(value = "purchase", key = "#purchaseId")
    public ResponseEntity<PurchaseOrder> updatePurchase(@PathVariable(value = "id") Long purchaseId,
         @RequestBody PurchaseOrder purchaseDetails) throws ResourceNotFoundException {
		
		logger.debug("PurchaseController::updatePurchase::entry()");
		
		PurchaseOrder purchase = purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + purchaseId));
		
		purchase.setPurchaseName(purchaseDetails.getPurchaseName());
		purchase.setPurchaseType(purchaseDetails.getPurchaseType());
		purchase.setPurchaseCount(purchaseDetails.getPurchaseCount());
		purchase.setpurchaseAmtperCount(purchaseDetails.getpurchaseAmtperCount());
		purchase.setpurchaseAmtTotal(purchaseDetails.getpurchaseAmtperCount()*purchaseDetails.getPurchaseCount());
		
		final PurchaseOrder updatedPurchase = purchaseRepository.save(purchase);
		
		logger.debug("PurchaseController::updatePurchase::exit()");
		
        return ResponseEntity.ok(updatedPurchase);
    }
	

	@ApiOperation(value = "Delete a purchase")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully deleted Purchase Order"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@DeleteMapping("/deletePurchase/v1/{id}")
	@CacheEvict(value = "purchases", allEntries=true)
    public Map<String, Boolean> deletePurchase(@PathVariable(value = "id") Long purchaseId)
         throws ResourceNotFoundException {
		
		logger.debug("PurchaseController::deletePurchase::entry()::id");
		
		PurchaseOrder purchase = purchaseRepository.findById(purchaseId)
       .orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + purchaseId));

		purchaseRepository.delete(purchase);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        
        logger.debug("PurchaseController::deletePurchase::exit()");
        
        return response;
    }
}
