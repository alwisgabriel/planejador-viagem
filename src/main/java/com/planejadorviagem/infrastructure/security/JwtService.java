package com.planejadorviagem.infrastructure.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public final class JwtService {

    private final String secret;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(UUID userId, String email) {
        try {
            JWSSigner signer = new MACSigner(secret.getBytes());
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(userId.toString())
                    .claim("email", email)
                    .issueTime(Date.from(Instant.now()))
                    .expirationTime(Date.from(Instant.now().plusSeconds(86400)))
                    .build();
            SignedJWT signed = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            signed.sign(signer);
            return signed.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public UUID validateToken(String token) {
        try {
            SignedJWT signed = SignedJWT.parse(token);
            MACVerifier verifier = new MACVerifier(secret.getBytes());
            if (!signed.verify(verifier)) {
                throw new RuntimeException("Token inválido");
            }
            Date expiration = signed.getJWTClaimsSet().getExpirationTime();
            if (expiration.before(Date.from(Instant.now()))) {
                throw new RuntimeException("Token expirado");
            }
            return UUID.fromString(signed.getJWTClaimsSet().getSubject());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao validar token JWT", e);
        }
    }
}
