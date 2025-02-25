import {
  Table,
  Tag,
  Button,
  Modal,
  Form,
  Input,
  DatePicker,
  Select,
  message,
} from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import dayjs from "dayjs";
import { getAllLeaveApprovals } from "../../redux/reducer/leaveRequestReducer";

const { Option } = Select;
const { RangePicker } = DatePicker;

const LeaveApproval = () => {
  const dispatch = useDispatch();
  const { leaveApprovals } = useSelector((state) => state.leaveRequestReducer);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingRequest, setEditingRequest] = useState(null);
  const [form] = Form.useForm();

  useEffect(() => {
    dispatch(getAllLeaveApprovals());
  }, [dispatch]);

  // const handleApprove = (id) => {
  //   dispatch(approveLeaveRequest(id)).then(() => {
  //     message.success("Leave request approved!");
  //     dispatch(getAllLeaveApprovals());
  //   });
  // };

  // const handleReject = (id) => {
  //   dispatch(rejectLeaveRequest(id)).then(() => {
  //     message.success("Leave request rejected!");
  //     dispatch(getAllLeaveApprovals());
  //   });
  // };

  // const handleEdit = (record) => {
  //   setEditingRequest(record);
  //   form.setFieldsValue({
  //     title: record.title,
  //     reason: record.reason,
  //     dateRange: [dayjs(record.fromDate), dayjs(record.toDate)],
  //     idTypeRequest: record.idTypeRequest,
  //   });
  //   setIsModalOpen(true);
  // };

  // const handleSaveEdit = async (values) => {
  //   const requestData = {
  //     ...editingRequest,
  //     title: values.title,
  //     reason: values.reason,
  //     fromDate: values.dateRange[0].format("YYYY-MM-DD"),
  //     toDate: values.dateRange[1].format("YYYY-MM-DD"),
  //     idTypeRequest: values.idTypeRequest,
  //   };
  //   await dispatch(editLeaveRequest(requestData));
  //   message.success("Leave request updated!");
  //   setIsModalOpen(false);
  //   dispatch(getAllLeaveApprovals());
  // };

  const columns = [
    {
      title: "Created By ",
      dataIndex: "nameUserCreated",
      key: "nameUserCreated",
    },

    { title: "Title", dataIndex: "title", key: "title" },
    { title: "Reason", dataIndex: "reason", key: "reason" },
    {
      title: "From Date",
      dataIndex: "fromDate",
      key: "fromDate",
      render: (text) => dayjs(text).format("DD-MM-YYYY"),
    },
    {
      title: "To Date",
      dataIndex: "toDate",
      key: "toDate",
      render: (text) => dayjs(text).format("DD-MM-YYYY"),
    },
    { title: "Type", dataIndex: "nameTypeLeave", key: "type" },
    {
      title: "Status",
      dataIndex: "nameRequestStatus",
      key: "status",
      render: (status) => (
        <Tag
          color={
            status === "Approved"
              ? "green"
              : status === "In progress"
              ? "blue"
              : "red"
          }
        >
          {status}
        </Tag>
      ),
    },
    {
      title: "Actions",
      key: "actions",
      render: (record) => {
        const currentDate = dayjs();
        const fromDate = dayjs(record.fromDate);
        return (
          <>
            {record.nameRequestStatus === "In progress" && (
              <>
                <Button
                  type="primary"
                  // onClick={() => handleApprove(record.idRequest)}
                >
                  Approve
                </Button>
                <Button
                  danger
                  className="ml-2"
                  // onClick={() => handleReject(record.idRequest)}
                >
                  Reject
                </Button>
              </>
            )}
            {(record.nameRequestStatus === "Approved" ||
              record.nameRequestStatus === "Rejected") &&
              currentDate.isBefore(fromDate.add(1, "day")) && (
                <Button
                // onClick={() => handleEdit(record)}
                >
                  Edit
                </Button>
              )}
          </>
        );
      },
    },
  ];

  return (
    <div className="container mt-4">
      <h1 className="text-center">Leave Approvals</h1>
      <Table columns={columns} dataSource={leaveApprovals} rowKey="idRequest" />
      <Modal
        title="Edit Leave Request"
        open={isModalOpen}
        onCancel={() => setIsModalOpen(false)}
        footer={null}
      >
        <Form
          form={form}
          layout="vertical"
          // onFinish={handleSaveEdit}
        >
          <Form.Item name="title" label="Title" rules={[{ required: true }]}>
            <Input placeholder="Enter title" />
          </Form.Item>
          <Form.Item name="reason" label="Reason" rules={[{ required: true }]}>
            <Input.TextArea placeholder="Enter reason" rows={3} />
          </Form.Item>
          <Form.Item
            name="dateRange"
            label="Date Range"
            rules={[{ required: true }]}
          >
            <RangePicker format="DD-MM-YYYY" />
          </Form.Item>
          <Form.Item
            name="idTypeRequest"
            label="Type of Leave"
            rules={[{ required: true }]}
          >
            <Select placeholder="Select leave type">
              {/* Giả sử leave types được lưu ở store */}
              {leaveApprovals.map((type) => (
                <Option key={type.id} value={type.id}>
                  {type.nameTypeLeave}
                </Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              Save Changes
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default LeaveApproval;
