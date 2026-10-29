package com.course_registration_system.business.auth;

import com.course_registration_system.business.auth.model.AuthTokens;
import com.course_registration_system.business.exception.UnauthorizedException;
import com.course_registration_system.business.user.UserRepository;
import com.course_registration_system.business.user.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}

	public AuthTokens login(String username, String password) {
		// Same error for an unknown user and a wrong password, so the response does not reveal which accounts exist
		User user = userRepository.findByUsername(username.trim())
				.filter(u -> passwordEncoder.matches(password, u.getPasswordHash()))
				.orElseThrow(() -> new UnauthorizedException("INVALID_CREDENTIALS", "Username or password is incorrect"));
		return tokenService.issue(user);
	}

	public AuthTokens refresh(String refreshToken) {
		String userId = tokenService.verifyRefreshToken(refreshToken);

		// Reload the user so a deleted account or a changed role takes effect on the next refresh
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UnauthorizedException("INVALID_REFRESH_TOKEN", "Refresh token is invalid or expired"));
		return tokenService.issue(user);
	}
}
