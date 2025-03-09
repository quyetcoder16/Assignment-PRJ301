package quyet.leavemanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quyet.leavemanagement.backend.entity.OtpToken;


import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByEmail(String email);

    Optional<OtpToken> findByEmailAndOtp(String email, String otp);
}
