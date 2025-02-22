import { createSlice } from "@reduxjs/toolkit";
import { myLeaveRequestService } from "../../service/myLeaveRequestService";
import { hideLoading, setLoading } from "./loadingReducer";
import { showNotification } from "./notificationReducer";
import { NOTIFICATION_TYPE } from "../../utils/setting/config";

const initialState = {
  leaveRequests: [],
  leaveTypes: [],
};

const leaveRequestReducer = createSlice({
  name: "leaveRequestReducer",
  initialState,
  reducers: {
    setLeaveRequests: (state, action) => {
      state.leaveRequests = action.payload;
    },
    setLeaveTypes: (state, action) => {
      state.leaveTypes = action.payload;
    },
  },
});

export const { setLeaveRequests, setLeaveTypes } = leaveRequestReducer.actions;

// redux thunk
export const getAllMyLeaveRequest = () => {
  return async (dispatch, getState) => {
    dispatch(setLoading());
    try {
      const response = await myLeaveRequestService.getAllMyLeaveRequestAPI();
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

export const getAllLeaveTypes = () => {
  return async (dispatch) => {
    try {
      const response = await myLeaveRequestService.getAllLeaveTypesAPI();
      if (response.data.statusCode === 1000) {
        const formattedTypes = response.data.data.map((type) => ({
          id: type.typeLeaveId, // ID lấy từ `typeLeaveId`
          name: type.nameTypeLeave, // Tên loại nghỉ từ `nameTypeLeave`
        }));
        dispatch(setLeaveTypes(formattedTypes));
      }
    } catch (error) {
      dispatch(
        showNotification({
          type: NOTIFICATION_TYPE.error,
          message: "Failed to fetch leave types!",
        })
      );
    }
  };
};

export const createLeaveRequest = (requestData) => {
  return async (dispatch) => {
    dispatch(setLoading());
    try {
      const { title, reason, fromDate, toDate, idTypeRequest } = requestData;
      const response = await myLeaveRequestService.createMyLeaveRequestAPI(
        title,
        reason,
        fromDate,
        toDate,
        idTypeRequest
      );
      if (response.data.statusCode === 1000) {
        dispatch(
          showNotification({
            type: NOTIFICATION_TYPE.success,
            message: "Create Leave request successfully!",
          })
        );
        dispatch(getAllMyLeaveRequest());
      } else {
        throw new Error("Create leave request failed!");
      }
    } catch (error) {
      dispatch(
        showNotification({
          type: NOTIFICATION_TYPE.error,
          message:
            error?.response?.data?.message || "Create leave request failed!",
        })
      );
      throw error;
    } finally {
      dispatch(hideLoading());
    }
  };
};

export default leaveRequestReducer.reducer;
