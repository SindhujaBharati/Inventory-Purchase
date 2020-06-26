package com.cognizant.purchase.service;

import java.util.List;

import com.cognizant.purchase.dto.PurchaseOrderDto;
import com.cognizant.purchase.entity.PurchaseOrderEntity;
import com.cognizant.purchase.exception.ResourceNotFoundException;

public interface PurchaseOrderService {

	public PurchaseOrderEntity createOrder(PurchaseOrderDto purchaseOrderDto);

	public PurchaseOrderEntity createOrderWithoutPurchaseType(PurchaseOrderDto purchaseOrderDto);

	public List<PurchaseOrderEntity>  fetchAllPurchases();

	public PurchaseOrderEntity getPurchaseById(long purchaseId) throws ResourceNotFoundException;

	public PurchaseOrderEntity updatePurchase(long purchaseId, PurchaseOrderDto purchaseDetails) throws ResourceNotFoundException;

	public void deletePurchase(long purchaseId) throws ResourceNotFoundException;

}
