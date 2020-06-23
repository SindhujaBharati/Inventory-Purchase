package com.cognizant.purchase.service;

import com.cognizant.purchase.domain.Stock;

public interface StockUpdate {
	
	void addStock(Stock stock);
	
	void updateStock(Stock stock,int stockCount,long  stockId);
		
}
