package com.course_registration_system.business.auth;

import com.course_registration_system.business.auth.model.AuthTokens;
import com.course_registration_system.business.exception.UnauthorizedException;
import com.course_registration_system.business.user.model.User;
import com.course_registration_system.data.define.UserRole;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

// Stateless tokens: a refresh token cannot be revoked before it expires.
@Service
public class TokenService {

	public static final String CLAIM_ROLE = "role";
	public static final String TYPE_ACCESS = "access";
	static final String TYPE_REFRESH = "refresh";
	// Marks access vs refresh, so one kind cannot be used in place of the other
	private static final String CLAIM_TYPE = "typ";

	private final JwtEncoder encoder;
	private final JwtDecoder refreshDecoder;
	private final Clock clock;
	private final Duration accessTtl;
	private final Duration refreshTtl;

	public TokenService(SecretKey jwtKey, Clock clock,
						@Value("${jwt.access-ttl}") Duration accessTtl,
						@Value("${jwt.refresh-ttl}") Duration refreshTtl) {
		this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey));
		this.refreshDecoder = decoderFor(jwtKey, TYPE_REFRESH);
		this.clock = clock;
		this.accessTtl = accessTtl;
		this.refreshTtl = refreshTtl;
	}

	/**
	 * Builds a decoder that accepts only tokens of the given type, signed with the key and not expired.
	 * Shared with SecurityConfig so API requests and refresh use the same validation rules.
	 */
	public static JwtDecoder decoderFor(SecretKey jwtKey, String type) {
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(jwtKey).macAlgorithm(MacAlgorithm.HS256).build();
		decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
				JwtValidators.createDefault(),
				new JwtClaimValidator<String>(CLAIM_TYPE, type::equals)));
		return decoder;
	}

	public AuthTokens issue(User user) {
		Instant now = clock.instant();
		String accessToken = encode(user, TYPE_ACCESS, now, accessTtl);
		String refreshToken = encode(user, TYPE_REFRESH, now, refreshTtl);
		return new AuthTokens(accessToken, refreshToken, accessTtl.toSeconds());
	}

	/**
	 * Returns the user id of a valid refresh token.
	 * Throws UnauthorizedException for a bad signature, an expired token or an access token.
	 */
	public String verifyRefreshToken(String refreshToken) {
		try {
			return refreshDecoder.decode(refreshToken).getSubject();
		} catch (JwtException e) {
			throw new UnauthorizedException("INVALID_REFRESH_TOKEN", "Refresh token is invalid or expired");
		}
	}

	private String encode(User user, String type, Instant now, Duration ttl) {
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(user.getId())
				.claim(CLAIM_ROLE, roleName(user.getRole()))
				.claim(CLAIM_TYPE, type)
				.issuedAt(now)
				.expiresAt(now.plus(ttl))
				.build();
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}

	// Role names become Spring authorities ROLE_<name>, checked by SecurityConfig
	private static String roleName(int role) {
		return switch (role) {
			case UserRole.SYSTEM_ADMIN -> "SYSTEM_ADMIN";
			case UserRole.STUDENT -> "STUDENT";
			default -> "GENERIC";
		};
	}
}
