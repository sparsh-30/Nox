import { useState } from "react";
import { BiSolidDownArrow } from "react-icons/bi";
import { FaRegFolder } from "react-icons/fa";
import { FaRegFile } from "react-icons/fa";
import { FaRegTrashCan } from "react-icons/fa6";
import { AiOutlineClear } from "react-icons/ai";

const ObjectEntry = ({ object, isEven }) => {
  const [expandTile, setExpandTile] = useState(false);
  function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return "0 B";

    const k = 1024;
    const sizes = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];

    const i = Math.floor(Math.log(bytes) / Math.log(k));
    const value = bytes / Math.pow(k, i);

    return `${parseFloat(value.toFixed(decimals))} ${sizes[i]}`;
  }

  const handleFolderOpen = () => {
    console.log("open folder");
  };

  const handleTileExpand = (e) => {
    e.stopPropagation();
    setExpandTile(!expandTile);
  };

  return (
    <div className={`${isEven ? "bg-secondary-bg" : "bg-primary-bg"}`}>
      <div
        onClick={() => handleFolderOpen()}
        className="w-full p-2 flex items-center cursor-pointer"
      >
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
              <div className="bg-blue-100 px-3 py-0.5 text-blue-800 font-bold text-xs w-fit rounded-lg flex justify-center items-center gap-2">
                <div>
                  <FaRegFolder />
                </div>
                <div>FOLDER</div>
              </div>
            ) : (
              <div className="bg-yellow-100 px-3 py-0.5 text-yellow-800 font-bold text-xs w-fit rounded-lg flex justify-center items-center gap-2">
                <div>
                  <FaRegFile />
                </div>
                <div>FILE</div>
              </div>
            )}
          </div>
          <div className="w-1/4 flex items-center">
            {formatBytes(object.size)}
          </div>
          <div className="w-1/4 flex items-center">{object.createdAt}</div>
        </div>
      </div>
      <div
        className={`overflow-hidden transition-all duration-300 ease-in-out
        ${expandTile ? "max-h-40 opacity-100" : "max-h-0 opacity-0"}
      `}
      >
        <div className="w-full px-2 py-2 flex justify-center items-center gap-3">
          <div
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
