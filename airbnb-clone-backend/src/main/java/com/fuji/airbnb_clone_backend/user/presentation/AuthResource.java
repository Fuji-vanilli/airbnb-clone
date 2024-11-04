package com.fuji.airbnb_clone_backend.user.presentation;

import com.fuji.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import com.fuji.airbnb_clone_backend.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/auth")
public class AuthResource {
    private final UserService userService;
    private final ClientRegistration clientRegistration;

    public AuthResource(UserService userService, ClientRegistrationRepository registration) {
        this.userService = userService;
        this.clientRegistration = registration.findByRegistrationId("okta");
    }

    @GetMapping("get-authenticated-user")
    public ResponseEntity<ReadUserDTO> getAuthenticatedUser(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @RequestParam boolean forceResync
            ) {
        if (Objects.isNull(oAuth2User)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            userService.syncWithIdp(oAuth2User, forceResync);
            ReadUserDTO authenticatedUser = userService.getAuthenticatedUserFromSecurityContext();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String issuerUri = clientRegistration.getProviderDetails().getIssuerUri();
        String originUrl = request.getHeader(HttpHeaders.ORIGIN);
        Object[] params = {issuerUri, clientRegistration.getClientId(), originUrl};
        String logoutUrl = MessageFormat.format("{0}v2/logout?client_id={1}&returnTo={2}", params);
        request.getSession().invalidate();
        return ResponseEntity.ok().body(Map.of("logoutUrl", logoutUrl));
    }
}
