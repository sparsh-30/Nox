import React from "react";
import { FiLogOut } from "react-icons/fi";

const SidebarFooter = () => {
  return (
    <div className="py-3 px-5 text-secondary-text flex items-center gap-3 hover:bg-brand-primary-400 rounded-t-md cursor-pointer">
      <div className="text-xl">
        <FiLogOut />
      </div>
      <div className="text-lg">Logout</div>
    </div>
  );
};

export default SidebarFooter;
