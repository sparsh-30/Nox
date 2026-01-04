import React from "react";
import useContainerStore from "../../../utils/store/ContainerStore";
import useSelectedContainerStore from "../../../utils/store/SelectedContainerStore";
import useModalToggleStore from "../../../utils/store/ModalToggleStore";
import { formatTimestamp } from "../../../utils/CommonMethods";
import { FaRegTrashCan } from "react-icons/fa6";
import { AiOutlineClear } from "react-icons/ai";

const ContainerManagementWindow = () => {
  const containers = useContainerStore((state) => state.containers);
  const selectedContainerId = useSelectedContainerStore(
    (state) => state.selectedContainerId
  );
  const containerToDisplay = containers.filter(
    (container) => container.containerId === selectedContainerId
  );
  const updateConfirmationModal = useModalToggleStore(
    (state) => state.updateConfirmationModal
  );
  return (
    <div className="w-9/10 h-fit bp:h-50 p-8 my-5 mx-auto bg-primary-bg rounded-2xl flex flex-col bp:flex-row items-center gap-5">
      <div className="w-full bp:w-1/2 bp:h-full bg-brand-primary-200 p-3 rounded-2xl">
        <div className="text-xl mb-4 font-bold">Container Info</div>
        <div className="flex mb-2">
          <div className="w-1/2">Name</div>
          <div className="w-1/2">{containerToDisplay[0]?.containerName}</div>
        </div>
        <div className="flex">
          <div className="w-1/2">Created At</div>
          <div className="w-1/2">
            {formatTimestamp(containerToDisplay[0]?.containerCreatedAt)}
          </div>
        </div>
      </div>
      <div className="w-full bp:w-1/2 h-full flex flex-1 flex-col gap-5">
        <div
          onClick={() => updateConfirmationModal("emptyContainer")}
          className="w-full h-10 bp:h-1/2 bg-brand-primary-600 text-secondary-text rounded-2xl flex items-center justify-center gap-1 text-[16px] cursor-pointer hover:bg-brand-primary-400 transition duration-100 ease-in-out hover:scale-105"
        >
          <div>Empty Container</div>
          <div className="text-lg">
            <FaRegTrashCan />
          </div>
        </div>
        <div
          onClick={() => updateConfirmationModal("deleteContainer")}
          className="w-full h-10 bp:h-1/2 bg-danger text-secondary-text rounded-2xl flex items-center justify-center gap-1 text-[16px] cursor-pointer hover:bg-red-400 transition duration-100 ease-in-out hover:scale-105"
        >
          <div>Delete Container</div>
          <div className="text-xl">
            <AiOutlineClear />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContainerManagementWindow;
