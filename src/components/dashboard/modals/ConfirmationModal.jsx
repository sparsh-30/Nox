import React from "react";
import { MdCancelPresentation } from "react-icons/md";
import { FaRegCheckSquare } from "react-icons/fa";
import useModalToggleStore from "../../../utils/store/ModalToggleStore";
import useSelectedContainerStore from "../../../utils/store/SelectedContainerStore";
import useContainerStore from "../../../utils/store/ContainerStore";
import useObjectStore from "../../../utils/store/ObjectStore";
import useUserInfoStore from "../../../utils/store/UserInfoStore";
import useParentPathStore from "../../../utils/store/ParentPathStore";
import useAuthStore from "../../../utils/store/AuthStore";
import { axiosInstance } from "../../../utils/AxiosConfig";
import { successToast } from "../../../utils/Toasts";

const ConfirmationModal = () => {
  const closeConfirmationModal = useModalToggleStore(
    (state) => state.closeConfirmationModal
  );
  const fieldType = useModalToggleStore((state) => state.confirmationModal);
  const miscellaneousData = useModalToggleStore(
    (state) => state.miscellaneousData
  );
  const clearMiscellaneousData = useModalToggleStore(
    (state) => state.clearMiscellaneousData
  );

  const selectedContainerId = useSelectedContainerStore(
    (state) => state.selectedContainerId
  );
  const setSelectedContainerId = useSelectedContainerStore(
    (state) => state.setSelectedContainerId
  );
  const containers = useContainerStore((state) => state.containers);
  const removeContainer = useContainerStore((state) => state.removeContainer);
  const objects = useObjectStore((state) => state.objects);
  const updateObjectsList = useObjectStore((state) => state.updateObjectsList);
  const removeObject = useObjectStore((state) => state.removeObject);
  const currentParentPath = useParentPathStore(
    (state) => state.currentParentPath
  );
  const setCurrentParentPath = useParentPathStore(
    (state) => state.setCurrentParentPath
  );
  const userId = useUserInfoStore((state) => state.userId);
  const removeUserInfo = useUserInfoStore((state) => state.removeUserInfo);
  const logOutUser = useAuthStore((state) => state.logOutUser);

  const fieldTypes = {
    logOutUser: {
      confirmationLine: "Are you sure you want to log out?",
      onSubmitAction: () => {
        axiosInstance
          .get("/auth/v1/logout", {
            params: {
              userId: userId,
            },
            withCredentials: true,
          })
          .then(() => {
            setSelectedContainerId("");
            setCurrentParentPath("/");
            closeConfirmationModal();
            clearMiscellaneousData();
            removeUserInfo();
            logOutUser();
            successToast("User logged out successfully!!");
          })
          .catch((err) => console.log(err));
      },
    },
    emptyContainer: {
      confirmationLine: (
        <>
          <span>Are you sure you want to empty this container, </span>
          <span className="font-bold text-brand-primary-600">
            {
              containers.filter(
                (cont) => cont.containerId === selectedContainerId
              )[0]?.containerName
            }
          </span>
          <span>? All data stored inside will be permanently deleted.</span>
        </>
      ),
      onSubmitAction: () => {
        axiosInstance
          .delete("/api/v1/container/empty", {
            params: {
              containerId: selectedContainerId,
            },
          })
          .then((res) => {
            updateObjectsList([]);
            setCurrentParentPath("/");
            closeConfirmationModal();
            successToast("Container is emptied!!");
          })
          .catch((err) => console.log(err));
      },
    },
    deleteContainer: {
      confirmationLine: (
        <>
          <span>Are you sure you want to delete this container, </span>
          <span className="font-bold text-brand-primary-600">
            {
              containers.filter(
                (cont) => cont.containerId === selectedContainerId
              )[0]?.containerName
            }
          </span>
          <span>? All data stored inside will be permanently deleted.</span>
        </>
      ),
      onSubmitAction: () => {
        axiosInstance
          .delete("/api/v1/container/delete", {
            params: {
              containerId: selectedContainerId,
            },
          })
          .then((res) => {
            setSelectedContainerId("");
            removeContainer(selectedContainerId);
            setCurrentParentPath("/");
            closeConfirmationModal();
            successToast("Container deleted successfully!!");
          })
          .catch((err) => console.log(err));
      },
    },
    emptyFolder: {
      confirmationLine:
        "Are you sure you want to empty this folder? All data stored inside will be permanently deleted.",
      onSubmitAction: () => {
        axiosInstance
          .delete("/api/v1/folder/empty", {
            params: {
              containerId: selectedContainerId,
              parentPath: currentParentPath + miscellaneousData + "/",
            },
          })
          .then((res) => {
            clearMiscellaneousData();
            closeConfirmationModal();
            successToast("Folder is emptied!!");
          })
          .catch((err) => console.log(err));
      },
    },
    deleteObject: {
      confirmationLine:
        "Are you sure you want to delete this folder? All data stored inside will be permanently deleted.",
      onSubmitAction: () => {
        axiosInstance
          .delete("/api/v1/delete/object", {
            params: {
              containerId: selectedContainerId,
              objectId: miscellaneousData,
            },
          })
          .then((res) => {
            removeObject(miscellaneousData);
            clearMiscellaneousData();
            closeConfirmationModal();
            successToast("Object is deleted successfully!!");
          })
          .catch((err) => console.log(err));
      },
    },
  };
  return (
    <div className="h-screen w-screen z-50 absolute top-0 left-0 bg-black/30 backdrop-blur-sm flex items-center justify-center">
      <div className="bg-secondary-bg h-fit bp:h-fit w-9/10 bp:w-4/10 px-10 py-10 gap-10 rounded-xl">
        <div>{fieldTypes[fieldType]?.confirmationLine}</div>
        <div className=" mt-5 flex justify-center items-center gap-4">
          <div
            onClick={() => closeConfirmationModal()}
            className="w-1/2 py-2 px-3 font-semibold bg-danger text-secondary-text rounded-md flex items-center justify-center gap-1 cursor-pointer transition-all duration-100 ease-in-out hover:scale-105"
          >
            <div>Close</div>
            <div className="text-lg">
              <MdCancelPresentation />
            </div>
          </div>
          <div
            onClick={fieldTypes[fieldType]?.onSubmitAction}
            className="w-1/2 py-2 px-3 font-semibold bg-brand-primary-600 cursor-pointer hover:scale-105 text-secondary-text rounded-md flex items-center justify-center gap-1 transition-all duration-100 ease-in-out"
          >
            <div>Confirm</div>
            <div className="text-lg">
              <FaRegCheckSquare />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ConfirmationModal;
