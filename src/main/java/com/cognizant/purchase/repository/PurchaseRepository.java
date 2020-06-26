package com.cognizant.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.purchase.entity.PurchaseOrderEntity;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseOrderEntity,Long>{

}
