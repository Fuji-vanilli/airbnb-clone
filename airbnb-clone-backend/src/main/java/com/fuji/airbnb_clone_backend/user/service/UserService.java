package com.fuji.airbnb_clone_backend.user.service;

import com.fuji.airbnb_clone_backend.infrastructure.config.SecurityUtils;
import com.fuji.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import com.fuji.airbnb_clone_backend.user.domain.User;
import com.fuji.airbnb_clone_backend.user.mapper.UserMapper;
import com.fuji.airbnb_clone_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static final String UPDATED_AT_KEY= "updated_at";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public ReadUserDTO getAuthenticatedUserFromSecurityContext() {
        OAuth2User principal= (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = SecurityUtils.mapOauth2AttributesToUser(principal.getAttributes());
        return getByEmail(user.getEmail()).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Optional<ReadUserDTO> getByEmail(String email) {
        Optional<User> userOptional = userRepository.findOneByEmail(email);
        return userOptional.map(userMapper::readUserToUserDTO);
    }

    public void syncWithIdp(OAuth2User oAuth2User, boolean forceResync) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        User user = SecurityUtils.mapOauth2AttributesToUser(attributes);

        Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            if (!Objects.isNull(attributes.get(UPDATED_AT_KEY))) {
                Instant lastModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
                Instant idpModifiedDate;

                if (attributes.get(UPDATED_AT_KEY) instanceof Instant instant) {
                    idpModifiedDate= instant;
                } else  {
                    idpModifiedDate= Instant.ofEpochSecond((Integer)attributes.get(UPDATED_AT_KEY));
                }

                if (idpModifiedDate.isAfter(lastModifiedDate) || forceResync) {
                    updateUser(user);
                }
            }
        } else {
            userRepository.saveAndFlush(user);
        }
    }

    private void updateUser(User user) {
        Optional<User> userToUpdateOpt = userRepository.findOneByEmail(user.getEmail());
        if (userToUpdateOpt.isPresent()) {
            User userToUpdate = userToUpdateOpt.get();
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setFirstname(user.getFirstname());
            userToUpdate.setLastname(user.getLastname());
            userToUpdate.setAuthorities(user.getAuthorities());
            userToUpdate.setImageUrl(user.getImageUrl());
            userRepository.saveAndFlush(userToUpdate);
        }
    }

    public Optional<ReadUserDTO> getByPublicId(UUID publicId) {
        Optional<User> oneByPublicId = userRepository.findOneByPublicId(publicId);
        return oneByPublicId.map(userMapper::readUserToUserDTO);
    }
}
