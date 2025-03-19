# frontend/Dockerfile
# Stage 1: Build ứng dụng React với Vite
FROM node:22.14.0 AS builder
WORKDIR /app

# Chỉ sao chép package.json và package-lock.json trước
COPY package.json package-lock.json ./

# Cài đặt dependencies
RUN npm install

# Sao chép toàn bộ mã nguồn
COPY . .

# Build ứng dụng
RUN npm run build

# Stage 2: Serve ứng dụng bằng Nginx
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 5173
CMD ["nginx", "-g", "daemon off;"]