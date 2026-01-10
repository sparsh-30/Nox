import axios from "axios";
import useAuthStore from "./store/AuthStore";

const baseURL = "http://localhost:8080";

const axiosInstance = axios.create({
  baseURL: baseURL,
  timeout: 10000,
});


// axiosInstance.interceptors.request.use((req) => {
//   if(req.url.startsWith("/auth/v1")){
//     return req;
//   }
//   const accessToken = useAuthStore.getState().authToken;
//   console.log(accessToken);
//   req.headers.Authorization =  accessToken ? `Bearer ${accessToken}` : '';
//   return req;
// });
// axiosInstance.interceptors.response.use(function onFulfilled(response) {
//     console.log(response)
//     return response;
//   }, function onRejected(error) {
//     console.log(error.response.data.code);
//     return Promise.reject(error);
//   });

export { axiosInstance, baseURL };
