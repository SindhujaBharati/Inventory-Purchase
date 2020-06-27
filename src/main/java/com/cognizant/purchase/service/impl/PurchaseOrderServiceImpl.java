package com.cognizant.purchase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cognizant.purchase.dto.PurchaseOrderDto;
import com.cognizant.purchase.dto.StockRequestDto;
import com.cognizant.purchase.entity.PurchaseOrderEntity;
import com.cognizant.purchase.entity.Stock;
import com.cognizant.purchase.exception.ResourceNotFoundException;
import com.cognizant.purchase.repository.PurchaseRepository;
import com.cognizant.purchase.service.PurchaseOrderService;
import com.cognizant.purchase.service.StockUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

	private static final String HOST = "stockmanangement-env.eba-bxgxm6dm.us-east-1.elasticbeanstalk.com";
	private static final String SCHEME = "http";
	private static final int STOCK_PORT = 5000;

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private StockUpdate stockUpdate;
	@Autowired
	private PurchaseRepository purchaseRepository;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public PurchaseOrderEntity createOrder(PurchaseOrderDto purchaseOrderDto) {
		
		logger.debug("PurchaseController::createOrder::entry()");
		
		String stockName = purchaseOrderDto.getPurchaseName();
		String stockType = purchaseOrderDto.getPurchaseType();

		String url = SCHEME + "://" + HOST + ":" + STOCK_PORT + "/" + "cognizant/stock/getstock/v3/" + stockName;
		Stock stockResult = restTemplate.getForObject(url, Stock.class);

		if (stockResult == null) {
			StockRequestDto stockRequest = new StockRequestDto();

			stockRequest.setStockName(purchaseOrderDto.getPurchaseName());
			stockRequest.setStockType(purchaseOrderDto.getPurchaseType());
			stockRequest.setStockCount(purchaseOrderDto.getPurchaseCount());
			stockUpdate.addStock(stockRequest);
		} else if(stockResult.getStockName().equals(stockName) && stockResult.getStockType().equals(stockType)){
			StockRequestDto stockRequest = new StockRequestDto();

			stockRequest.setStockName(purchaseOrderDto.getPurchaseName());
			stockRequest.setStockType(purchaseOrderDto.getPurchaseType());
			stockRequest.setStockCount(stockResult.getStockCount()+purchaseOrderDto.getPurchaseCount());
			//int stockCount = stockResult.getStockCount()+purchaseOrderDto.getPurchaseCount();
			//long stockId = stockResult.getStockId();
			//stockUpdate.updateStock(stockRequest, stockId);
			//int stockCount = stockResult.getStockCount()+purchaseOrderDto.getPurchaseCount();
			//long stockId = stockResult.getStockId();
			//stockUpdate.updateStock(stockRequest, stockId);
			
			//sending to SQS
			try {
				jmsTemplate.convertAndSend(objectMapper.writeValueAsString(stockRequest));
			} catch (JmsException | JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}

		logger.debug("PurchaseController::createOrder::exit()");
		PurchaseOrderEntity purchaseOrderEntity = new PurchaseOrderEntity();
		purchaseOrderEntity.setpurchaseAmtperCount(purchaseOrderDto.getpurchaseAmtperCount());
		purchaseOrderEntity.setpurchaseAmtTotal(purchaseOrderDto.getpurchaseAmtTotal());
		purchaseOrderEntity.setPurchaseCount(purchaseOrderDto.getPurchaseCount());
		purchaseOrderEntity.setPurchaseId(purchaseOrderDto.getPurchaseId());
		purchaseOrderEntity.setPurchaseName(purchaseOrderDto.getPurchaseName());
		purchaseOrderEntity.setPurchaseType(purchaseOrderDto.getPurchaseType());

		return purchaseRepository.save(purchaseOrderEntity);
	}

	@Override
	public PurchaseOrderEntity createOrderWithoutPurchaseType(PurchaseOrderDto purchaseOrderDto) {
		PurchaseOrderEntity purchaseOrderEntity = new PurchaseOrderEntity();
		purchaseOrderEntity.setPurchaseName(purchaseOrderDto.getPurchaseName());
		purchaseOrderEntity.setPurchaseType("Not Applicable");
		purchaseOrderEntity.setPurchaseCount(purchaseOrderDto.getPurchaseCount());
		purchaseOrderEntity.setpurchaseAmtperCount(purchaseOrderDto.getpurchaseAmtperCount());
		purchaseOrderEntity
				.setpurchaseAmtTotal(purchaseOrderDto.getpurchaseAmtperCount() * purchaseOrderDto.getPurchaseCount());
		return purchaseRepository.save(purchaseOrderEntity);
	}

	@Override
	public List<PurchaseOrderEntity> fetchAllPurchases() {
		return purchaseRepository.findAll();
	}

	@Override
	public PurchaseOrderEntity getPurchaseById(long purchaseId) throws ResourceNotFoundException {
		return purchaseRepository.findById(purchaseId)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + purchaseId));

	}

	@Override
	public PurchaseOrderEntity updatePurchase(long purchaseId, PurchaseOrderDto purchaseDetails)
			throws ResourceNotFoundException {
		PurchaseOrderEntity purchase = purchaseRepository.findById(purchaseId)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + purchaseId));
		purchase.setPurchaseName(purchaseDetails.getPurchaseName());
		purchase.setPurchaseType(purchaseDetails.getPurchaseType());
		purchase.setPurchaseCount(purchaseDetails.getPurchaseCount());
		purchase.setpurchaseAmtperCount(purchaseDetails.getpurchaseAmtperCount());
		purchase.setpurchaseAmtTotal(purchaseDetails.getpurchaseAmtperCount() * purchaseDetails.getPurchaseCount());

		return purchaseRepository.save(purchase);
	}

	@Override
	public void deletePurchase(long purchaseId) throws ResourceNotFoundException {
		PurchaseOrderEntity purchase = purchaseRepository.findById(purchaseId)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + purchaseId));
		purchaseRepository.delete(purchase);
	}

}
