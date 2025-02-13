import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import "../../assets/styles/NotFoundPage.css";
import NotFound from "../../assets/img/not_found.jpg";

const NotFoundPage = () => {
  const navigate = useNavigate();

  return (
    <div className="not-found-container">
      <img src={NotFound} alt="" />
      <p className="not-found-description">
        Sorry, we couldn’t find the page you are looking for
      </p>
      <Button
        variant="contained"
        className="not-found-button"
        onClick={() => navigate("/")}
      >
        Go to Home
      </Button>
    </div>
  );
};

export default NotFoundPage;
