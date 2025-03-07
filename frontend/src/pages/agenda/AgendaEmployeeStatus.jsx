import React, { useState, useEffect } from "react";
import { Table, DatePicker, Select, Tag } from "antd";
import { useDispatch, useSelector } from "react-redux";
import dayjs from "dayjs";
import isoWeek from "dayjs/plugin/isoWeek";
import { getSubordinateEmployeeStatus } from "../../redux/reducer/agendaReducer";

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
      })
    );
  }, [dispatch, dateRange]);

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
      let row = { key: emp.id, name: emp.name };
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
