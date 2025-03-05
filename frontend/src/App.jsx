import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/login/LoginPage";
import NotFoundPage from "./pages/notfound/NotFoundPage";
import HomeTemplate from "./templates/HomeTemplate";
import LeaveRequest from "./pages/LeaveRequest/LeaveRequest";
import Loading from "./components/GlobalSetting/Loading";
import NotificationHandler from "./components/Notification/NotificationHandler";
import ProtectedRoute from "./routes/ProtectedRoute";
import LeaveApproval from "./pages/leave_approval/LeaveApproval";
import AgendaEmployeeStatus from "./pages/agenda/AgendaEmployeeStatus";
import Profile from "./pages/profile/Profile";
import ForgotPassword from "./pages/auth/ForgotPassword";
import EmployeeManagement from "./pages/employee_management/EmployeeManagement";

function App() {
  return (
    <>
      <NotificationHandler />
      <Loading />
      <Router>
        <Routes>
          <Route element={<ProtectedRoute />}>
            <Route path="/" element={<HomeTemplate />}>
              <Route path="/my-leave-request" element={<LeaveRequest />} />
              <Route path="/leave-approval" element={<LeaveApproval />} />
              <Route path="/agenda" element={<AgendaEmployeeStatus />} />
              <Route path="/" element={<Profile />} />
              <Route
                path="/employee-management"
                element={<EmployeeManagement />}
              />
            </Route>
          </Route>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
