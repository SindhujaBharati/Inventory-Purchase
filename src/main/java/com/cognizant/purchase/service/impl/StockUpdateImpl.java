package com.cognizant.purchase.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cognizant.purchase.dto.StockRequestDto;
import com.cognizant.purchase.entity.Stock;
import com.cognizant.purchase.service.StockUpdate;

@Service
public class StockUpdateImpl implements StockUpdate {
	private static final String HOST = "stockmanangement-env.eba-bxgxm6dm.us-east-1.elasticbeanstalk.com";
	private static final String SCHEME = "http";
	private int stockPort = 5000;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void addStock(StockRequestDto stockRequest) {

		String url = SCHEME + "://" + HOST + ":" + stockPort + "/" + "cognizant/stock/addStock/v2";
		StockRequestDto result = restTemplate.postForObject(url, stockRequest, StockRequestDto.class);
		System.out.println(result);
	}

	@Override
	public void updateStock(StockRequestDto stockRequest, long stockId) {

		String url = SCHEME + "://" + HOST + ":" + stockPort + "/" + "cognizant/stock/updateStock/v2/" + stockId;

		Map<String, Long> params = new HashMap<>();
		params.put("id", stockId);

		restTemplate.put(url, stockRequest, params);

	}

}
