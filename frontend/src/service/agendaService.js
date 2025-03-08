import { DOMAIN } from "../utils/setting/config";
import authorizedAxiosInstance from "./authorizedAxios";

const getSubordinateEmployeeStatus = async (startDate, endDate, viewMode) => {
  return await authorizedAxiosInstance.get(`${DOMAIN}/agenda/employee-status`, {
    params: {
      startDate,
      endDate,
      viewMode,
    },
  });
};

const exportToExcel = async (startDate, endDate) => {
  return await authorizedAxiosInstance.get(`${DOMAIN}/agenda/export-excel`, {
    params: {
      startDate,
      endDate,
    },
    responseType: "blob",
  });
};

export const agendaService = {
  getSubordinateEmployeeStatus,
  exportToExcel,
};
