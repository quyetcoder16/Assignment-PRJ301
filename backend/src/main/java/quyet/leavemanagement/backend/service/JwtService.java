package quyet.leavemanagement.backend.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.entity.User;

import java.text.ParseException;

@Service
public interface JwtService {
    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;

    SignedJWT getSignedJWT(String token, boolean isRefresh) throws ParseException, JOSEException;

    String generateAccessToken(User user);

    String generateRefreshToken(User user);
}
