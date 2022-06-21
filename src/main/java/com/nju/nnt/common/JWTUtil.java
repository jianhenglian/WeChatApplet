package com.nju.nnt.common;

import com.nju.nnt.entity.User;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

import java.security.Key;

public class JWTUtil {
    public static String generateToken(User userToken, int expire) throws Exception {
        JwtClaims claims = new JwtClaims();
        claims.setSubject(userToken.getOpenId()+"");
        claims.setClaim("gender", userToken.getGender());
        claims.setClaim("nickname", userToken.getNickname());
        claims.setClaim("avatarUrl", userToken.getAvatarUrl());
        claims.setExpirationTimeMinutesInTheFuture(expire == 0 ? 60*24 : expire);

        Key key = new HmacKey("NJUNNT".getBytes("UTF-8"));

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(key);
        jws.setDoKeyValidation(false); // relaxes the key length requirement

        //签名
        String token = jws.getCompactSerialization();
        return token;
    }

    public static User getInfoFromToken(String token) throws Exception {

        if (token == null) {
            return null;
        }

        Key key = new HmacKey("NJUNNT".getBytes("UTF-8"));

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setVerificationKey(key)
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build();

        JwtClaims processedClaims = jwtConsumer.processToClaims(token);
        User user = new User();
        user.setOpenId(processedClaims.getSubject());
        user.setNickname((String) processedClaims.getClaimValue("nickname"));
        user.setGender((String) processedClaims.getClaimValue("gender"));
        user.setAvatarUrl((String) processedClaims.getClaimValue("avatarUrl"));
        return user;

    }


}
