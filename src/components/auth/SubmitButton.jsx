import React from "react";
import { useNavigate } from "react-router";
import useAuthStore from "../../utils/store/AuthStore";
import useUserInfoStore from "../../utils/store/UserInfoStore";
import { axiosInstance } from "../../../../frontend/src/utils/AxiosConfig";
import { successToast } from "../../utils/Toasts";

const SubmitButton = ({ clearFormValues, formValues, formType }) => {
  const authenticateUser = useAuthStore((state) => state.authenticateUser);
  const updateUserInfo = useUserInfoStore((state) => state.updateUserInfo);
  const navigate = useNavigate();
  const handleFormSubmit = () => {
    axiosInstance
      .post(
        `/auth/v1/${formType}`,
        {
          username: formValues.username,
          password: formValues.password,
        },
        { withCredentials: true }
      )
      .then((res) => {
        updateUserInfo(res.data.payload);
        authenticateUser(res.data.payload.accessToken);
        successToast("User logged in successfull!!");
        navigate("/dashboard", { replace: true });
      })
      .catch((err) => console.log(err));
    clearFormValues();
  };
  return (
    <div
      onClick={() => handleFormSubmit()}
      className="w-full mt-5 py-2 text-center bg-brand-primary-600 text-secondary-text rounded-lg hover:scale-105 transition duration-100 ease-in-out cursor-pointer"
    >
      Submit
    </div>
  );
};

export default SubmitButton;
