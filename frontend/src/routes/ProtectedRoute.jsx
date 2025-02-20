import { Navigate, Outlet } from "react-router-dom";
import {
  ACCESS_TOKEN,
  REFRESH_TOKEN,
  USER_INFO,
} from "../utils/setting/config";

export default function ProtectedRoute() {
  let accessToken = localStorage.getItem(ACCESS_TOKEN);
  let refreshToken = localStorage.getItem(REFRESH_TOKEN);
  let userInfo = localStorage.getItem(USER_INFO);

  if (!accessToken || !refreshToken || !userInfo) {
    return <Navigate to={"/login"} />;
  }

  return <Outlet />;
}
