package com.cognizant.purchase.dto;

import java.io.Serializable;

public class PurchaseOrderDto implements Serializable{
	private static final long serialVersionUID = 7385325066515149619L;

	private long purchaseId;

	private String purchaseName;

	private String purchaseType;

	private int purchaseCount;

	private float purchaseAmtperCount;

	private float purchaseAmtTotal;

	public long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getPurchaseName() {
		return purchaseName;
	}

	public void setPurchaseName(String purchaseName) {
		this.purchaseName = purchaseName;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public int getPurchaseCount() {
		return purchaseCount;
	}

	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}

	public float getpurchaseAmtperCount() {
		return purchaseAmtperCount;
	}

	public void setpurchaseAmtperCount(float purchaseAmtperCount) {
		this.purchaseAmtperCount = purchaseAmtperCount;
	}

	public float getpurchaseAmtTotal() {
		return purchaseAmtTotal;
	}

	public void setpurchaseAmtTotal(float purchaseAmtTotal) {
		this.purchaseAmtTotal = purchaseAmtTotal;
	}

	@Override
	public String toString() {
		return "PurchaseOrder [purchaseId=" + purchaseId + ", purchaseName=" + purchaseName + ", purchaseType="
				+ purchaseType + ", purchaseCount=" + purchaseCount + ", purchaseAmtperCount=" + purchaseAmtperCount
				+ ", purchaseAmtTotal=" + purchaseAmtTotal + "]";
	}
}
