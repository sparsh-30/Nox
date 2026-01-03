import React from "react";
import ContainerManagementWindow from "./ContainerManagementWindow";
import useSelectedContainerStore from "../../../utils/store/SelectedContainerStore";

const MainWindow = () => {
  const selectedContainerId = useSelectedContainerStore(
    (state) => state.selectedContainerId
  );
  return (
    <>
      {selectedContainerId === "" ? (
        <div className="w-full h-full bg-secondary-bg flex items-center justify-center">
          <div className="w-fit h-fit">
            Please select a container or create a new one.
          </div>
        </div>
      ) : (
        <div className="w-full h-screen flex-1 bg-secondary-bg overflow-y-scroll">
          <ContainerManagementWindow />
        </div>
      )}
    </>
  );
};

export default MainWindow;
