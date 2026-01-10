import React from "react";

const SubmitButton = ({ handleFormSubmit }) => {
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
