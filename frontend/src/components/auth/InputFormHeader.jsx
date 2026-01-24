import React from "react";

const InputFormHeader = ({ formType }) => {
  return (
    <div>
      <div className="text-4xl font-bold">NOX</div>
      <div className="text-primary-border">
        {formType === "register"
          ? "Create your account and get started in seconds."
          : "Welcome back — sign in to continue."}
      </div>
    </div>
  );
};

export default InputFormHeader;
