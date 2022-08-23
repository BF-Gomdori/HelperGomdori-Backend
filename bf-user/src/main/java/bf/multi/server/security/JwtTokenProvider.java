package bf.multi.server.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private Long expirationMilliSec;

    @Autowired
    private UserDetailsService userDetailsService;


    public String generateToken(Authentication authentication) {
        log.info("Authentication: "+String.valueOf(authentication));
        Claims claims = Jwts.claims().setSubject(authentication.getName());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMilliSec);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Authentication getAuthenticationByToken(String token) {
        UserDetails user = userDetailsService.loadUserByUsername(getUsernameByToken(token));
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public String getUsernameByToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        } else return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException exception) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT Token");
        } catch (ExpiredJwtException exception) {
            log.error("Expired JWT Token");
        } catch (UnsupportedJwtException exception) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException exception) {
            log.error("JWT claims is empty");
        }
        return false;
    }
}
