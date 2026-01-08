import { useState, useRef } from "react";
import { FaUser } from "react-icons/fa";
import { IoEye } from "react-icons/io5";
import { IoEyeOff } from "react-icons/io5";

const CredentialInput = ({ credentialType }) => {
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const usernameRef = useRef(null);
  const passwordRef = useRef(null);
  const credentialTypeAttributes = {
    username: {
      ref: usernameRef,
      inputType: "text",
      placeholder: "Enter username",
      inputIcon: (
        <div>
          <FaUser />
        </div>
      ),
    },
    password: {
      ref: passwordRef,
      inputType: isPasswordVisible ? "text" : "password",
      placeholder: "Enter password",
      inputIcon: (
        <div
          onClick={() => setIsPasswordVisible(!isPasswordVisible)}
          className="text-[16px] cursor-pointer"
        >
          {isPasswordVisible ? <IoEye /> : <IoEyeOff />}
        </div>
      ),
    },
  };
  return (
    <div className="w-full h-fit mt-3 flex items-center border border-primary-border p-1 rounded-lg">
      <div className="w-full h-full">
        <input
          ref={credentialTypeAttributes[credentialType]["ref"]}
          className="w-full outline-none p-1"
          type={credentialTypeAttributes[credentialType]["inputType"]}
          placeholder={credentialTypeAttributes[credentialType]["placeholder"]}
        />
      </div>
      <div className="px-2">
        {credentialTypeAttributes[credentialType]["inputIcon"]}
      </div>
    </div>
  );
};

export default CredentialInput;
