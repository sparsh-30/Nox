import { useEffect } from "react";
import { axiosInstance } from "../AxiosConfig";
import useAuthStore from "../store/AuthStore";

const useAuthInit = () => {
  const authenticateUser = useAuthStore((state) => state.authenticateUser);

  useEffect(() => {
    let isMounted = true;

    const refresh = async () => {
      try {
        const response = await axiosInstance.post(
          "/auth/v1/refresh",
          {},
          { withCredentials: true }
        );

        if (isMounted) {
          authenticateUser(response.data.payload);
        }
      } catch (error) {
        if (isMounted) {
          authenticateUser(null);
        }
      }
    };

    refresh();

    return () => {
      isMounted = false;
    };
  }, [authenticateUser]);
};

export default useAuthInit;
