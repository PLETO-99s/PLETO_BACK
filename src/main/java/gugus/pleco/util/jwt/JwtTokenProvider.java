package gugus.pleco.util.jwt;

import gugus.pleco.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${accesstoken.secret}")
    private String accessTokenKey;

    @Value("${refreshtoken.secret")
    private String refreshTokenKey;

    private final Long ACCESS_TOKEN_TIME = 60 * 30 *1000L; //accessToken 유효시간 30분
    private final Long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 30 * 1000L; //유효시간 1달


    private final UserRepository userRepository;


    @PostConstruct
    protected void init() {
        accessTokenKey = Base64.getEncoder().encodeToString(accessTokenKey.getBytes());
        refreshTokenKey = Base64.getEncoder().encodeToString(accessTokenKey.getBytes());
    }

    public Map<String, String> createToken(String UserPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(UserPk);
        claims.put("roles", roles);
        Date now = new Date();
        String accestToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, accessTokenKey)
                .compact();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, this.refreshTokenKey)
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("ACCESS-TOKEN", accestToken);
        map.put("REFRESH-TOKEN", refreshToken);
        return map;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = (UserDetails) userRepository.findByUsername(this.getUserPkAccessToken(token)).get();
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPkAccessToken(String token){
        return Jwts.parser().setSigningKey(accessTokenKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserPkRefreshToken(String token){
        return Jwts.parser().setSigningKey(refreshTokenKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("ACCESS-TOKEN");
    }

    public boolean validateAccessToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessTokenKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    public boolean validateRefreshToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshTokenKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
