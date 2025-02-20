import axios from "axios";
import { DOMAIN } from "../utils/setting/config";

const loginAPI = async (email, password) => {
  return await axios({
    url: `${DOMAIN}/auth/login`,
    method: "POST",
    data: {
      email,
      password,
    },
  });
};

export const authServicer = {
  loginAPI,
};
