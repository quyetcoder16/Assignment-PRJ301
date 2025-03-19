import { useNavigate, useSearchParams } from "react-router-dom";
import { useEffect } from "react";
import { Box, CircularProgress, Typography } from "@mui/material";
import { useDispatch } from "react-redux";
import { authService } from "../../service/authService";
import { NOTIFICATION_TYPE } from "../../utils/setting/config";
import { setUserLogin } from "../../redux/reducer/authReducer";
import { showNotification } from "../../redux/reducer/notificationReducer";

export default function Authenticate() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const authenticateUser = async () => {
      try {
        const authCode = searchParams.get("code"); // Lấy code từ query params

        if (!authCode) {
          throw new Error("Authorization code is missing.");
        }

        // Gọi API login với Google
        const { data, status } = await authService.loginWithGoogleV2(authCode);

        if (status === 200 && data?.statusCode === 1000) {
          dispatch(
            setUserLogin({
              accessToken: data?.data?.accessToken,
              refreshToken: data?.data?.refreshToken,
              user: data?.data?.user,
            })
          );

          dispatch(
            showNotification({
              type: NOTIFICATION_TYPE.success,
              message: "Login Successful",
              description: "You have logged in successfully.",
            })
          );

          navigate("/"); // Chuyển hướng về trang chủ
        } else {
          throw new Error(data?.message || "Login failed.");
        }
      } catch (error) {
        console.error("Authentication Error:", error.message);

        dispatch(
          showNotification({
            type: NOTIFICATION_TYPE.error,
            message: "Authentication Failed",
            description: error.message || "Unable to authenticate.",
          })
        );

        navigate("/login"); // Chuyển về trang login nếu lỗi
      }
    };

    authenticateUser();
  }, [searchParams, dispatch, navigate]);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: "30px",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
      }}
    >
      <CircularProgress />
      <Typography>Authenticating...</Typography>
    </Box>
  );
}
