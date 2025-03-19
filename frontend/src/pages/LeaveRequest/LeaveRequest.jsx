import {
  Table,
  Tag,
  Button,
  Modal,
  Form,
  Input,
  DatePicker,
  Select,
  Row,
  Col,
  Tooltip,
} from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getAllMyLeaveRequest,
  createLeaveRequest,
  getAllLeaveTypes,
  updateLeaveRequest,
  deleteLeaveRequest,
} from "../../redux/reducer/leaveRequestReducer";
import dayjs from "dayjs";

const { Option } = Select;
const { RangePicker } = DatePicker;

const LeaveRequest = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const { leaveRequests, leaveTypes, totalLeaveRequest } = useSelector(
    (state) => state.leaveRequestReducer
  );
  const { user } = useSelector((state) => state.authReducer);

  const getFiltersFromUrl = () => {
    const searchParams = new URLSearchParams(location.search);
    return {
      page: parseInt(searchParams.get("page")) || 0,
      size: parseInt(searchParams.get("size")) || 10,
      startCreatedAt: searchParams.get("startCreatedAt") || "",
      endCreatedAt: searchParams.get("endCreatedAt") || "",
      leaveDateStart: searchParams.get("leaveDateStart") || "",
      leaveDateEnd: searchParams.get("leaveDateEnd") || "",
      leaveTypeId: parseInt(searchParams.get("leaveTypeId")) || 0,
      statusId: parseInt(searchParams.get("statusId")) || 0,
      sort: searchParams.get("sort") || "idRequest,desc",
    };
  };

  const [filters, setFilters] = useState(getFiltersFromUrl());
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false); // Thêm state cho modal xóa
  const [formCreate] = Form.useForm();
  const [formUpdate] = Form.useForm();
  const [formSearch] = Form.useForm();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [requestToDelete, setRequestToDelete] = useState(null); // Lưu request cần xóa

  const updateUrl = (newFilters) => {
    const searchParams = new URLSearchParams();
    Object.entries(newFilters).forEach(([key, value]) => {
      if (value !== "" && value !== 0) {
        searchParams.set(key, value);
      }
    });
    navigate(`${location.pathname}?${searchParams.toString()}`, {
      replace: true,
    });
  };

  useEffect(() => {
    dispatch(getAllLeaveTypes());
    dispatch(getAllMyLeaveRequest(filters));
    formSearch.setFieldsValue({
      createdDateRange:
        filters.startCreatedAt && filters.endCreatedAt
          ? [dayjs(filters.startCreatedAt), dayjs(filters.endCreatedAt)]
          : null,
      leaveDateRange:
        filters.leaveDateStart && filters.leaveDateEnd
          ? [dayjs(filters.leaveDateStart), dayjs(filters.leaveDateEnd)]
          : null,
      leaveTypeId: filters.leaveTypeId || undefined,
      statusId: filters.statusId || undefined,
    });
  }, [dispatch, filters, formSearch]);

  const handleTableChange = (pagination, _, sorter) => {
    const newFilters = {
      ...filters,
      page: pagination.current - 1,
      size: pagination.pageSize,
      sort: sorter.order === "ascend" ? "idRequest,asc" : "idRequest,desc",
    };
    setFilters(newFilters);
    updateUrl(newFilters);
  };

  const handleSearch = (values) => {
    const { createdDateRange, leaveDateRange, leaveTypeId, statusId } = values;
    const newFilters = {
      ...filters,
      page: 0,
      startCreatedAt: createdDateRange
        ? createdDateRange[0].format("YYYY-MM-DD") + "T00:00:00"
        : "",
      endCreatedAt: createdDateRange
        ? createdDateRange[1].format("YYYY-MM-DD") + "T23:59:59"
        : "",
      leaveDateStart: leaveDateRange
        ? leaveDateRange[0].format("YYYY-MM-DD")
        : "",
      leaveDateEnd: leaveDateRange
        ? leaveDateRange[1].format("YYYY-MM-DD")
        : "",
      leaveTypeId: leaveTypeId || 0,
      statusId: statusId || 0,
    };
    setFilters(newFilters);
    updateUrl(newFilters);
  };

  const handleResetFilters = () => {
    formSearch.resetFields();
    const newFilters = {
      page: 0,
      size: 10,
      startCreatedAt: "",
      endCreatedAt: "",
      leaveDateStart: "",
      leaveDateEnd: "",
      leaveTypeId: 0,
      statusId: 0,
      sort: "idRequest,desc",
    };
    setFilters(newFilters);
    updateUrl(newFilters);
  };

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
      render: (status, record) => (
        <Tooltip title={record.noteProcess || "No note available"}>
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
        </Tooltip>
      ),
    },
    { title: "Type", dataIndex: "nameTypeLeave", key: "type" },
    {
      title: "Processor",
      dataIndex: "employeeProcess",
      key: "processor",
      render: (text) => (text ? text : "-"),
    },
    {
      title: "Actions",
      key: "actions",
      render: (_, record) =>
        record.nameRequestStatus === "In progress" &&
        dayjs().isBefore(dayjs(record.fromDate).add(1, "day"), "day") && (
          <>
            <Button type="link" onClick={() => openUpdateModal(record)}>
              <i className="fa-solid fa-pen-to-square"></i>
            </Button>
            <Button type="link" danger onClick={() => openDeleteModal(record)}>
              <i className="fa-solid fa-trash"></i>
            </Button>
          </>
        ),
    },
    {
      title: "Created At",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (text) => (text ? dayjs(text).format("YYYY-MM-DD") : "-"),
    },
  ];

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

  const openUpdateModal = (request) => {
    setSelectedRequest(request);
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

  const handleUpdateRequest = async (values) => {
    setIsSubmitting(true);
    const updatedData = {
      idRequest: selectedRequest.idRequest,
      title: values.title,
      reason: values.reason,
      fromDate: values.dateRange[0].format("YYYY-MM-DD"),
      toDate: values.dateRange[1].format("YYYY-MM-DD"),
      idTypeRequest: values.idTypeRequest,
    };

    try {
      await dispatch(updateLeaveRequest(updatedData));
      setIsUpdateModalOpen(false);
      setSelectedRequest(null);
    } finally {
      setIsSubmitting(false);
    }
  };

  const openDeleteModal = (record) => {
    setRequestToDelete(record);
    setIsDeleteModalOpen(true);
  };

  const handleDeleteRequest = async () => {
    setIsSubmitting(true);
    try {
      await dispatch(deleteLeaveRequest(requestToDelete.idRequest));
      setIsDeleteModalOpen(false);
      setRequestToDelete(null);
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

      <Form form={formSearch} layout="vertical" onFinish={handleSearch}>
        <Row gutter={16}>
          <Col span={6}>
            <Form.Item name="createdDateRange" label="Created Date Range">
              <RangePicker format="DD-MM-YYYY" style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item name="leaveDateRange" label="Leave Date Range">
              <RangePicker format="DD-MM-YYYY" style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={4}>
            <Form.Item name="leaveTypeId" label="Leave Type">
              <Select placeholder="Select leave type" allowClear>
                <Option value={0}>All</Option>
                {leaveTypes.map((type) => (
                  <Option key={type.id} value={type.id}>
                    {type.name}
                  </Option>
                ))}
              </Select>
            </Form.Item>
          </Col>
          <Col span={4}>
            <Form.Item name="statusId" label="Status">
              <Select placeholder="Select status" allowClear>
                <Option value={0}>All</Option>
                <Option value={1}>In progress</Option>
                <Option value={2}>Approved</Option>
                <Option value={3}>Rejected</Option>
              </Select>
            </Form.Item>
          </Col>
          <Col span={4}>
            <Form.Item label=" ">
              <Button
                type="primary"
                htmlType="submit"
                style={{ width: "100%" }}
              >
                Search
              </Button>
              <Button
                onClick={handleResetFilters}
                style={{ width: "100%", marginTop: 8 }}
              >
                Reset
              </Button>
            </Form.Item>
          </Col>
        </Row>
      </Form>

      <Table
        columns={columns}
        dataSource={leaveRequests}
        rowKey="idRequest"
        pagination={{
          pageSize: filters.size,
          total: totalLeaveRequest,
          current: filters.page + 1,
        }}
        onChange={handleTableChange}
      />

      {/* Modal Create */}
      <Modal
        title="Create New Leave Request"
        open={isCreateModalOpen}
        onCancel={() => setIsCreateModalOpen(false)}
        footer={
          <span>
            This leave request will be sent to{" "}
            <span className="text-primary">{user?.direct_management}</span>
          </span>
        }
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

      {/* Modal Update */}
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
          <Form.Item
            name="title"
            label="Title"
            rules={[{ required: true, message: "Please enter title" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="reason"
            label="Reason"
            rules={[{ required: true, message: "Please enter reason" }]}
          >
            <Input.TextArea rows={3} />
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

      {/* Modal Confirm Delete */}
      <Modal
        title="Confirm Deletion"
        open={isDeleteModalOpen}
        onCancel={() => setIsDeleteModalOpen(false)}
        footer={[
          <Button key="cancel" onClick={() => setIsDeleteModalOpen(false)}>
            Cancel
          </Button>,
          <Button
            key="delete"
            type="primary"
            danger
            onClick={handleDeleteRequest}
            loading={isSubmitting}
          >
            Delete
          </Button>,
        ]}
      >
        <p>Are you sure you want to delete this leave request?</p>
        {requestToDelete && (
          <p>
            <strong>Title:</strong> {requestToDelete.title} <br />
            <strong>From:</strong>{" "}
            {dayjs(requestToDelete.fromDate).format("DD-MM-YYYY")} <br />
            <strong>To:</strong>{" "}
            {dayjs(requestToDelete.toDate).format("DD-MM-YYYY")}
          </p>
        )}
      </Modal>
    </div>
  );
};

export default LeaveRequest;
