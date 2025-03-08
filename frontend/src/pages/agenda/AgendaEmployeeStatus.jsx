import React, { useState, useEffect } from "react";
import { Table, DatePicker, Select, Tag, Button } from "antd";
import { useDispatch, useSelector } from "react-redux";
import dayjs from "dayjs";
import isoWeek from "dayjs/plugin/isoWeek";
import { getSubordinateEmployeeStatus } from "../../redux/reducer/agendaReducer";
import { agendaService } from "../../service/agendaService";
import { saveAs } from "file-saver";

const { RangePicker } = DatePicker;
const { Option } = Select;
dayjs.extend(isoWeek);

const AgendaEmployeeStatus = () => {
  const dispatch = useDispatch();
  const { subordinateEmployees } = useSelector((state) => state.agendaReducer);

  const [dateRange, setDateRange] = useState([dayjs(), dayjs().add(7, "day")]);
  const [viewMode, setViewMode] = useState("day");

  useEffect(() => {
    dispatch(
      getSubordinateEmployeeStatus({
        startDate: dateRange[0].format("YYYY-MM-DD"),
        endDate: dateRange[1].format("YYYY-MM-DD"),
        viewMode: viewMode === "week" ? "day" : viewMode, // Week vẫn dùng "day" để lấy dữ liệu chi tiết
      })
    );
  }, [dispatch, dateRange, viewMode]);

  const generateColumns = () => {
    const columns = [
      { title: "Employee", dataIndex: "name", key: "name" },
      {
        title: "Department",
        dataIndex: "departmentName",
        key: "departmentName",
      },
    ];

    let start = dateRange[0].startOf(viewMode);
    let end = dateRange[1].endOf(viewMode);
    let current = start;

    while (current.isBefore(end) || current.isSame(end)) {
      const title =
        viewMode === "day"
          ? current.format("DD/MM")
          : viewMode === "week"
          ? `Week ${current.isoWeek()} (${current
              .startOf("week")
              .format("DD/MM")} - ${current.endOf("week").format("DD/MM")})`
          : viewMode === "month"
          ? current.format("MM/YYYY")
          : current.format("YYYY");

      columns.push({
        title,
        dataIndex: title,
        key: title,
        align: "center",
      });

      current = current.add(1, viewMode);
    }
    return columns;
  };

  const generateData = () => {
    return subordinateEmployees.map((emp) => {
      let row = {
        key: emp.id,
        name: emp.name,
        departmentName: emp.departmentName || "N/A",
      };
      let start = dateRange[0].startOf(viewMode);
      let end = dateRange[1].endOf(viewMode);
      let current = start;

      while (current.isBefore(end) || current.isSame(end)) {
        const key =
          viewMode === "day"
            ? current.format("DD/MM")
            : viewMode === "week"
            ? `Week ${current.isoWeek()} (${current
                .startOf("week")
                .format("DD/MM")} - ${current.endOf("week").format("DD/MM")})`
            : viewMode === "month"
            ? current.format("MM/YYYY")
            : current.format("YYYY");

        if (viewMode === "day") {
          row[key] = emp.leaveDays.includes(current.format("YYYY-MM-DD")) ? (
            <Tag color="red">Off</Tag>
          ) : (
            <Tag color="green">No Leave</Tag>
          );
        } else {
          let leaveCount = emp.leaveDays.filter((day) => {
            let dayObj = dayjs(day);
            return (
              dayObj.isSame(current, viewMode) ||
              (dayObj.isAfter(current.startOf(viewMode)) &&
                dayObj.isBefore(current.endOf(viewMode)))
            );
          }).length;

          row[key] =
            leaveCount > 0 ? (
              <Tag color="red">{leaveCount} days</Tag>
            ) : (
              <Tag color="green">No Leave</Tag>
            );
        }
        current = current.add(1, viewMode);
      }
      return row;
    });
  };

  const handleExportExcel = async () => {
    try {
      const startDate = dateRange[0].format("YYYY-MM-DD");
      const endDate = dateRange[1].format("YYYY-MM-DD");

      const response = await agendaService.exportToExcel(startDate, endDate);
      const blob = new Blob([response.data], {
        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      });

      const contentDisposition = response.headers["content-disposition"];
      let today = new Date();
      let formattedDate =
        today.getFullYear() +
        "-" +
        String(today.getMonth() + 1).padStart(2, "0") +
        "-" +
        String(today.getDate()).padStart(2, "0") +
        "_" +
        String(today.getHours()).padStart(2, "0") +
        "-" +
        String(today.getMinutes()).padStart(2, "0") +
        "-" +
        String(today.getSeconds()).padStart(2, "0");

      let fileName = `export_file_${formattedDate}.xlsx`;
      if (contentDisposition) {
        const fileNameMatch = contentDisposition.match(/filename="(.+)"/);
        if (fileNameMatch && fileNameMatch[1]) {
          fileName = fileNameMatch[1];
        }
      }

      saveAs(blob, fileName);
    } catch (error) {
      console.error("Error exporting Excel:", error);
    }
  };

  return (
    <div className="container mt-4">
      <h1>Agenda - Subordinate Employee Status</h1>
      <div
        style={{
          display: "flex",
          gap: "10px",
          alignItems: "center",
          marginBottom: "10px",
        }}
      >
        <RangePicker
          format="DD-MM-YYYY"
          value={dateRange}
          onChange={(dates) =>
            setDateRange(dates || [dayjs(), dayjs().add(7, "day")])
          }
        />
        <Select value={viewMode} onChange={(value) => setViewMode(value)}>
          <Option value="day">Day</Option>
          <Option value="week">Week</Option>
          <Option value="month">Month</Option>
          <Option value="year">Year</Option>
        </Select>
        <Button type="primary" onClick={handleExportExcel}>
          Export to Excel
        </Button>
      </div>
      <Table
        columns={generateColumns()}
        dataSource={generateData()}
        pagination={false}
        locale={{ emptyText: "No data available or you lack permission." }}
      />
    </div>
  );
};

export default AgendaEmployeeStatus;
