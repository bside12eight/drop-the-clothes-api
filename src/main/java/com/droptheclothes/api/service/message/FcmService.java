package com.droptheclothes.api.service.message;

import com.droptheclothes.api.model.base.FcmMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FcmService {

  private final String API_URL = "https://fcm.googleapis.com/v1/projects/eight-front/messages:send";
  private final ObjectMapper objectMapper;

  public void sendMessageTo(String targetToken, String title, String body) throws IOException {
    String message = makeMessage(targetToken, title, body);

    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"),
        message);
    Request request = new Request.Builder()
        .url(API_URL)
        .post(requestBody)
        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
        .build();
    Response response = client.newCall(request).execute();

  }

  private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
    FcmMessage fcmMessage = FcmMessage.builder()
        .message(FcmMessage.Message.builder()
            .token(targetToken)
            .notification(FcmMessage.Notification.builder()
                .title(title)
                .body(body)
                .image(null)
                .build()
            ).build()).validateOnly(false).build();

    return objectMapper.writeValueAsString(fcmMessage);
  }

  private String getAccessToken() throws IOException {

    String firebaseConfigPath = "firebase/serviceAccountKey.json";

    GoogleCredentials googleCredentials = GoogleCredentials
        .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

    googleCredentials.refreshIfExpired();
    System.out.println("token : " + googleCredentials.getAccessToken().getTokenValue());

    return googleCredentials.getAccessToken().getTokenValue();

  }
}