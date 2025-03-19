export const DOMAIN = import.meta.env.VITE_API_URL;
export const ACCESS_TOKEN = "ACCESS_TOKEN";
export const REFRESH_TOKEN = "REFRESH_TOKEN";
export const USER_INFO = "USER_INFO";

export const NOTIFICATION_TYPE = {
  success: "success",
  warning: "warning",
  info: "info",
  error: "error",
};

export const STATUS_CODE = {
  SUCCESS: 1000,
};

export const HTTP_STATUS_CODE = {
  SUCCESS: 200,
};

export const OAuthConfig = {
  clientId:
    "571904821045-tsgpln2109kio131mdfnbg9l45j8lai4.apps.googleusercontent.com",
  redirectUri: "http://localhost:5173/authenticate",
  authUri: "https://accounts.google.com/o/oauth2/auth",
};
