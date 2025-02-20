import { Table, Tag } from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getAllMyLeaveRequest } from "../../redux/reducer/leaveRequestReducer";

const columns = [
  {
    title: "Title",
    dataIndex: "title",
    key: "title",
  },
  {
    title: "Reason",
    dataIndex: "reason",
    key: "reason",
  },
  {
    title: "From Date",
    dataIndex: "fromDate",
    key: "fromDate",
    render: (text) => new Date(text).toLocaleDateString("en-GB"),
  },
  {
    title: "To Date",
    dataIndex: "toDate",
    key: "toDate",
    render: (text) => new Date(text).toLocaleDateString("en-GB"),
  },
  {
    title: "Status",
    dataIndex: "nameRequestStatus",
    key: "status",
    render: (status) => {
      let color = status === "In progress" ? "blue" : "green";
      return <Tag color={color}>{status}</Tag>;
    },
  },
  {
    title: "Type",
    dataIndex: "nameTypeLeave",
    key: "type",
  },
  {
    title: "Processor",
    dataIndex: "nameUserProcess",
    key: "processor",
    render: (text) => (text ? text : "-"),
  },
];

const LeaveRequest = () => {
  const dispatch = useDispatch();
  const { leaveRequests } = useSelector((state) => state.leaveRequestReducer);

  useEffect(() => {
    dispatch(getAllMyLeaveRequest());
  }, [dispatch]);

  return (
    <div>
      <div className="container">
        <h1>My Leave Requests</h1>
        <Table
          columns={columns}
          dataSource={leaveRequests}
          rowKey="idRequest"
        />
      </div>
    </div>
  );
};

export default LeaveRequest;
