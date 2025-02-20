import { DOMAIN } from "../utils/setting/config";
import authorizedAxiosInstance from "./authorizedAxios";

const getAllMyLeaveRequestAPi = async () => {
  return authorizedAxiosInstance.get(`${DOMAIN}/my_leave_request`);
};

export const myLeaveRequestService = {
  getAllMyLeaveRequestAPi,
};
