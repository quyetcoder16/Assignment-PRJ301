import { createSlice } from "@reduxjs/toolkit";
import {
  ACCESS_TOKEN,
  HTTP_STATUS_CODE,
  NOTIFICATION_TYPE,
  REFRESH_TOKEN,
  STATUS_CODE,
  USER_INFO,
} from "../../utils/setting/config";
import { hideLoading, setLoading } from "./loadingReducer";
import { authService } from "../../service/authService";
import { showNotification } from "./notificationReducer";

const initialState = {
  user: JSON.parse(localStorage.getItem(USER_INFO)) || null,
  accessToken: localStorage.getItem(ACCESS_TOKEN) || "",
  refreshToken: localStorage.getItem(REFRESH_TOKEN) || "",
};

const authReducer = createSlice({
  name: "authReducer",
  initialState,
  reducers: {
    setUserLogin: (state, action) => {
      const { accessToken, refreshToken, user } = action.payload;
      localStorage.setItem(ACCESS_TOKEN, accessToken);
      localStorage.setItem(REFRESH_TOKEN, refreshToken);
      localStorage.setItem(USER_INFO, JSON.stringify(user));
      state.accessToken = accessToken;
      state.refreshToken = refreshToken;
      state.user = user;
    },
  },
});

export const { setUserLogin } = authReducer.actions;

// redux thunk

export const loginUser = (email, password) => {
  return async (dispatch) => {
    dispatch(setLoading());
    try {
      const { data, status } = await authService.loginAPI(email, password);
      if (
        status == HTTP_STATUS_CODE.SUCCESS &&
        data?.statusCode == STATUS_CODE.SUCCESS
      ) {
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
            message: data?.message,
            description: "You have logged in successfully.",
          })
        );
      }
    } catch (error) {
      const data = error?.response?.data;
      dispatch(
        showNotification({
          type: NOTIFICATION_TYPE.error,
          message: data?.message,
        })
      );
      throw error;
    } finally {
      dispatch(hideLoading());
    }
  };
};

export const logoutUser = () => {
  return async (dispatch, getState) => {
    try {
      const { accessToken, refreshToken } = getState().authReducer;

      await authService.logoutAPI(accessToken, refreshToken);

      localStorage.removeItem(ACCESS_TOKEN);
      localStorage.removeItem(REFRESH_TOKEN);
      localStorage.removeItem(USER_INFO);

      dispatch(setUserLogin({ accessToken: "", refreshToken: "", user: null }));

      dispatch(
        showNotification({
          type: NOTIFICATION_TYPE.success,
          message: "Logged out successfully",
        })
      );

      window.location.href = "/login";
    } catch (error) {
      const data = error?.response?.data;
      dispatch(
        showNotification({
          type: NOTIFICATION_TYPE.error,
          message: data?.message || "Logout failed. Please try again.",
        })
      );
    }
  };
};

export default authReducer.reducer;
