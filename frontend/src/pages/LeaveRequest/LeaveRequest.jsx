import { useEffect, useState } from "react";
import { myLeaveRequestService } from "../../service/myLeaveRequestService";

const LeaveRequest = () => {
  const [leaveRequests, setLeaveRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchLeaveRequests = async () => {
      try {
        const response = await myLeaveRequestService.getAllMyLeaveRequestAPi();
        console.log(response);
        setLeaveRequests(response.data); // Giả sử API trả về danh sách đơn nghỉ phép
      } catch (err) {
        setError("Failed to fetch leave requests");
      } finally {
        setLoading(false);
      }
    };

    fetchLeaveRequests();
  }, []);

  return (
    <div>
      <h1>My Leave Request</h1>
      <p>Danh sách các đơn nghỉ phép của bạn sẽ xuất hiện ở đây.</p>
    </div>
  );
};

export default LeaveRequest;
