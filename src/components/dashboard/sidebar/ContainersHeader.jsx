import React from "react";
import { FaRegPlusSquare } from "react-icons/fa";

const ContainersHeader = () => {
  return (
    <div className="mb-2">
      <div className="w-full h-10 relative">
        <div className="w-full h-full flex items-center justify-center absolute top-0 left-0">
          <div className="w-full h-px bg-secondary-text"></div>
        </div>
        <div className="w-full h-full flex items-center justify-center absolute top-0 left-0">
          <div className="bg-brand-primary-600 px-2">Containers</div>
        </div>
      </div>
      <div className="w-full">
        <div className="w-full bg-primary-bg text-brand-primary-400 flex items-center justify-center gap-2 py-2 rounded-md cursor-pointer hover:scale-105 transition-all ease-in-out duration-100">
          <FaRegPlusSquare />
          <div>Create</div>
        </div>
      </div>
    </div>
  );
};

export default ContainersHeader;
