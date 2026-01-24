import axios from "axios";
import useAuthStore from "./store/AuthStore";

const baseURL = "http://localhost:8080";

const axiosInstance = axios.create({
  baseURL,
  timeout: 10000,
});

/* =========================
   REQUEST INTERCEPTOR
========================= */
axiosInstance.interceptors.request.use((req) => {
  // Skip auth endpoints
  if (req.url?.startsWith("/auth/v1")) {
    return req;
  }

  const accessToken = useAuthStore.getState().authToken;

  if (accessToken) {
    req.headers.Authorization = `Bearer ${accessToken}`;
  }

  return req;
});

/* =========================
   RESPONSE INTERCEPTOR
========================= */
axiosInstance.interceptors.response.use(
  (response) => response,

  async (error) => {
    const originalRequest = error.config;

    // Defensive checks
    if (!error.response) {
      return Promise.reject(error);
    }

    const { status, data } = error.response;

    // ✅ Handle expired / invalid access token
    if (
      status === 401 &&
      data?.code === "AUTH_TOKEN_NOT_VALID" &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;

      try {
        // 🔁 Call refresh endpoint
        const refreshResponse = await axiosInstance.post(
          "/auth/v1/refresh",
          {},
          { withCredentials: true }
        );

        const newAccessToken = refreshResponse.data.payload;

        // ✅ Store new token in Zustand
        useAuthStore.getState().authenticateUser(newAccessToken);

        // ✅ Attach new token and retry original request
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

        return axiosInstance(originalRequest);
      } catch (refreshError) {
        // ❌ Refresh failed → logout user
        useAuthStore.getState().logOutUser();
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export { axiosInstance, baseURL };
