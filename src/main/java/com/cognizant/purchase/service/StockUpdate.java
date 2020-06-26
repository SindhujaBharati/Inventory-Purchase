package com.cognizant.purchase.service;

import com.cognizant.purchase.dto.StockRequestDto;

public interface StockUpdate {

	void addStock(StockRequestDto stock);

	void updateStock(StockRequestDto stock, int stockCount, long stockId);

}
