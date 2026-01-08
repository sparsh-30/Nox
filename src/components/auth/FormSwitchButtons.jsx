import { useState } from "react";

const FormSwitchButtons = () => {
  const [selectedButton, setSelectedButton] = useState("register");
  const handleButtonChange = () => {
    if (selectedButton === "register") setSelectedButton("login");
    if (selectedButton === "login") setSelectedButton("register");
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
