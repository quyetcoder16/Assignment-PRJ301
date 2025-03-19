import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173, // Đảm bảo port này khớp với môi trường local
    host: true, // Cho phép truy cập từ ngoài local
  },
});
