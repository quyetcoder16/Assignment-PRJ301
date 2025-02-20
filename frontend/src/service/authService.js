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

export const authService = {
  loginAPI,
  logoutAPI,
  refreshTokenAPI,
};
