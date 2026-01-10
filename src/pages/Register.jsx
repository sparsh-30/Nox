import { useState } from "react";
import InputFormHeader from "../components/auth/InputFormHeader";
import FormSwitchButtons from "../components/auth/FormSwitchButtons";
import CredentialInput from "../components/auth/CredentialInput";
import SubmitButton from "../components/auth/SubmitButton";

const Register = () => {
  const formType = "register";
  const [registerFormValues, setRegisterFormValues] = useState({
    username: "",
    password: "",
  });
  const handleFormValueChange = (fieldType) => (e) => {
    setRegisterFormValues((prev) => ({
      ...prev,
      [fieldType]: e.target.value,
    }));
  };
  const handleFormSubmit = () => {
    console.log(registerFormValues);
    setRegisterFormValues({
      username: "",
      password: "",
    });
  };

  return (
    <div className="w-screen h-screen flex justify-center items-center bg-brand-primary-200">
      <div className="w-9/10 bp:w-3/5 bp:h-4/5 bg-secondary-bg shadow-2xl rounded-4xl flex overflow-hidden">
        <div className='w-2/5 hidden bp:flex bg-[url("/img.jpg")]'></div>
        <div className="w-full bp:w-3/5 h-fit my-auto px-8 py-24 bp:py-16">
          <InputFormHeader formType={formType} />
          <FormSwitchButtons />
          <CredentialInput
            credentialType="username"
            formValues={registerFormValues}
            handleFormValueChange={handleFormValueChange("username")}
          />
          <CredentialInput
            credentialType="password"
            formValues={registerFormValues}
            handleFormValueChange={handleFormValueChange("password")}
          />
          <SubmitButton handleFormSubmit={handleFormSubmit} />
        </div>
      </div>
    </div>
  );
};

export default Register;
