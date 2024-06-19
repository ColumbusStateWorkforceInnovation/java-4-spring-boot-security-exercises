package edu.cscc.securityexercises.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    Logger logger = LoggerFactory.getLogger(TokenService.class);

    public boolean hasScope(Jwt jwt, String scope) {
        String[] scopes = jwt.getClaimAsString("scope").split(" ");
        logger.info("Checking if scope {} is in scopes {}", scope, scopes);
        List<String> scopeList = List.of(scopes);
        return scopeList.contains(scope);
    }

    public boolean hasScopes(Jwt jwt, String scopes) {
        String[] scopeArray = scopes.split(" ");
        logger.info("Checking if scopes {} are in scopes {}", scopes, jwt.getClaimAsString("scope"));
        for (String scope : scopeArray) {
            if (!hasScope(jwt, scope)) {
                return false;
            }
        }
        return true;
    }
}
