import { notification } from "antd";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { hideNotification } from "../../redux/reducer/notificationReducer";

export default function NotificationHandler() {
  const dispatch = useDispatch();
  const { visible, type, message, description } = useSelector(
    (state) => state.notificationReducer
  );
  const [api, contextHolder] = notification.useNotification();

  useEffect(() => {
    if (visible) {
      api[type]({
        message: message,
        description: description,
        duration: 3,
        placement: "topRight",
        showProgress: true,
        pauseOnHover: false,
      });
      dispatch(hideNotification());
    }
  }, [visible, type, message, description, dispatch]);
  return <div>{contextHolder}</div>;
}
