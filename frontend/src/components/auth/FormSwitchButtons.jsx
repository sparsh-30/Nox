import { useState } from "react";
import { useLocation, useNavigate } from "react-router";

const FormSwitchButtons = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [selectedButton, setSelectedButton] = useState(
    location.pathname === "/auth/register" ? "register" : "login"
  );
  const handleButtonChange = () => {
    if (selectedButton === "register") {
      navigate("/auth/login");
    }
    if (selectedButton === "login") {
      navigate("/auth/register");
    }
  };
  return (
    <div className="my-8 w-full h-fit border border-primary-border rounded-lg flex items-center justify-center p-0.5 text-center">
      <div
        onClick={handleButtonChange}
        className={`w-1/2 py-2 ${
          selectedButton === "register"
            ? "bg-brand-primary-600 text-secondary-text"
            : ""
        } rounded-lg cursor-pointer`}
      >
        Register
      </div>
      <div
        onClick={handleButtonChange}
        className={`w-1/2 py-2 ${
          selectedButton === "login"
            ? "bg-brand-primary-600 text-secondary-text"
            : ""
        } rounded-lg cursor-pointer`}
      >
        Login
      </div>
    </div>
  );
};

export default FormSwitchButtons;
