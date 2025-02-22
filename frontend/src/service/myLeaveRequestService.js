import { DOMAIN } from "../utils/setting/config";
import authorizedAxiosInstance from "./authorizedAxios";

const getAllMyLeaveRequestAPI = async () => {
  return authorizedAxiosInstance.get(`${DOMAIN}/my_leave_request`);
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

export const myLeaveRequestService = {
  getAllMyLeaveRequestAPI,
  createMyLeaveRequestAPI,
  getAllLeaveTypesAPI,
};
