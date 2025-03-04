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
import {
  getAllLeaveApprovals,
  // approveLeaveRequest,
  // rejectLeaveRequest,
  // editLeaveRequestStatus,
} from "../../redux/reducer/leaveRequestReducer";

const { Option } = Select;
const { RangePicker } = DatePicker;

const LeaveApproval = () => {
  const dispatch = useDispatch();
  const { leaveApprovals } = useSelector((state) => state.leaveRequestReducer);

  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [actionType, setActionType] = useState("");
  const [filter, setFilter] = useState({
    creator: "",
    dateRange: [],
    status: "",
  });

  const [formConfirm] = Form.useForm();
  const [formEdit] = Form.useForm();

  useEffect(() => {
    dispatch(getAllLeaveApprovals());
  }, [dispatch]);

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

  const filteredData = leaveApprovals.filter((item) => {
    return (
      (!filter.creator || item.nameUserCreated.includes(filter.creator)) &&
      (!filter.status || item.nameRequestStatus === filter.status) &&
      (!filter.dateRange.length ||
        (dayjs(item.fromDate).isAfter(dayjs(filter.dateRange[0]), "day") &&
          dayjs(item.toDate).isBefore(dayjs(filter.dateRange[1]), "day")))
    );
  });

  const columns = [
    {
      title: "Created By",
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

      <div
        className="mb-3"
        style={{ display: "flex", gap: "10px", alignItems: "center" }}
      >
        <Input
          placeholder="Search by Creator"
          value={filter.creator}
          onChange={(e) => setFilter({ ...filter, creator: e.target.value })}
          style={{ width: "200px" }}
        />
        <RangePicker
          format="DD-MM-YYYY"
          onChange={(dates) => setFilter({ ...filter, dateRange: dates })}
        />
        <Select
          placeholder="Select Status"
          value={filter.status}
          onChange={(value) => setFilter({ ...filter, status: value })}
          allowClear
          style={{ width: "150px" }}
        >
          <Option value="In progress">In Progress</Option>
          <Option value="Approved">Approved</Option>
          <Option value="Rejected">Rejected</Option>
        </Select>
      </div>

      <Table columns={columns} dataSource={filteredData} rowKey="idRequest" />

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
