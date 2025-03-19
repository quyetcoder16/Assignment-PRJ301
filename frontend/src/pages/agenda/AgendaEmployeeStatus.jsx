import React, { useState, useEffect } from "react";
import { Table, DatePicker, Select, Tag, Button, Alert } from "antd";
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

  const [dateRange, setDateRange] = useState([dayjs().add(-7, "day"), dayjs()]); // Đặt mặc định theo yêu cầu
  const [viewMode, setViewMode] = useState("day");
  const [showWarning, setShowWarning] = useState(false);

  useEffect(() => {
    // Kiểm tra khoảng thời gian nếu viewMode là "day"
    const daysDiff = dateRange[1].diff(dateRange[0], "day");
    if (viewMode === "day" && daysDiff > 31) {
      setShowWarning(true);
    } else {
      setShowWarning(false);
    }

    dispatch(
      getSubordinateEmployeeStatus({
        startDate: dateRange[0].format("YYYY-MM-DD"),
        endDate: dateRange[1].format("YYYY-MM-DD"),
        viewMode: viewMode === "week" ? "week" : viewMode, // Giữ nguyên logic week dùng "week"
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
    let daysCounter = 0;

    while (
      (current.isBefore(end) || current.isSame(end)) &&
      (viewMode !== "day" || daysCounter < 31)
    ) {
      const title =
        viewMode === "day"
          ? current.format("DD/MM")
          : viewMode === "week"
          ? `Week ${current.isoWeek()} (${current
              .startOf("week")
              .format("DD/MM/YYYY")} - ${current
              .endOf("week")
              .format("DD/MM/YYYY")})` // Thêm năm vào tiêu đề
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
      if (viewMode === "day") daysCounter++;
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
      let daysCounter = 0;

      while (
        (current.isBefore(end) || current.isSame(end)) &&
        (viewMode !== "day" || daysCounter < 31)
      ) {
        const key =
          viewMode === "day"
            ? current.format("DD/MM")
            : viewMode === "week"
            ? `Week ${current.isoWeek()} (${current
                .startOf("week")
                .format("DD/MM/YYYY")} - ${current
                .endOf("week")
                .format("DD/MM/YYYY")})` // Thêm năm vào key
            : viewMode === "month"
            ? current.format("MM/YYYY")
            : current.format("YYYY");

        if (viewMode === "day") {
          const currentDate = current.format("YYYY-MM-DD");
          row[key] = emp.leaveDays.includes(currentDate) ? (
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
        if (viewMode === "day") daysCounter++;
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
      {showWarning && (
        <Alert
          message="Warning"
          description="The selected date range exceeds 31 days. Only the first 31 days will be displayed. Please switch to 'Month' or 'Year' view for a broader range."
          type="warning"
          showIcon
          style={{ marginBottom: "10px" }}
        />
      )}
      <Table
        columns={generateColumns()}
        dataSource={generateData()}
        pagination={false}
        locale={{ emptyText: "No data available or you lack permission." }}
        scroll={{ x: "max-content" }} // Thêm scroll ngang để xử lý nhiều cột
      />
    </div>
  );
};

export default AgendaEmployeeStatus;
