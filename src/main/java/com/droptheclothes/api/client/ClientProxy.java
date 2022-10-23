package com.droptheclothes.api.client;


import com.droptheclothes.api.model.entity.Members;

public interface ClientProxy {

    Members getUserData(String accessToken);
}
