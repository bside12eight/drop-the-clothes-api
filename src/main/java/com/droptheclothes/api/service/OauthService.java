package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.OauthInfoRequest;
import com.droptheclothes.api.repository.OauthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OauthService {

  private final OauthRepository oauthRepository;

  /**
   * 프론트에서 받은 AccessToken 정보를 DB에 저장하는 메소드
   * @param requestDto
   */
  public void saveAccessToken(OauthInfoRequest requestDto) {
    oauthRepository.save(requestDto.toEntity());
  }


  public void getUserInfo(){
  }

}
