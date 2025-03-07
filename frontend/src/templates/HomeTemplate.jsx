import { useState } from "react";
import {
  CalendarOutlined,
  LogoutOutlined,
  //   DesktopOutlined,
  //   FileOutlined,
  //   PieChartOutlined,
  //   TeamOutlined,
  //   UserOutlined,
} from "@ant-design/icons";
import {
  Avatar,
  Breadcrumb,
  Layout,
  Menu,
  theme,
  Button,
  Dropdown,
  Space,
} from "antd";
import { Outlet, useNavigate } from "react-router-dom";
const { Header, Content, Footer, Sider } = Layout;

import { DownOutlined, UserOutlined } from "@ant-design/icons";
import "../assets/styles/HomeTemplate.css";

const userMenuItems = [
  {
    label: "Profile",
    key: "profile",
    icon: <UserOutlined />,
  },
  {
    label: "Logout",
    key: "logout",
    icon: <LogoutOutlined />,
    danger: true,
  },
];

function getItem(label, key, icon, children) {
  return {
    key,
    icon,
    children,
    label,
  };
}

import logo from "../assets/img/logo.png";
import { USER_INFO } from "../utils/setting/config";
import { useDispatch } from "react-redux";
import { logoutUser } from "../redux/reducer/authReducer";

const items = [
  {
    key: "sub1",
    icon: <CalendarOutlined />,
    label: "Leave",
    children: [
      { key: "my-leave-request", label: "My Leave Request" },
      { key: "leave-approval", label: "Leave Approval" },
      { key: "agenda", label: "Agenda" },
    ],
  },
  // {
  //   key: "employee-management",
  //   icon: <UserOutlined />,
  //   label: "Employee Management",
  //   // children: [
  //   //   { key: "my-leave-request", label: "My Leave Request" },
  //   //   { key: "leave-approval", label: "Leave Approval" },
  //   //   { key: "agenda", label: "Agenda" },
  //   // ],
  // },
];

export default function HomeTemplate() {
  const [collapsed, setCollapsed] = useState(false);
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const handleMenuClick = ({ key }) => {
    navigate(`/${key}`);
  };

  const navigate = useNavigate();
  const dispatch = useDispatch();
  let userInfo = JSON.parse(localStorage.getItem(USER_INFO));

  const handleUserMenuClick = (e) => {
    if (e.key === "logout") {
      try {
        dispatch(logoutUser());
      } catch (error) {}
    } else if (e.key === "profile") {
      navigate("/");
    }
  };

  const currentPath = location.pathname.substring(1);
  const selectedKeys = [currentPath];

  const pathSnippets = location.pathname.split("/").filter((i) => i);
  const breadcrumbItems = [
    "Home",
    ...pathSnippets.map((path) => path.replace("-", " ")),
  ];

  return (
    <Layout
      style={{
        minHeight: "100vh",
      }}
    >
      <Sider
        style={{ background: "#008390" }}
        collapsible
        collapsed={collapsed}
        onCollapse={(value) => setCollapsed(value)}
      >
        <div className="demo-logo-vertical text-center">
          <img className="w-50" src={logo} alt="" />
        </div>
        <Menu
          theme="light"
          defaultSelectedKeys={["1"]}
          mode="inline"
          items={items}
          onClick={handleMenuClick}
          selectedKeys={selectedKeys}
        />
      </Sider>
      <Layout>
        <Header
          style={{ padding: 0, background: colorBgContainer }}
          className="custom-header"
        >
          <div
            style={{
              display: "flex",
              justifyContent: "flex-end",
              alignItems: "center",
              paddingRight: 20,
            }}
          >
            <Dropdown
              menu={{ items: userMenuItems, onClick: handleUserMenuClick }}
              trigger={["click"]}
            >
              <Button type="text">
                <Space>
                  <Avatar
                    src={`https://i.pravatar.cc/150?img=${userInfo?.userId}`}
                    size="large"
                    icon={<UserOutlined />}
                  />
                  Hi, {userInfo?.fullName} <DownOutlined />
                </Space>
              </Button>
            </Dropdown>
          </div>
        </Header>
        <Content
          style={{
            margin: "0 16px",
          }}
        >
          <Breadcrumb style={{ margin: "16px 0" }}>
            {breadcrumbItems.map((item, index) => (
              <Breadcrumb.Item key={index}>{item}</Breadcrumb.Item>
            ))}
          </Breadcrumb>

          <div
            style={{
              padding: 24,
              minHeight: 360,
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
            }}
          >
            <Outlet />
          </div>
        </Content>
        <Footer
          style={{
            textAlign: "center",
          }}
        >
          Assignment PRJ301
        </Footer>
      </Layout>
    </Layout>
  );
}
