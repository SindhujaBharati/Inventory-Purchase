package com.cognizant.purchase.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cognizant.purchase.domain.Stock;
import com.cognizant.purchase.service.StockUpdate;

@Service
public class StockUpdateImpl implements StockUpdate{
	private static final String HOST = "localhost";
	private static final String SCHEME = "http";
	private int stockPort=8081;
	
	@Autowired
	private Stock stock;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void addStock(Stock stockRequest) {
		
		String url = SCHEME + "://" + HOST + ":" + stockPort + "/" + "cognizant/stock/addStock/v2";
     
		stock.setStockName(stockRequest.getStockName());
		stock.setStockType(stockRequest.getStockType());
		stock.setStockCount(stockRequest.getStockCount());

	    Stock result = restTemplate.postForObject( url, stock, Stock.class);
	 
	    System.out.println(result);
	}

	@Override
	public void updateStock(Stock stockRequest, int stockCount,long  stockId) {
		
		String url = SCHEME + "://" + HOST + ":" + stockPort + "/" + "cognizant/stock/updateStock/v2/" + stockId;
	     
	    Map<String, Long> params = new HashMap<String, Long>();
	    params.put("id", stockId);
	    
	    stock.setStockName(stockRequest.getStockName());
		stock.setStockType(stockRequest.getStockType());
		stock.setStockCount(stockRequest.getStockCount()+stockCount);

	    restTemplate.put ( url, stock, params);
		
	}

	
	
}
