import { useState } from "react";
import {
  CalendarOutlined,
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
    key: "1",
    icon: <UserOutlined />,
  },
  {
    label: "Logout",
    key: "logout",
    icon: <UserOutlined />,
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

const items = [
  //   getItem("Option 1", "1", <PieChartOutlined />),
  //   getItem("Option 2", "2", <DesktopOutlined />),
  getItem("Leave", "sub1", <CalendarOutlined />, [
    getItem("My Leave Request", "3"),
    getItem("Leave approval", "4"),
    getItem("Agenda", "5"),
  ]),
  //   getItem("Team", "sub2", <TeamOutlined />, [
  //     getItem("Team 1", "6"),
  //     getItem("Team 2", "8"),
  //   ]),
  //   getItem("Files", "9", <FileOutlined />),
];

export default function HomeTemplate() {
  const [collapsed, setCollapsed] = useState(false);
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const navigate = useNavigate();
  let userInfo = JSON.parse(localStorage.getItem(USER_INFO));

  const handleMenuClick = (e) => {
    if (e.key === "logout") {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      navigate("/login");
    }
  };

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
              menu={{ items: userMenuItems, onClick: handleMenuClick }}
              trigger={["click"]}
            >
              <Button type="text">
                <Space>
                  <Avatar size="large" icon={<UserOutlined />} />
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
          <Breadcrumb
            style={{
              margin: "16px 0",
            }}
          >
            <Breadcrumb.Item>Home</Breadcrumb.Item>
            <Breadcrumb.Item>Leave</Breadcrumb.Item>
            <Breadcrumb.Item>My Leave</Breadcrumb.Item>
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
