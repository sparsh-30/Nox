import React from "react";
import { AiOutlineMenu } from "react-icons/ai";
import { MdClose } from "react-icons/md";
import { getProfileColor } from "../../../utils/ProfileColors";

const Navbar = ({ isMenuOpen, toggleMenu }) => {
  const username = "test";
  return (
    <div className="w-screen h-fit px-5 bp:px-10 py-2 flex items-center justify-between">
      <div className="text-4xl">NOX</div>
      <div className="bp:flex items-center gap-2 hidden">
        <div
          style={{ backgroundColor: getProfileColor(username) }}
          className="w-10 h-10 rounded-xl text-2xl font-bold text-secondary-text flex items-center justify-center"
        >
          {username.charAt(0).toUpperCase()}
        </div>
        <div className="font-semibold w-fit h-fit">
          <div className="">{username}</div>
          <div className="text-[10px] text-gray-500">Admin</div>
        </div>
      </div>
      <div
        onClick={() => toggleMenu()}
        className="bp:hidden flex text-lg p-1 bg-brand-primary-600 text-white rounded-md"
      >
        {isMenuOpen ? <MdClose /> : <AiOutlineMenu />}
      </div>
    </div>
  );
};

export default Navbar;
