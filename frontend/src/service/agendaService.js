import { DOMAIN } from "../utils/setting/config";
import authorizedAxiosInstance from "./authorizedAxios";

const getSubordinateEmployeeStatus = async (startDate, endDate) => {
  return await authorizedAxiosInstance.get(`${DOMAIN}/agenda/employee-status`, {
    params: {
      startDate,
      endDate,
    },
  });
};

export const agendaService = {
  getSubordinateEmployeeStatus,
};
