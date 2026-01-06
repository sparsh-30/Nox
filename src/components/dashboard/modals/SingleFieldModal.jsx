import { useState } from "react";
import { MdCancelPresentation } from "react-icons/md";
import { FaRegCheckSquare } from "react-icons/fa";
import { axiosInstance } from "../../../../../frontend/src/utils/AxiosConfig";
import useObjectStore from "../../../utils/store/ObjectStore";
import useContainerStore from "../../../utils/store/ContainerStore";
import useParentPathStore from "../../../utils/store/ParentPathStore";
import useSelectedContainerStore from "../../../utils/store/SelectedContainerStore";
import useModalToggleStore from "../../../utils/store/ModalToggleStore";

const SingleFieldModal = () => {
  const [inputData, setInputData] = useState("");
  const fieldType = useModalToggleStore((state) => state.singleFieldModal);

  const addNewContainer = useContainerStore((state) => state.addNewContainer);
  const addNewObject = useObjectStore((state) => state.addNewObject);

  const selectedContainerId = useSelectedContainerStore(
    (state) => state.selectedContainerId
  );
  const setSelectedContainerId = useSelectedContainerStore(
    (state) => state.setSelectedContainerId
  );
  const currentParentPath = useParentPathStore(
    (state) => state.currentParentPath
  );

  const closeSingleFieldModal = useModalToggleStore(
    (state) => state.closeSingleFieldModal
  );

  const fieldTypes = {
    createContainer: {
      userMessage: "Please type the name of container below: ",
      placeholderMessage: "container name ...",
      submitButtonLabel: "Create",
      onSubmitAction: () => {
        if (inputData === "") return;
        axiosInstance
          .get("/api/v1/container/create", {
            params: {
              containerName: inputData,
            },
          })
          .then((res) => {
            addNewContainer(res.data.payload);
            setSelectedContainerId(res.data.payload.containerId);
            closeSingleFieldModal();
          })
          .catch((err) => console.log(err));
      },
    },
    createFolder: {
      userMessage: "Please type the name of folder below: ",
      placeholderMessage: "folder name ...",
      submitButtonLabel: "Create",
      onSubmitAction: () => {
        if (inputData === "") return;
        axiosInstance
          .post(
            "/api/v1/object/create/folder",
            {},
            {
              params: {
                containerId: selectedContainerId,
                parentPath: currentParentPath,
                size: 0,
                objectName: inputData,
              },
            }
          )
          .then((res) => {
            addNewObject(res.data.payload);
            closeSingleFieldModal();
          })
          .catch((err) => console.log(err));
      },
    },
  };

  const handleInput = (e) => {
    if (e.key === "Enter") {
      (() => fieldTypes[fieldType]?.onSubmitAction?.())();
    }
    setInputData(e.target.value);
  };
  return (
    <div className="h-screen w-screen z-50 absolute top-0 left-0 bg-black/30 backdrop-blur-sm flex items-center justify-center">
      <div className="bg-secondary-bg h-fit bp:h-fit w-9/10 bp:w-4/10 px-10 py-10 gap-10 rounded-xl">
        <div>{fieldTypes[fieldType]?.userMessage}</div>
        <input
          onKeyUp={(e) => handleInput(e)}
          className="w-full my-5 px-2 py-1 font-bold outline-none border-2 border-primary-border rounded-md"
          type="text"
          placeholder={fieldTypes[fieldType]?.placeholderMessage}
        />
        <div className="flex justify-center items-center gap-4">
          <div
            onClick={() => closeSingleFieldModal()}
            className="w-1/2 py-2 px-3 font-semibold bg-danger text-secondary-text rounded-md flex items-center justify-center gap-1 cursor-pointer transition-all duration-100 ease-in-out hover:scale-105"
          >
            <div>Close</div>
            <div className="text-lg">
              <MdCancelPresentation />
            </div>
          </div>
          <div
            onClick={fieldTypes[fieldType]?.onSubmitAction}
            className={`w-1/2 py-2 px-3 font-semibold ${
              inputData.length === 0
                ? "bg-brand-primary-600/50 cursor-not-allowed"
                : "bg-brand-primary-600 cursor-pointer hover:scale-105"
            } text-secondary-text rounded-md flex items-center justify-center gap-1 transition-all duration-100 ease-in-out`}
          >
            <div>{fieldTypes[fieldType]?.submitButtonLabel}</div>
            <div className="text-lg">
              <FaRegCheckSquare />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SingleFieldModal;
