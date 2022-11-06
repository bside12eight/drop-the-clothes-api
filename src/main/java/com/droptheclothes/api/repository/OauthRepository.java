package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthRepository extends JpaRepository<Oauth, Long> {

}
