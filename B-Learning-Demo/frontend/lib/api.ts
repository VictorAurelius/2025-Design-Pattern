/**
 * API Client
 * ===========
 *
 * Axios client để gọi backend API.
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

import axios from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8000';

// Create axios instance
const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10s
});

// Request interceptor (có thể thêm auth token ở đây)
apiClient.interceptors.request.use(
  (config) => {
    // TODO: Add auth token if needed
    // config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor (xử lý errors)
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // Server trả về error response
      console.error('API Error:', error.response.data);
    } else if (error.request) {
      // Request được gửi nhưng không nhận được response
      console.error('Network Error:', error.message);
    } else {
      // Lỗi khi setup request
      console.error('Request Error:', error.message);
    }
    return Promise.reject(error);
  }
);

export default apiClient;
