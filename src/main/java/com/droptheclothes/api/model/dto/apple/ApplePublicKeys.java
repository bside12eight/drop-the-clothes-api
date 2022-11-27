package com.droptheclothes.api.model.dto.apple;

import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class ApplePublicKeys {

    private List<ApplePublicKey> keys;

    @Getter
    public static class ApplePublicKey {

        private String alg;

        private String e;

        private String kid;

        private String kty;

        private String n;

        private String use;
    }

    public Optional<ApplePublicKey> getMatchedApplePublicKey(IdentityTokenHeader identityTokenHeader) {
        return keys.stream()
                .filter(key -> key.getKid().equals(identityTokenHeader.getKid()) && key.getAlg().equals(identityTokenHeader.getAlg()))
                .findFirst();
    }
}