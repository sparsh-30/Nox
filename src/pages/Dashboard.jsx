import { useState } from "react";
import Navbar from "../components/dashboard/navbar/Navbar";
import Sidebar from "../components/dashboard/sidebar/Sidebar";
import MainWindow from "../components/dashboard/main-screen/MainWindow";
import SingleFieldModal from "../components/dashboard/modals/SingleFieldModal";
import ConfirmationModal from "../components/dashboard/modals/ConfirmationModal";
import useModalToggleStore from "../utils/store/ModalToggleStore";

const Dashboard = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const singleFieldModal = useModalToggleStore(
    (state) => state.singleFieldModal
  );
  const confirmationModal = useModalToggleStore(
    (state) => state.confirmationModal
  );
  return (
    <>
      <div className="w-screen h-screen overflow-hidden flex flex-col">
        <Navbar
          toggleMenu={() => setIsMenuOpen(!isMenuOpen)}
          isMenuOpen={isMenuOpen}
        />
        <div className="flex flex-1 overflow-hidden">
          <div
            className={`${
              isMenuOpen ? "" : "hidden"
            } w-full bp:block bp:w-1/5 overflow-hidden`}
          >
            <Sidebar />
          </div>
          <div
            className={`${
              isMenuOpen ? "hidden" : ""
            } w-full bp:block bp:w-4/5 overflow-hidden`}
          >
            <MainWindow />
          </div>
        </div>
      </div>
      {singleFieldModal ? <SingleFieldModal /> : ""}
      {confirmationModal ? <ConfirmationModal /> : ""}
    </>
  );
};

export default Dashboard;
