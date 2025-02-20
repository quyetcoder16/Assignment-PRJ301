import axios from "axios";
import {
  ACCESS_TOKEN,
  REFRESH_TOKEN,
  USER_INFO,
} from "../utils/setting/config";
import { authService } from "./authService";

let authorizedAxiosInstance = axios.create();

authorizedAxiosInstance.defaults.timeout = 1000 * 60 * 10;

// Add a request interceptor
authorizedAxiosInstance.interceptors.request.use(
  (config) => {
    // Do something before request is sent

    const accessToken = localStorage.getItem(ACCESS_TOKEN);
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

let refreshTokenPromise = null;

// Add a response interceptor
authorizedAxiosInstance.interceptors.response.use(
  (response) => {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    return response;
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error

    const originalRequest = error.config;

    if (error.response?.status === 401 && originalRequest) {
      if (!refreshTokenPromise) {
        const refreshToken = localStorage.getItem(REFRESH_TOKEN);
        const accessToken = localStorage.getItem(ACCESS_TOKEN);
        refreshTokenPromise = authService
          .refreshTokenAPI(accessToken, refreshToken)
          .then((res) => {
            const newAccessToken = res?.data?.data?.accessToken;
            const newRefreshToken = res?.data?.data?.refreshToken;
            localStorage.setItem(ACCESS_TOKEN, newAccessToken);
            localStorage.setItem(REFRESH_TOKEN, newRefreshToken);

            authorizedAxiosInstance.defaults.headers.Authorization = `Bearer ${newAccessToken}`;
          })
          .catch((_error) => {
            localStorage.removeItem(USER_INFO);
            localStorage.removeItem(REFRESH_TOKEN);
            localStorage.removeItem(ACCESS_TOKEN);
            window.location.href = "/login";

            return Promise.reject(_error);
          })
          .finally(() => {
            refreshTokenPromise = null;
          });
      }

      return refreshTokenPromise.then(() => {
        return authorizedAxiosInstance(originalRequest);
      });
    }

    return Promise.reject(error);
  }
);

export default authorizedAxiosInstance;
