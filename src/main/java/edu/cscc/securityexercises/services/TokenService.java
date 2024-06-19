package edu.cscc.securityexercises.services;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    public boolean hasScope(Jwt jwt, String scope) {
        String[] scopes = jwt.getClaimAsString("scope").split(" ");
        List<String> scopeList = List.of(scopes);
        return scopeList.contains(scope);
    }

    public boolean hasScopes(Jwt jwt, String[] scopes) {
        for (String scope : scopes) {
            if (!hasScope(jwt, scope)) {
                return false;
            }
        }
        return true;
    }

    public String getClaim(Jwt jwt, String claim) {
        return jwt.getClaim(claim);
    }
}
