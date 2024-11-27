package be.ucll.se.groep02backend.config;

import java.security.Key;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import be.ucll.se.groep02backend.user.service.UserServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.bytebuddy.asm.Advice.Return;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.tokenExpirationTime}")
    private Integer tokenExpirationTime;

    public String extractUsername(String jWTtoken) throws JwtTokenException {
        return extractClaim(jWTtoken, Claims::getSubject);
    }

    public <T> T extractClaim(String jWTtoken, Function<Claims, T> claimsResolver) throws JwtTokenException {
        final Claims claims = extractAllClaims(jWTtoken);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        extraClaims.put("roles", roles.toArray(new String[0]));

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String jWTtoken, UserDetails userDetails) throws JwtTokenException {
        final String username = extractUsername(jWTtoken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jWTtoken));
    }

    private boolean isTokenExpired(String jWTtoken) throws JwtTokenException {
        return extractExpiration(jWTtoken).before(new Date());
    }

    private Date extractExpiration(String jWTtoken) throws JwtTokenException {
        return extractClaim(jWTtoken, Claims::getExpiration);

    }

    private Claims extractAllClaims(String token) throws JwtTokenException {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException("JWT", "Token is expired");
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
