import { DOMAIN } from "../utils/setting/config";
import authorizedAxiosInstance from "./authorizedAxios";

const getAllSubLeaveRequest = async (
  page = 0,
  size = 10,
  startCreatedAt = "",
  endCreatedAt = "",
  leaveDateStart = "",
  leaveDateEnd = "",
  leaveTypeId = 0,
  statusId = 0,
  sort = "idRequest,desc",
  employeeName
) => {
  const params = {
    size,
    page,
    startCreatedAt,
    endCreatedAt,
    leaveDateStart,
    leaveDateEnd,
    leaveTypeId,
    statusId,
    sort,
    employeeName,
  };
  return await authorizedAxiosInstance.get(`${DOMAIN}/leave_approval`, {
    params,
  });
};

const processLeaveRequestAPI = async (idRequest, noteProcess, action) => {
  return await authorizedAxiosInstance.post(
    `${DOMAIN}/leave_approval/process`,
    {
      idRequest,
      noteProcess,
      action,
    }
  );
};

export const leaveApprovalService = {
  getAllSubLeaveRequest,
  processLeaveRequestAPI,
};
