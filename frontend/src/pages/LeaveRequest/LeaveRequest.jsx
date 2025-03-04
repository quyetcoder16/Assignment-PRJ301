import {
  Table,
  Tag,
  Button,
  Modal,
  Form,
  Input,
  DatePicker,
  Select,
} from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  getAllMyLeaveRequest,
  createLeaveRequest,
  getAllLeaveTypes,
  //  updateLeaveRequest, // ✅ API cập nhật
} from "../../redux/reducer/leaveRequestReducer";
import dayjs from "dayjs";

const { Option } = Select;
const { RangePicker } = DatePicker;

const LeaveRequest = () => {
  const dispatch = useDispatch();
  const { leaveRequests, leaveTypes } = useSelector(
    (state) => state.leaveRequestReducer
  );

  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false);
  const [formCreate] = Form.useForm();
  const [formUpdate] = Form.useForm();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null); // ✅ Lưu đơn cần update

  useEffect(() => {
    dispatch(getAllMyLeaveRequest());
    dispatch(getAllLeaveTypes());
  }, [dispatch]);

  // ===========================
  // Table Columns
  // ===========================
  const columns = [
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
    { title: "Type", dataIndex: "nameTypeLeave", key: "type" },
    {
      title: "Processor",
      dataIndex: "nameUserProcess",
      key: "processor",
      render: (text) => (text ? text : "-"),
    },
    {
      title: "Actions",
      key: "actions",
      render: (_, record) =>
        record.nameRequestStatus === "In progress" &&
        dayjs().isBefore(dayjs(record.fromDate), "day") && (
          <Button type="link" onClick={() => openUpdateModal(record)}>
            <i className="fa-solid fa-pen-to-square"></i>
          </Button>
        ),
    },
  ];

  // ===========================
  // Create Leave Request
  // ===========================
  const handleCreateRequest = async (values) => {
    setIsSubmitting(true);
    const requestData = {
      title: values.title,
      reason: values.reason,
      fromDate: values.dateRange[0].format("YYYY-MM-DD"),
      toDate: values.dateRange[1].format("YYYY-MM-DD"),
      idTypeRequest: values.idTypeRequest,
    };

    try {
      await dispatch(createLeaveRequest(requestData));
      formCreate.resetFields();
      setIsCreateModalOpen(false);
    } finally {
      setIsSubmitting(false);
    }
  };

  // ===========================
  // Open Update Modal
  // ===========================
  const openUpdateModal = (request) => {
    setSelectedRequest(request);
    // console.log(request);
    const matchedType = leaveTypes.find(
      (type) => type.name === request.nameTypeLeave
    );
    formUpdate.setFieldsValue({
      title: request.title,
      reason: request.reason,
      dateRange: [dayjs(request.fromDate), dayjs(request.toDate)],
      idTypeRequest: matchedType ? matchedType.id : undefined,
    });
    setIsUpdateModalOpen(true);
  };

  // ===========================
  // Update Leave Request
  // ===========================
  const handleUpdateRequest = async (values) => {
    setIsSubmitting(true);
    const updatedData = {
      idRequest: selectedRequest.idRequest, // ✅ ID đơn cần cập nhật
      title: values.title,
      reason: values.reason,
      fromDate: values.dateRange[0].format("YYYY-MM-DD"),
      toDate: values.dateRange[1].format("YYYY-MM-DD"),
      idTypeRequest: values.idTypeRequest,
    };

    try {
      //await dispatch(updateLeaveRequest(updatedData));
      setIsUpdateModalOpen(false);
      setSelectedRequest(null);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container mt-4">
      <h1 className="text-center">My Leave Requests</h1>
      <Button
        type="primary"
        className="mb-3"
        onClick={() => {
          formCreate.resetFields();
          setIsCreateModalOpen(true);
        }}
      >
        Create New Request
      </Button>

      <Table columns={columns} dataSource={leaveRequests} rowKey="idRequest" />

      {/* ========== Create Modal ========== */}
      <Modal
        title="Create New Leave Request"
        open={isCreateModalOpen}
        onCancel={() => setIsCreateModalOpen(false)}
        footer={null}
      >
        <Form
          form={formCreate}
          layout="vertical"
          onFinish={handleCreateRequest}
          disabled={isSubmitting}
        >
          <Form.Item
            name="title"
            label="Title"
            rules={[{ required: true, message: "Please enter title" }]}
          >
            <Input placeholder="Enter title" />
          </Form.Item>

          <Form.Item
            name="reason"
            label="Reason"
            rules={[{ required: true, message: "Please enter reason" }]}
          >
            <Input.TextArea placeholder="Enter reason" rows={3} />
          </Form.Item>

          <Form.Item
            name="dateRange"
            label="Select Date Range"
            rules={[{ required: true, message: "Please select date range" }]}
          >
            <RangePicker format="DD-MM-YYYY" />
          </Form.Item>

          <Form.Item
            name="idTypeRequest"
            label="Type of Leave"
            rules={[{ required: true, message: "Please select leave type" }]}
          >
            <Select placeholder="Select leave type">
              {leaveTypes.map((type) => (
                <Option key={type.id} value={type.id}>
                  {type.name}
                </Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" className="w-100">
              Submit Request
            </Button>
          </Form.Item>
        </Form>
      </Modal>

      {/* ========== Update Modal ========== */}
      <Modal
        title="Update Leave Request"
        open={isUpdateModalOpen}
        onCancel={() => setIsUpdateModalOpen(false)}
        footer={null}
      >
        <Form
          form={formUpdate}
          layout="vertical"
          onFinish={handleUpdateRequest}
          disabled={isSubmitting}
        >
          <Form.Item name="title" label="Title">
            <Input />
          </Form.Item>

          <Form.Item name="reason" label="Reason">
            <Input.TextArea rows={3} />
          </Form.Item>

          <Form.Item name="dateRange" label="Select Date Range">
            <RangePicker format="DD-MM-YYYY" />
          </Form.Item>

          <Form.Item name="idTypeRequest" label="Type of Leave">
            <Select>
              {leaveTypes.map((type) => (
                <Option key={type.id} value={type.id}>
                  {type.name}
                </Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit">
              Update Request
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default LeaveRequest;
