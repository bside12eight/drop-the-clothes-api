package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.dto.FcmRequest;
import com.droptheclothes.api.service.message.FcmService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    // 정완님 토근 : "eD-UCZBM9070pJ10se9j4N:APA91bF7zGYgQnt7Eeox1qOWDj1Uo692dkO2Aw0Umryloj_yDEgc1OyewxIbdW6W6lDVCadrE_cH54ZhX2u8ikDykJbV2iYj0P8TWOHWsvWhoIC2t3mqDyHWLnFJkPOktsb-LM-MSjWE"
    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestBody FcmRequest request) throws IOException {
        fcmService.sendMessageTo(
                request.getTargetToken(),
                request.getTitle(),
                request.getBody());
        return ResponseEntity.ok().build();
    }


}
