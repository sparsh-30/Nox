import React from "react";
import ContainerManagementWindow from "./ContainerManagementWindow";
import ObjectsManagementWindow from "./ObjectsManagementWindow";
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
        <div className="w-full h-full bg-secondary-bg overflow-y-scroll scrollbar-thin scrollbar-thumb-brand-primary-600 scrollbar-track-secondary-bg scrollbar-thumb-rounded-full scrollbar-no-buttons">
          <ContainerManagementWindow />
          <ObjectsManagementWindow />
        </div>
      )}
    </>
  );
};

export default MainWindow;
