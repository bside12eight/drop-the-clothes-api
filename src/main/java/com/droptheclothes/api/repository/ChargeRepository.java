package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Charge;
import com.droptheclothes.api.model.entity.pk.ChargeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge, ChargeId>  {

}
