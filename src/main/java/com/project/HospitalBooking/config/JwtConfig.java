package com.project.HospitalBooking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;

    @Configuration
    public class JwtConfig {

        @Value("${jwt.secret}")
        private String secret;

        @Bean
        public JwtEncoder jwtEncoder() {

            SecretKeySpec secretKey = new SecretKeySpec(
                    secret.getBytes(),
                    "HmacSHA256"
            );

            return NimbusJwtEncoder.withSecretKey(secretKey)
                    .build();
        }
    }

