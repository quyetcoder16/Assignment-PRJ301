import { DOMAIN } from "../utils/setting/config";
import authorizedAxiosInstance from "./authorizedAxios";

const getAllMyLeaveRequestAPi = async () => {
  authorizedAxiosInstance.get(`${DOMAIN}/leave_requests`);
};

export const myLeaveRequestService = {
  getAllMyLeaveRequestAPi,
};
