import { createSlice } from "@reduxjs/toolkit";
import { myLeaveRequestService } from "../../service/myLeaveRequestService";
import { hideLoading, setLoading } from "./loadingReducer";
import { showNotification } from "./notificationReducer";
import { NOTIFICATION_TYPE } from "../../utils/setting/config";
import { leaveApprovalService } from "../../service/leaveApprovalService";

const initialState = {
  leaveRequests: [],
  leaveTypes: [],
  leaveApprovals: [],
  totalLeaveRequest: 0,
  totalLeaveApproval: 0,
};

const leaveRequestReducer = createSlice({
  name: "leaveRequestReducer",
  initialState,
  reducers: {
    setLeaveRequests: (state, action) => {
      state.leaveRequests = action.payload.content; // Dữ liệu đơn nghỉ
      state.totalLeaveRequest = action.payload.totalElements; // Tổng số đơn nghỉ
    },
    setLeaveTypes: (state, action) => {
      state.leaveTypes = action.payload;
    },
    setLeaveApprovals: (state, action) => {
      state.leaveApprovals = action.payload.content;
      state.totalLeaveApproval = action.payload.totalElements;
    },
  },
});

export const { setLeaveRequests, setLeaveTypes, setLeaveApprovals } =
  leaveRequestReducer.actions;

// redux thunk
export const getAllMyLeaveRequest = ({
  page = 0,
  size = 10,
  startCreatedAt = "",
  endCreatedAt = "",
  leaveDateStart = "",
  leaveDateEnd = "",
  leaveTypeId = 0,
  statusId = 0,
  sort = "idRequest,desc",
}) => {
  return async (dispatch, getState) => {
    dispatch(setLoading());
    try {
      const response = await myLeaveRequestService.getAllMyLeaveRequestAPI(
        page,
        size,
        startCreatedAt,
        endCreatedAt,
        leaveDateStart,
        leaveDateEnd,
        leaveTypeId,
        statusId,
        sort
      );
      if (response.data.statusCode === 1000) {
        dispatch(
          setLeaveRequests({
            content: response?.data?.data?.content,
            totalElements: response?.data?.data?.totalElements,
          })
        );
        // dispatch(
        //   showNotification({
        //     type: NOTIFICATION_TYPE.success,
        //     message: "Get Leave Requests successfully",
        //   })
        // );
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
        // dispatch(
        //   getAllMyLeaveRequest({
        //     page: 0,
        //     size: 10,

        //     sort: "idRequest,desc",
        //   })
        // );
        window.location.href = "/my-leave-request";
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

export const getAllLeaveApprovals =
  ({
    page = 0,
    size = 10,
    startCreatedAt = "",
    endCreatedAt = "",
    leaveDateStart = "",
    leaveDateEnd = "",
    leaveTypeId = 0,
    statusId = 0,
    sort = "idRequest,desc",
    employeeName = "",
  }) =>
  async (dispatch) => {
    dispatch(setLoading());
    try {
      const response = await leaveApprovalService.getAllSubLeaveRequest(
        page,
        size,
        startCreatedAt,
        endCreatedAt,
        leaveDateStart,
        leaveDateEnd,
        leaveTypeId,
        statusId,
        sort,
        employeeName
      );
      if (response.data.statusCode === 1000) {
        dispatch(
          setLeaveApprovals({
            content: response?.data?.data?.content,
            totalElements: response?.data?.data?.totalElements,
          })
        );
      }
    } catch (error) {
      dispatch(
        showNotification({
          type: NOTIFICATION_TYPE.error,
          message: "Failed to fetch leave approvals!",
        })
      );
    } finally {
      dispatch(hideLoading());
    }
  };

export const processLeaveRequest = (idRequest, noteProcess, action) => {
  return async (dispatch) => {
    dispatch(setLoading());
    try {
      const response = await leaveApprovalService.processLeaveRequestAPI(
        idRequest,
        noteProcess,
        action
      );
      if (response.data.statusCode === 1000) {
        dispatch(
          showNotification({
            type: NOTIFICATION_TYPE.success,
            message: `Leave request ${action.toLowerCase()}ed successfully!`,
          })
        );
        // Cập nhật lại danh sách sau khi xử lý
        window.location.reload();
        // dispatch(getAllLeaveApprovals(getFiltersFromState()));
      } else {
        throw new Error("Process leave request failed!");
      }
    } catch (error) {
      dispatch(
        showNotification({
          type: NOTIFICATION_TYPE.error,
          message:
            error?.response?.data?.message || "Process leave request failed!",
        })
      );
    } finally {
      dispatch(hideLoading());
    }
  };
};

export default leaveRequestReducer.reducer;
