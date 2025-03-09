// src/components/ForgotPassword.jsx
import { useState } from "react";
import {
  Container,
  TextField,
  Button,
  Typography,
  Stepper,
  Step,
  StepLabel,
  Box,
  Alert,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import logo from "../../assets/img/logo.png";
import { authService } from "../../service/authService";

const ForgotPassword = () => {
  const [step, setStep] = useState(0);
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const handleNext = async () => {
    try {
      if (step === 0) {
        await authService.sendOtp(email);
        setSuccess("OTP has been sent to your email!");
        setError("");
        setStep((prev) => prev + 1);
      } else if (step === 1) {
        await authService.verifyOtp(email, otp);
        setSuccess("OTP verified successfully!");
        setError("");
        setStep((prev) => prev + 1);
      }
    } catch (err) {
      setError(err?.response?.data?.message);
      setSuccess("");
    }
  };

  const handleBack = () => {
    setStep((prev) => prev - 1);
    setError("");
    setSuccess("");
  };

  const handleReset = async () => {
    try {
      if (password !== confirmPassword) {
        setError("Passwords do not match!");
        return;
      }
      await authService.resetPassword(email, otp, password);
      setSuccess("Password reset successfully!");
      setError("");
      setTimeout(() => {
        navigate("/login"); // Chuyển hướng về trang login
        setEmail("");
        setOtp("");
        setPassword("");
        setConfirmPassword("");
        setSuccess("");
      }, 2000);
    } catch (err) {
      setError(err?.response?.data?.message);
      setSuccess("");
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-left">
          <img src={logo} alt="Calendar Icon" className="login-icon" />

          <Container maxWidth="xs" sx={{ mt: 5, textAlign: "center" }}>
            <Typography variant="h5" sx={{ mb: 2 }}>
              {step === 0 && "Forgot Password?"}
              {step === 1 && "Enter OTP"}
              {step === 2 && "Set New Password"}
            </Typography>

            <Stepper activeStep={step} alternativeLabel sx={{ mb: 3 }}>
              <Step>
                <StepLabel>Email</StepLabel>
              </Step>
              <Step>
                <StepLabel>OTP</StepLabel>
              </Step>
              <Step>
                <StepLabel>New Password</StepLabel>
              </Step>
            </Stepper>

            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error}
              </Alert>
            )}
            {success && (
              <Alert severity="success" sx={{ mb: 2 }}>
                {success}
              </Alert>
            )}

            {step === 0 && (
              <Box>
                <TextField
                  fullWidth
                  label="Enter your email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  sx={{ mb: 2 }}
                />
                <Button
                  fullWidth
                  variant="contained"
                  color="primary"
                  onClick={handleNext}
                  disabled={!email}
                >
                  Send OTP
                </Button>
              </Box>
            )}

            {step === 1 && (
              <Box>
                <TextField
                  fullWidth
                  label="Enter OTP"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  sx={{ mb: 2 }}
                />
                <Button
                  fullWidth
                  variant="contained"
                  color="primary"
                  onClick={handleNext}
                  sx={{ mb: 1 }}
                  disabled={!otp}
                >
                  Verify OTP
                </Button>
                <Button
                  fullWidth
                  variant="outlined"
                  color="secondary"
                  onClick={handleBack}
                >
                  Back
                </Button>
              </Box>
            )}

            {step === 2 && (
              <Box>
                <TextField
                  fullWidth
                  label="New Password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  sx={{ mb: 2 }}
                />
                <TextField
                  fullWidth
                  label="Confirm Password"
                  type="password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  sx={{ mb: 2 }}
                />
                <Button
                  fullWidth
                  variant="contained"
                  color="primary"
                  onClick={handleReset}
                  sx={{ mb: 1 }}
                  disabled={!password || !confirmPassword}
                >
                  Reset Password
                </Button>
                <Button
                  fullWidth
                  variant="outlined"
                  color="secondary"
                  onClick={handleBack}
                >
                  Back
                </Button>
              </Box>
            )}
          </Container>
        </div>
        <div className="login-right"></div>
      </div>
    </div>
  );
};

export default ForgotPassword;
