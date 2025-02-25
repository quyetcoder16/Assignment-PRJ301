import { DOMAIN } from "../utils/setting/config";
import authorizedAxiosInstance from "./authorizedAxios";

const getAllSubLeaveRequest = async () => {
  return await authorizedAxiosInstance.get(`${DOMAIN}/leave_approval`);
};

export const leaveApprovalService = {
  getAllSubLeaveRequest,
};
