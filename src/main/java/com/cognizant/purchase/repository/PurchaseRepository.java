package com.cognizant.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.purchase.domain.PurchaseOrder;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseOrder,Long>{

}
