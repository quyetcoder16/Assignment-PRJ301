import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  visible: false,
  type: "",
  message: "",
  description: "",
};

const notificationReducer = createSlice({
  name: "notificationReducer",
  initialState,
  reducers: {
    showNotification: (state, action) => {
      const { type, message, description } = action.payload;
      state.visible = true;
      state.type = type;
      state.message = message;
      state.description = description;
    },
    hideNotification: (state) => {
      state.visible = false;
    },
  },
});

export const { showNotification, hideNotification } =
  notificationReducer.actions;

export default notificationReducer.reducer;
