package com.hieupn.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.jwk.RSAKey;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.security.KeyPair;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class AuthorizationServerKeyConfig {

    @Value("${PUBLIC_KEY_PATH}")
    private String publicKeyPath;

    @Value("${PRIVATE_KEY_PATH}")
    private String privateKeyPath;

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        RSAPublicKey publicKey = loadPublicKey(publicKeyPath);
        RSAPrivateKey privateKey = loadPrivateKey(privateKeyPath);

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("6046252f-545c-4143-86c5-3c62767df008")
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private RSAPrivateKey loadPrivateKey(String filepath) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        try (PEMParser pemParser = new PEMParser(new FileReader(filepath))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (object instanceof PEMKeyPair) {
                KeyPair kp = converter.getKeyPair((PEMKeyPair) object);
                return (RSAPrivateKey) kp.getPrivate();
            } else if (object instanceof PrivateKeyInfo) {
                return (RSAPrivateKey) converter.getPrivateKey((PrivateKeyInfo) object);
            } else {
                throw new IllegalArgumentException("Cannot parse private key from PEM");
            }
        }
    }

    private RSAPublicKey loadPublicKey(String filepath) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        try (PEMParser pemParser = new PEMParser(new FileReader(filepath))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (object instanceof SubjectPublicKeyInfo) {
                return (RSAPublicKey) converter.getPublicKey((SubjectPublicKeyInfo) object);
            } else {
                throw new IllegalArgumentException("Cannot parse private key from PEM");
            }
        }
    }
}
