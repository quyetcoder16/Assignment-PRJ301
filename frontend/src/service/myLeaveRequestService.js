import { DOMAIN } from "../utils/setting/config";
import authorizedAxiosInstance from "./authorizedAxios";

const getAllMyLeaveRequestAPI = async (
  page = 0,
  size = 10,
  startCreatedAt = "",
  endCreatedAt = "",
  leaveDateStart = "",
  leaveDateEnd = "",
  leaveTypeId = 0,
  statusId = 0,
  sort = "idRequest,desc"
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
  };
  return authorizedAxiosInstance.get(`${DOMAIN}/my_leave_request`, { params });
};

const createMyLeaveRequestAPI = async (
  title,
  reason,
  fromDate,
  toDate,
  idTypeRequest
) => {
  return authorizedAxiosInstance.post(`${DOMAIN}/my_leave_request`, {
    title,
    reason,
    fromDate,
    toDate,
    idTypeRequest,
  });
};

const getAllLeaveTypesAPI = async () => {
  return authorizedAxiosInstance.get(`${DOMAIN}/type_leave`);
};

const updateMyLeaveRequestAPI = async (requestData) => {
  return await authorizedAxiosInstance.put(
    `${DOMAIN}/my_leave_request/update`,
    requestData
  );
};

const deleteMyLeaveRequestAPI = async (idRequest) => {
  return await authorizedAxiosInstance.delete(
    `${DOMAIN}/my_leave_request/delete/${idRequest}`
  );
};

export const myLeaveRequestService = {
  getAllMyLeaveRequestAPI,
  createMyLeaveRequestAPI,
  getAllLeaveTypesAPI,
  updateMyLeaveRequestAPI,
  deleteMyLeaveRequestAPI,
};
