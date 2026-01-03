import React from "react";
import { LuContainer } from "react-icons/lu";
import useSelectedContainerStore from "../../../utils/store/SelectedContainerStore";

const ContainerEntry = ({ containerId, containerName, isSelected }) => {
  const selectedContainerId = useSelectedContainerStore(
    (state) => state.selectedContainerId
  );
  const setSelectedContainerId = useSelectedContainerStore(
    (state) => state.setSelectedContainerId
  );

  const handleContainerClick = () => {
    if (selectedContainerId === containerId) setSelectedContainerId("");
    else setSelectedContainerId(containerId);
  };

  return (
    <div
      onClick={handleContainerClick}
      className={`w-full flex items-center gap-4 text-lg px-5 my-1 py-2 rounded-md 
        ${
          isSelected
            ? "bg-primary-bg text-brand-primary-600"
            : "hover:bg-brand-primary-400 hover:text-primary-bg"
        }  cursor-pointer`}
    >
      <LuContainer />
      <div>{containerName}</div>
    </div>
  );
};

export default ContainerEntry;
