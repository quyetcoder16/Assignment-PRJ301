import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/login/LoginPage";
import NotFoundPage from "./pages/notfound/NotFoundPage";
import HomeTemplate from "./templates/HomeTemplate";
import LeaveRequest from "./pages/LeaveRequest/LeaveRequest";
import Loading from "./components/GlobalSetting/Loading";
import NotificationHandler from "./components/Notification/NotificationHandler";
import ProtectedRoute from "./routes/ProtectedRoute";

function App() {
  return (
    <>
      <NotificationHandler />
      <Loading />
      <Router>
        <Routes>
          <Route element={<ProtectedRoute />}>
            <Route path="/" element={<HomeTemplate />}>
              <Route path="/leave-request" element={<LeaveRequest />} />
            </Route>
          </Route>
          <Route path="/login" element={<LoginPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
