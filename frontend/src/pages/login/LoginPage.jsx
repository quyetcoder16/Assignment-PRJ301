import { useState } from "react";
import { TextField, Button, Checkbox, FormControlLabel } from "@mui/material";
import GoogleIcon from "@mui/icons-material/Google";
import FacebookIcon from "@mui/icons-material/Facebook";
import { useDispatch } from "react-redux";
// import { loginSuccess } from "../redux/authSlice";
// import { loginUser } from "../service/authService";
import { useNavigate } from "react-router-dom";
import "../../assets/styles/LoginPage.css";
import logo from "../../assets/img/logo.png";

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [emailError, setEmailError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const validateEmail = (email) => {
    const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return re.test(email);
  };

  const validatePassword = (password) => {
    return password.length >= 6;
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    let valid = true;

    if (!validateEmail(email)) {
      setEmailError("Invalid email format");
      valid = false;
    } else {
      setEmailError("");
    }

    if (!validatePassword(password)) {
      setPasswordError("Password must be at least 6 characters");
      valid = false;
    } else {
      setPasswordError("");
    }

    if (!valid) return;
    // try {
    //   const response = await loginUser({ email, password });
    //   const { token, user } = response.data;
    //   localStorage.setItem('token', token);
    //   localStorage.setItem('user', JSON.stringify(user));
    //   dispatch(loginSuccess({ token, user }));
    //   navigate('/');
    // } catch (error) {
    //   console.error('Đăng nhập thất bại', error);
    // }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-left">
          <img src={logo} alt="Calendar Icon" className="login-icon" />
          <h1>Log in to your Account</h1>
          <p>Welcome back! Select method to log in:</p>
          <div className="social-login">
            <Button className="google-btn" startIcon={<GoogleIcon />}>
              Google
            </Button>
            <Button
              className="facebook-btn"
              startIcon={<FacebookIcon />}
              style={{ backgroundColor: "#1877F2", color: "white" }}
            >
              Facebook
            </Button>
          </div>
          <p>or continue with email</p>
          <form onSubmit={handleLogin}>
            <TextField
              label="Email"
              fullWidth
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              margin="normal"
              required
              error={!!emailError}
              helperText={emailError}
            />
            <TextField
              label="Password"
              type="password"
              fullWidth
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              margin="normal"
              required
              error={!!passwordError}
              helperText={passwordError}
            />
            <div className="options">
              <FormControlLabel control={<Checkbox />} label="Remember me" />
              <a href="/forgot-password">Forgot Password?</a>
            </div>
            <Button
              type="submit"
              variant="contained"
              fullWidth
              className="login-btn"
            >
              Log in
            </Button>
          </form>
        </div>
        <div className="login-right"></div>
      </div>
    </div>
  );
};

export default LoginPage;
