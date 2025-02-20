import { Navigate, Outlet } from "react-router-dom";
import {
  ACCESS_TOKEN,
  REFRESH_TOKEN,
  USER_INFO,
} from "../utils/setting/config";

export default function ProtectedRoute() {
  const accessToken = localStorage.getItem(ACCESS_TOKEN);
  const refreshToken = localStorage.getItem(REFRESH_TOKEN);
  const userInfo = localStorage.getItem(USER_INFO);

  if (!accessToken || !refreshToken || !userInfo) {
    return <Navigate to={"/login"} />;
  }

  return <Outlet />;
}
