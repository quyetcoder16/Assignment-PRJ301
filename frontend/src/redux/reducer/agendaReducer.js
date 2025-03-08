import { createSlice } from "@reduxjs/toolkit";
import { hideLoading, setLoading } from "./loadingReducer";
import { showNotification } from "./notificationReducer";
import { agendaService } from "../../service/agendaService";
import { NOTIFICATION_TYPE } from "../../utils/setting/config";

const initialState = {
  subordinateEmployees: [],
};

const agendaReducer = createSlice({
  name: "agendaReducer",
  initialState,
  reducers: {
    setSubordinateEmployees: (state, action) => {
      state.subordinateEmployees = action.payload;
    },
  },
});

export const { setSubordinateEmployees } = agendaReducer.actions;
export const getSubordinateEmployeeStatus =
  ({ startDate, endDate, viewMode }) =>
  async (dispatch) => {
    dispatch(setLoading());
    try {
      const response = await agendaService.getSubordinateEmployeeStatus(
        startDate,
        endDate,
        viewMode
      );
      if (response.data.statusCode === 1000) {
        const employeesData = response.data.data.map((emp) => ({
          key: emp.id,
          id: emp.id,
          name: emp.name,
          departmentName: emp.departmentName,
          leaveDays: emp.leaveDays,
        }));
        dispatch(setSubordinateEmployees(employeesData));
        if (employeesData.length === 0) {
          dispatch(
            showNotification({
              type: NOTIFICATION_TYPE.info,
              message: "No subordinate employees found.",
            })
          );
        }
      } else {
        throw new Error("Failed to fetch subordinate employee status.");
      }
    } catch (error) {
      dispatch(
        showNotification({
          type: NOTIFICATION_TYPE.error,
          message:
            error?.response?.status === 403
              ? "You do not have permission to view this data."
              : "Failed to load employee status.",
        })
      );
      dispatch(setSubordinateEmployees([]));
    } finally {
      dispatch(hideLoading());
    }
  };

export default agendaReducer.reducer;
