package com.cognizant.purchase.entity;

import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "Purchase")
@ApiModel(description = "All details about the Purchase. ")
public class PurchaseOrderEntity {
	
	public PurchaseOrderEntity() {

	}
	public PurchaseOrderEntity(long purchaseId, String purchaseName,
			String purchaseType, int purchaseCount,
			float purchaseAmtperCount, float purchaseAmtTotal) {
		super();
		this.purchaseId = purchaseId;
		this.purchaseName = purchaseName;
		this.purchaseType = purchaseType;
		this.purchaseCount = purchaseCount;
		this.purchaseAmtperCount = purchaseAmtperCount;
		this.purchaseAmtTotal = purchaseAmtTotal;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long purchaseId;
	
	@Column(name = "purchase_name", nullable = false)
	private String purchaseName;
	
	@Column(name = "purchase_type", nullable = false)
	private String purchaseType;
	
	@Column(name = "purchase_count", nullable = false)
	private int purchaseCount;
	
	@Column(name = "purchase_amtPerCount", nullable = false)
	private float purchaseAmtperCount;

	@Column(name = "purchase_amtTotal", nullable = false)
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
		return "PurchaseOrder [purchaseId=" + purchaseId + ", purchaseName="
				+ purchaseName + ", purchaseType=" + purchaseType
				+ ", purchaseCount=" + purchaseCount
				+ ", purchaseAmtperCount=" + purchaseAmtperCount
				+ ", purchaseAmtTotal=" + purchaseAmtTotal + "]";
	}
	
	
}
