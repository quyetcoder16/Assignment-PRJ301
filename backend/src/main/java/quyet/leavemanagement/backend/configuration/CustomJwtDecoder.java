package quyet.leavemanagement.backend.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import quyet.leavemanagement.backend.service.JwtService;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.access-token-signer-key}")
    private String accessTokenSignerKey;

    private final JwtService jwtService;
  
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    public CustomJwtDecoder(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // ✅ Gọi phương thức verifyToken trực tiếp nếu không cần dùng SignedJWT
            jwtService.verifyToken(token, false);
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(accessTokenSignerKey.getBytes(), "HS256");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
