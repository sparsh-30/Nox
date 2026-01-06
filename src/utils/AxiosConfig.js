import axios from "axios";

const baseURL = "http://localhost:8080";

const axiosInstance = axios.create({
  baseURL: baseURL,
  timeout: 10000,
});

export { axiosInstance, baseURL };
