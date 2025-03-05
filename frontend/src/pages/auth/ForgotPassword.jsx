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
} from "@mui/material";
import logo from "../../assets/img/logo.png";

const ForgotPassword = () => {
  const [step, setStep] = useState(0);
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleNext = () => setStep((prev) => prev + 1);
  const handleBack = () => setStep((prev) => prev - 1);

  const handleReset = () => {
    alert("Password reset successfully!");
    setStep(0);
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
                  sx={{ mr: 1 }}
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
                  sx={{ mr: 1 }}
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
