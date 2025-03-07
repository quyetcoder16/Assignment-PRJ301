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
  Col,
  Row,
} from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import dayjs from "dayjs";
import {
  getAllLeaveApprovals,
  getAllLeaveTypes,
  // approveLeaveRequest,
  // rejectLeaveRequest,
  // editLeaveRequestStatus,
} from "../../redux/reducer/leaveRequestReducer";
import { useLocation, useNavigate } from "react-router-dom";

const { Option } = Select;
const { RangePicker } = DatePicker;

const LeaveApproval = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const { leaveApprovals, leaveTypes, totalLeaveApproval } = useSelector(
    (state) => state.leaveRequestReducer
  );

  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [actionType, setActionType] = useState("");

  // Hàm để parse query parameters từ URL
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
      employeeName: searchParams.get("employeeName") || "",
    };
  };
  const [filters, setFilters] = useState(getFiltersFromUrl());

  const [formConfirm] = Form.useForm();
  const [formEdit] = Form.useForm();
  const [formSearch] = Form.useForm();

  // Cập nhật URL khi filters thay đổi
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
    dispatch(getAllLeaveApprovals(filters));
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
  }, [dispatch, formSearch, filters]);

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

  // Xử lý tìm kiếm
  const handleSearch = (values) => {
    const {
      createdDateRange,
      leaveDateRange,
      leaveTypeId,
      statusId,
      employeeName,
    } = values;
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
      employeeName: employeeName || "",
    };
    setFilters(newFilters);
    updateUrl(newFilters);
  };

  // Reset bộ lọc
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

  const openConfirmModal = (record, type) => {
    setSelectedRequest(record);
    setActionType(type);
    formConfirm.resetFields();
    setIsConfirmModalOpen(true);
  };

  const handleConfirmAction = async (values) => {
    const requestData = {
      idRequest: selectedRequest.idRequest,
      noteProcess: values.noteProcess,
    };

    if (actionType === "approve") {
      // await dispatch(approveLeaveRequest(requestData));
      message.success("Leave request approved!");
    } else {
      // await dispatch(rejectLeaveRequest(requestData));
      message.success("Leave request rejected!");
    }

    setIsConfirmModalOpen(false);
    dispatch(getAllLeaveApprovals());
  };

  const openEditModal = (record) => {
    setSelectedRequest(record);
    formEdit.setFieldsValue({
      status: record.nameRequestStatus,
      noteProcess: "",
    });
    setIsEditModalOpen(true);
  };

  const handleEditStatus = async (values) => {
    const requestData = {
      idRequest: selectedRequest.idRequest,
      newStatus: values.status,
      noteProcess: values.noteProcess,
    };

    // await dispatch(editLeaveRequestStatus(requestData));
    message.success("Leave request status updated!");

    setIsEditModalOpen(false);
    dispatch(getAllLeaveApprovals());
  };

  const columns = [
    {
      title: "Created By",
      dataIndex: "employeeCreated",
      key: "employeeCreated",
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
                  onClick={() => openConfirmModal(record, "approve")}
                >
                  Approve
                </Button>
                <Button
                  danger
                  className="ml-2"
                  onClick={() => openConfirmModal(record, "reject")}
                >
                  Reject
                </Button>
              </>
            )}
            {(record.nameRequestStatus === "Approved" ||
              record.nameRequestStatus === "Rejected") &&
              currentDate.isBefore(fromDate) && (
                <Button className="ml-2" onClick={() => openEditModal(record)}>
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
      <h1>Leave Approvals</h1>

      <Form form={formSearch} layout="vertical" onFinish={handleSearch}>
        <Row gutter={16}>
          <Col span={3}>
            <Form.Item name="employeeName" label="Employee Name">
              <Input placeholder="Enter employee name" />
            </Form.Item>
          </Col>
          <Col span={5}>
            <Form.Item name="createdDateRange" label="Created Date Range">
              <RangePicker format="DD-MM-YYYY" style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={5}>
            <Form.Item name="leaveDateRange" label="Leave Date Range">
              <RangePicker format="DD-MM-YYYY" style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={3}>
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
          <Col span={3}>
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
        dataSource={leaveApprovals}
        rowKey="idRequest"
        pagination={{
          pageSize: filters.size,
          total: totalLeaveApproval,
          current: filters.page + 1,
        }}
        onChange={handleTableChange}
      />

      {/* Modal Confirm Approve/Reject */}
      <Modal
        title={
          actionType === "approve"
            ? "Approve Leave Request"
            : "Reject Leave Request"
        }
        open={isConfirmModalOpen}
        onCancel={() => setIsConfirmModalOpen(false)}
        footer={null}
      >
        <Form
          form={formConfirm}
          layout="vertical"
          onFinish={handleConfirmAction}
        >
          <Form.Item
            name="noteProcess"
            label="Note"
            rules={[{ required: true, message: "Please enter a note" }]}
          >
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              Confirm
            </Button>
          </Form.Item>
        </Form>
      </Modal>

      {/* Modal Edit Status */}
      <Modal
        title="Edit Leave Request Status"
        open={isEditModalOpen}
        onCancel={() => setIsEditModalOpen(false)}
        footer={null}
      >
        <Form form={formEdit} layout="vertical" onFinish={handleEditStatus}>
          <Form.Item
            name="status"
            label="Status"
            rules={[{ required: true, message: "Please select a status" }]}
          >
            <Select>
              <Option value="Approved">Approved</Option>
              <Option value="Rejected">Rejected</Option>
              <Option value="In progress">In Progress</Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="noteProcess"
            label="Note"
            rules={[{ required: true, message: "Please enter a note" }]}
          >
            <Input.TextArea rows={3} />
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
