import React, { useState } from "react";
import { Table, DatePicker, Select, Tag } from "antd";
import dayjs from "dayjs";
import isoWeek from "dayjs/plugin/isoWeek";

const { RangePicker } = DatePicker;
const { Option } = Select;
dayjs.extend(isoWeek);

// Dữ liệu mẫu
const employees = [
  {
    id: 1,
    name: "Nguyen Van A",
    leaveDays: ["2025-03-05", "2025-03-07", "2025-03-09"],
  },
  { id: 2, name: "Tran Thi B", leaveDays: ["2025-03-06", "2025-03-10"] },
];

const AgendaEmployeeStatus = () => {
  const [dateRange, setDateRange] = useState([dayjs(), dayjs().add(7, "day")]);
  const [viewMode, setViewMode] = useState("day");

  // Tạo danh sách cột tương ứng
  const generateColumns = () => {
    const columns = [{ title: "Employee", dataIndex: "name", key: "name" }];

    let start = dateRange[0].startOf(viewMode);
    let end = dateRange[1].endOf(viewMode);
    let current = start;

    while (current.isBefore(end) || current.isSame(end)) {
      const title =
        viewMode === "day"
          ? current.format("DD/MM")
          : viewMode === "week"
          ? `Week ${current.isoWeek()}`
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

  // Tạo dữ liệu hiển thị cho bảng
  const generateData = () => {
    return employees.map((emp) => {
      let row = { key: emp.id, name: emp.name };
      let start = dateRange[0].startOf(viewMode);
      let end = dateRange[1].endOf(viewMode);
      let current = start;

      while (current.isBefore(end) || current.isSame(end)) {
        const key =
          viewMode === "day"
            ? current.format("DD/MM")
            : viewMode === "week"
            ? `Week ${current.isoWeek()}`
            : viewMode === "month"
            ? current.format("MM/YYYY")
            : current.format("YYYY");

        if (viewMode === "day") {
          row[key] = emp.leaveDays.includes(current.format("YYYY-MM-DD")) ? (
            <Tag color="red">Off</Tag>
          ) : (
            ""
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
            leaveCount > 0 ? <Tag color="red">{leaveCount} days</Tag> : "";
        }
        current = current.add(1, viewMode);
      }
      return row;
    });
  };

  return (
    <div className="container mt-4">
      <h1>Agenda - Employee Status</h1>
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
          onChange={(dates) => setDateRange(dates)}
        />
        <Select value={viewMode} onChange={(value) => setViewMode(value)}>
          <Option value="day">Day</Option>
          <Option value="week">Week</Option>
          <Option value="month">Month</Option>
          <Option value="year">Year</Option>
        </Select>
      </div>
      <Table
        columns={generateColumns()}
        dataSource={generateData()}
        pagination={false}
      />
    </div>
  );
};

export default AgendaEmployeeStatus;
