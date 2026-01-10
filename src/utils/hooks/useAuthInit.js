import { useEffect } from "react";
import { axiosInstance } from "../AxiosConfig";
import useAuthStore from "../store/AuthStore";
import useUserInfoStore from "../store/UserInfoStore";

const useAuthInit = () => {
  const authenticateUser = useAuthStore((state) => state.authenticateUser);
  const updateUserInfo = useUserInfoStore((state) => state.updateUserInfo);
  const removeUserInfo = useUserInfoStore((state) => state.removeUserInfo);

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
          updateUserInfo(response.data.payload);
          authenticateUser(response.data.payload);
        }
      } catch (error) {
        if (isMounted) {
          removeUserInfo();
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
