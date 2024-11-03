package com.fuji.airbnb_clone_backend.infrastructure.config;

import com.fuji.airbnb_clone_backend.user.domain.User;

import java.util.Map;
import java.util.Objects;

public class SecurityUtils {
    public static final String ROLE_TENANT= "ROLE_TENANT";
    public static final String ROLE_LANDLORD= "ROLE_LANDLORD";
    public static final String CLAIMS_NAMESPACE= "https://fuji.com/roles";

    public static User mapOauth2AttributesToUser(Map<String, Object> attributes) {
        User user= new User();
        String username= null;

        if (!Objects.isNull(attributes.get("preferred_name"))) {
            username= ((String) attributes.get("preferred_name")).toLowerCase();
        }

        if (!Objects.isNull(attributes.get("family_name"))) {
            user.setFirstname(((String) attributes.get("family_name")).toLowerCase());
        }

        if (!Objects.isNull(attributes.get("given_name"))) {
            user.setLastname(((String) attributes.get("given_name")).toLowerCase());
        } else if (!Objects.isNull(attributes.get("nickname"))) {
            user.setLastname((String) attributes.get("nickname"));
        }

        return user;
    }
}
