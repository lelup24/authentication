package de.tutorial.authentication.backend.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

  String meinSecret = "1234";

  public String createToken(final Authentication authResult) {
    return JWT.create()
        .withSubject(((User) authResult.getPrincipal()).getUsername())
        .withIssuer("me")
        .withExpiresAt(new Date(new Date().getTime() + 1000000))
        .withIssuedAt(new Date())
        .withClaim("rollen", List.of("Admin"))
        .sign(Algorithm.HMAC256(meinSecret));
  }

  public Boolean validate(final String tokenAsString) {
    try {
      final Verification verification = JWT.require(Algorithm.HMAC256(meinSecret));
      final JWTVerifier verifier = verification.build();
      verifier.verify(tokenAsString);
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

  public String getUsername(final String token) {
    final Verification verification = JWT.require(Algorithm.HMAC256(meinSecret));
    final JWTVerifier verifier = verification.build();
    final DecodedJWT decodedJWT = verifier.verify(token);
    return decodedJWT.getSubject();
  }

  public Instant getExpiresAt(final String token) {
    final Verification verification = JWT.require(Algorithm.HMAC256(meinSecret));
    final JWTVerifier verifier = verification.build();
    final DecodedJWT decodedJWT = verifier.verify(token);
    return decodedJWT.getExpiresAtAsInstant();
  }
}
