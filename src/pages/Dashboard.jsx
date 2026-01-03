import { useState } from "react";
import Navbar from "../components/dashboard/navbar/Navbar";
import Sidebar from "../components/dashboard/sidebar/Sidebar";
import MainWindow from "../components/dashboard/main-screen/MainWindow";

const Dashboard = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };
  return (
    <div className="w-screen h-screen flex flex-col">
      <Navbar toggleMenu={toggleMenu} isMenuOpen={isMenuOpen} />
      <div className="flex items-center h-full">
        <div
          className={`${
            isMenuOpen ? "" : "hidden"
          } w-full bp:flex bp:w-1/5 h-full`}
        >
          <Sidebar />
        </div>
        <div
          className={`${
            isMenuOpen ? "hidden" : ""
          } w-full bp:flex bp:w-4/5 h-full`}
        >
          <MainWindow />
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
