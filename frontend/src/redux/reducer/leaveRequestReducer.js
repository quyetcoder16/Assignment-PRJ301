import { createSlice } from "@reduxjs/toolkit";
import { myLeaveRequestService } from "../../service/myLeaveRequestService";
import { hideLoading, setLoading } from "./loadingReducer";
import { showNotification } from "./notificationReducer";
import { NOTIFICATION_TYPE } from "../../utils/setting/config";

const initialState = {
  leaveRequests: [],
};

const leaveRequestReducer = createSlice({
  name: "leaveRequestReducer",
  initialState,
  reducers: {
    setLeaveRequests: (state, action) => {
      state.leaveRequests = action.payload;
    },
  },
});

export const { setLeaveRequests } = leaveRequestReducer.actions;

// redux thunk
export const getAllMyLeaveRequest = () => {
  return async (dispatch, getState) => {
    dispatch(setLoading());
    try {
      const response = await myLeaveRequestService.getAllMyLeaveRequestAPi();
      if (response.data.statusCode === 1000) {
        dispatch(setLeaveRequests(response?.data?.data));
        dispatch(
          showNotification({
            type: NOTIFICATION_TYPE.success,
            message: "Get Leave Requests successfully",
          })
        );
      }
    } catch (error) {
      const data = error?.response?.data;
      dispatch(
        showNotification({
          type: NOTIFICATION_TYPE.error,
          message: data?.message || "get my leave request fail!",
        })
      );
    } finally {
      dispatch(hideLoading());
    }
  };
};

export default leaveRequestReducer.reducer;
