import { useSelector } from "react-redux";
import stylingLoading from "./Loading.module.css";
import loading from "../../assets/img/loading.gif";

export default function Loading() {
  let { isLoading } = useSelector((state) => state.loadingReducer);
  return (
    <div>
      {isLoading ? (
        <div className={stylingLoading.bgLoading}>
          <img src={loading} alt="loading" />
        </div>
      ) : (
        ""
      )}
    </div>
  );
}
