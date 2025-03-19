import axios from "axios";
import { DOMAIN } from "../utils/setting/config";

const loginAPI = async (email, password) => {
  return await axios({
    url: `${DOMAIN}/auth/login`,
    method: "POST",
    data: {
      email,
      password,
    },
  });
};

const logoutAPI = async (accessToken, refreshToken) => {
  return await axios({
    url: `${DOMAIN}/auth/logout`,
    method: "POST",
    data: {
      accessToken,
      refreshToken,
    },
  });
};

const refreshTokenAPI = async (accessToken, refreshToken) => {
  return await axios({
    url: `${DOMAIN}/auth/refresh_token`,
    method: "POST",
    data: {
      accessToken,
      refreshToken,
    },
  });
};

const sendOtp = async (email) => {
  return await axios.post(`${DOMAIN}/auth/forgot-password/send-otp`, { email });
};

const verifyOtp = async (email, otp) => {
  return await axios.post(`${DOMAIN}/auth/forgot-password/verify-otp`, {
    email,
    otp,
  });
};

const loginWithGoogle = async (accessToken) => {
  return await axios.post(`${DOMAIN}/auth/login-google`, {
    accessToken,
  });
};

const resetPassword = async (email, otp, newPassword) => {
  return await axios.post(`${DOMAIN}/auth/forgot-password/reset-password`, {
    email,
    otp,
    newPassword,
  });
};

const loginWithGoogleV2 = async (code) => {
  return await axios.post(
    `${DOMAIN}/auth/outbound/authentication?code=${code}`
  );
};

export const authService = {
  loginAPI,
  logoutAPI,
  refreshTokenAPI,
  resetPassword,
  verifyOtp,
  sendOtp,
  loginWithGoogle,
  loginWithGoogleV2,
};
