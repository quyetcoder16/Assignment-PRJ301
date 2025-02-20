import { configureStore } from "@reduxjs/toolkit";
import loadingReducer from "./reducer/loadingReducer";
import authReducer from "./reducer/authReducer";
import notificationReducer from "./reducer/notificationReducer";
import leaveRequestReducer from "./reducer/leaveRequestReducer";
export const store = configureStore({
  reducer: {
    loadingReducer,
    authReducer,
    notificationReducer,
    leaveRequestReducer,
  },
});
