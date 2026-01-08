import React from "react";

const InputFormHeader = ({ formType }) => {
  return (
    <div>
      <div className="text-4xl font-bold">NOX</div>
      <div className="text-primary-border">
        {formType === "register"
          ? "Lorem ipsum dolor sit, amet consectetur adipisicing elit. Modi, inventore."
          : "Login"}
      </div>
    </div>
  );
};

export default InputFormHeader;
