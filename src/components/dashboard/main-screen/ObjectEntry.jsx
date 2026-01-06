import { useState } from "react";
import { BiSolidDownArrow } from "react-icons/bi";
import { FaRegFolder } from "react-icons/fa";
import { FaRegFile } from "react-icons/fa";
import { FaRegTrashCan } from "react-icons/fa6";
import { IoMdCloudDownload } from "react-icons/io";
import { AiOutlineClear } from "react-icons/ai";
import { formatBytes, formatTimestamp } from "../../../utils/CommonMethods";
import useParentPathStore from "../../../utils/store/ParentPathStore";
import useModalToggleStore from "../../../utils/store/ModalToggleStore";

const ObjectEntry = ({ object, isEven }) => {
  const [expandTile, setExpandTile] = useState(false);
  const currentParentPath = useParentPathStore(
    (state) => state.currentParentPath
  );
  const setCurrentParentPath = useParentPathStore(
    (state) => state.setCurrentParentPath
  );
  const updateConfirmationModal = useModalToggleStore(
    (state) => state.updateConfirmationModal
  );
  const updateMiscellaneousData = useModalToggleStore(
    (state) => state.updateMiscellaneousData
  );

  const handleFolderOpen = () => {
    if (object.objectType === "FILE") return;
    setCurrentParentPath(currentParentPath + object.objectName + "/");
  };

  const handleEmptyFolder = () => {
    updateMiscellaneousData(object.objectName);
    updateConfirmationModal("emptyFolder");
  };

  const handleDeleteObject = () => {
    updateMiscellaneousData(object.objectId);
    updateConfirmationModal("deleteObject");
  };

  const handleTileExpand = (e) => {
    e.stopPropagation();
    setExpandTile(!expandTile);
  };

  return (
    <div className={`${isEven ? "bg-secondary-bg" : "bg-primary-bg"}`}>
      <div className="w-full p-2 flex items-center">
        <div className="w-10 h-full">
          <div
            onClick={(e) => handleTileExpand(e)}
            className={`p-1 h-6 w-7 bg-brand-primary-600 text-secondary-text rounded-lg flex items-baseline-center justify-center z-50`}
          >
            <div
              className={`${
                expandTile ? "rotate-180" : ""
              } transition duration-300 ease-in-out cursor-pointer`}
            >
              <BiSolidDownArrow />
            </div>
          </div>
        </div>
        <div className="w-full flex items-center">
          <div className="w-1/4 flex items-center">{object.objectName}</div>
          <div className="w-1/4 flex items-center">
            {object.objectType === "FOLDER" ? (
              <div
                onClick={handleFolderOpen}
                className="bg-blue-100 px-3 py-0.5 text-blue-800 font-bold text-xs w-fit rounded-lg flex justify-center items-center gap-2 cursor-pointer"
              >
                <div>
                  <FaRegFolder />
                </div>
                <div>FOLDER</div>
              </div>
            ) : (
              <div
                onClick={handleFolderOpen}
                className="bg-yellow-100 px-3 py-0.5 text-yellow-800 font-bold text-xs w-fit rounded-lg flex justify-center items-center gap-2 cursor-pointer"
              >
                <div>
                  <FaRegFile />
                </div>
                <div>FILE</div>
              </div>
            )}
          </div>
          <div className="w-1/4 flex items-center">
            {object.objectType === "FILE" ? formatBytes(object.size) : "-"}
          </div>
          <div className="w-1/4 flex items-center">
            {formatTimestamp(object.objectCreatedAt)}
          </div>
        </div>
      </div>
      <div
        className={`overflow-hidden transition-all duration-300 ease-in-out
        ${expandTile ? "max-h-40 opacity-100" : "max-h-0 opacity-0"}
      `}
      >
        <div className="w-full px-2 py-2 flex justify-center items-center gap-3">
          <div
            onClick={handleEmptyFolder}
            className={`w-1/2 py-1 bg-brand-primary-600 text-secondary-text text-center rounded-md ${
              object.objectType === "FOLDER" ? "flex" : "hidden"
            } items-center justify-center gap-1 cursor-pointer hover:bg-brand-primary-400`}
          >
            <div>Empty Folder</div>
            <div className="text-lg">
              <AiOutlineClear />
            </div>
          </div>
          <div
            onClick={handleDeleteObject}
            className={`w-1/2 py-1 bg-danger text-secondary-text text-center rounded-md ${
              object.objectType === "FOLDER" ? "flex" : "hidden"
            } items-center justify-center gap-1 cursor-pointer hover:bg-red-400`}
          >
            <div>Delete Folder</div>
            <div>
              <FaRegTrashCan />
            </div>
          </div>
          <div
            // onClick={handleEmptyFolder}
            className={`w-1/2 py-1 bg-brand-primary-600 text-secondary-text text-center rounded-md ${
              object.objectType === "FILE" ? "flex" : "hidden"
            } items-center justify-center gap-1 cursor-pointer hover:bg-brand-primary-400`}
          >
            <div>Download File</div>
            <div className="text-lg">
              <IoMdCloudDownload />
            </div>
          </div>
          <div
            onClick={handleDeleteObject}
            className={`w-1/2 py-1 bg-danger text-secondary-text text-center rounded-md ${
              object.objectType === "FILE" ? "flex" : "hidden"
            } items-center justify-center gap-1 cursor-pointer hover:bg-red-400`}
          >
            <div>Delete File</div>
            <div>
              <FaRegTrashCan />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ObjectEntry;
