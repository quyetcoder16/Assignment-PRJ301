import { StrictMode } from "react";
import "@fontsource/roboto/300.css";
import "@fontsource/roboto/400.css";
import "@fontsource/roboto/500.css";
import "@fontsource/roboto/700.css";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";

// setup redux
import { store } from "./redux/configureStore.js";
import { Provider } from "react-redux";
import { ConfigProvider } from "antd";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <ConfigProvider
      theme={{
        token: {
          // Seed Token
          colorPrimary: "#008390",
          borderRadius: 2,

          // Alias Token
          colorBgContainer: "#f6ffed",
        },
      }}
    >
      <Provider store={store}>
        <App />
      </Provider>
    </ConfigProvider>
  </StrictMode>
);
