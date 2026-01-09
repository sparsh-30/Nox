import React from "react";

const SubmitButton = ({ userInputValues }) => {
  return (
    <div
      onClick={() => console.log(userInputValues)}
      className="w-full mt-5 py-2 text-center bg-brand-primary-600 text-secondary-text rounded-lg hover:scale-105 transition duration-100 ease-in-out cursor-pointer"
    >
      Submit
    </div>
  );
};

export default SubmitButton;
