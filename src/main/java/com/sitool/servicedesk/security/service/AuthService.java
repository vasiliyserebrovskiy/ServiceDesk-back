package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.security.dto.request.LoginUserRequest;
import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import com.sitool.servicedesk.token.dto.RefreshTokenDTO;
import com.sitool.servicedesk.token.entity.RefreshToken;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService  refreshTokenService;
    private final UserRepository userRepository;

    public TokenResponseDto login(LoginUserRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        } catch (DisabledException ex) {
            throw new RestApiException(HttpStatus.FORBIDDEN,
                    "User account is not active. Please confirm your email.");
        } catch (LockedException ex) {
            throw new RestApiException(HttpStatus.FORBIDDEN,
                    "User account is locked.");
        } catch (BadCredentialsException ex) {
            throw new RestApiException(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password.");
        }

        AuthUserDetails principal =
                (AuthUserDetails) authentication.getPrincipal();

        UUID userId = principal.user().getId();

        String accessToken =
                jwtTokenService.generateAccessToken(principal.getUsername());

        RefreshTokenDTO refreshToken =
                jwtTokenService.generateRefreshToken(principal.getUsername());

        refreshTokenService.saveRefreshToken(
                userId,
                refreshToken.refreshToken(),
                refreshToken.createdAt(),
                refreshToken.expiredAt()
        );

        return new TokenResponseDto(
                accessToken,
                refreshToken.refreshToken()
        );

    }

    @Transactional
    public TokenResponseDto refreshAccessToken(String refreshToken) {

        RefreshToken storedToken = refreshTokenService.validateRefreshToken(refreshToken);

        refreshTokenService.revokeRefreshToken(storedToken);

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new RestApiException(HttpStatus.UNAUTHORIZED, "User not found"));

        String username = user.getEmail();

        String newAccessToken = jwtTokenService.generateAccessToken(username);
        RefreshTokenDTO newRefreshToken = jwtTokenService.generateRefreshToken(username);

        refreshTokenService.saveRefreshToken(
                user.getId(),
                newRefreshToken.refreshToken(),
                newRefreshToken.createdAt(),
                newRefreshToken.expiredAt()
        );

        return new TokenResponseDto(newAccessToken, newRefreshToken.refreshToken());
    }

}
