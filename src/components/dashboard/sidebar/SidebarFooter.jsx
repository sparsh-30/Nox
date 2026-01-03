import React from "react";
import { FiLogOut } from "react-icons/fi";
import { getProfileColor } from "../../../utils/ProfileColors";

const SidebarFooter = () => {
  const username = "test";
  return (
    <div className="py-3 px-5 text-secondary-text flex items-center justify-between bp:justify-start bp:gap-3 hover:bg-brand-primary-400 rounded-t-md cursor-pointer">
      <div className="flex items-center gap-2 bp:hidden">
        <div
          style={{ backgroundColor: getProfileColor(username) }}
          className={`w-10 h-10 rounded-xl text-2xl font-bold text-secondary-text flex items-center justify-center`}
        >
          {username.charAt(0).toUpperCase()}
        </div>
        <div className="font-semibold w-fit h-fit">
          <div className="">{username}</div>
          <div className="text-[10px] bp:text-gray-500">Admin</div>
        </div>
      </div>
      <div className="flex gap-2 items-center cursor-pointer">
        <div className="text-xl">
          <FiLogOut />
        </div>
        <div className="text-lg">Logout</div>
      </div>
    </div>
  );
};

export default SidebarFooter;
