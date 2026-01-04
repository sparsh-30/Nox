import { useRef } from "react";
import { FaSearch } from "react-icons/fa";
import { FaXmark } from "react-icons/fa6";
import { FaRegFolderClosed } from "react-icons/fa6";
import { FaUpload } from "react-icons/fa6";
import useParentPathStore from "../../../utils/store/ParentPathStore";
import { trimLastSegment } from "../../../utils/CommonMethods";

const ObjectsManagementHeader = ({ objectSearch, updateObjectSearch }) => {
  const currentParentPath = useParentPathStore(
    (state) => state.currentParentPath
  );
  const setCurrentParentPath = useParentPathStore(
    (state) => state.setCurrentParentPath
  );

  const handleBack = () => {
    if (currentParentPath === "/") return;
    const trimmed = trimLastSegment(currentParentPath);
    setCurrentParentPath(trimmed);
  };
  const searchInputRef = useRef(null);
  return (
    <div className="w-full">
      <div className="flex items-center justify-between">
        <div className="text-xl">{currentParentPath}</div>
        <div
          onClick={handleBack}
          className="bg-brand-primary-600 text-secondary-text p-2 rounded-md flex items-center justify-center gap-1 cursor-pointer hover:bg-brand-primary-400 hover:scale-105 transition-all duration-100 ease-in-out"
        >
          Back
        </div>
      </div>
      <div className="w-full my-3 flex flex-col bp:flex-row items-center justify-between gap-3">
        <div className="w-full bp:w-1/2 h-fit rounded-md relative bg-secondary-bg flex items-center">
          <input
            className="w-full text-primary-text bg-secondary-bg pl-2 py-2 outline-none rounded-md"
            type="text"
            placeholder="Search ..."
            ref={searchInputRef}
            onChange={() => updateObjectSearch(searchInputRef.current.value)}
          />
          <div className="text-primary-text px-2">
            {objectSearch === "" ? (
              <FaSearch />
            ) : (
              <div
                className="cursor-pointer"
                onClick={() => {
                  searchInputRef.current.value = "";
                  updateObjectSearch("");
                }}
              >
                <FaXmark />
              </div>
            )}
          </div>
        </div>
        <div className="w-full bp:w-fit flex items-center gap-3">
          <div className="w-1/2 bp:w-fit bg-brand-primary-600 text-secondary-text p-2 rounded-md flex items-center justify-center gap-1 cursor-pointer hover:bg-brand-primary-400 hover:scale-105 transition-all duration-100 ease-in-out">
            <div>Create Folder</div>
            <div>
              <FaRegFolderClosed />
            </div>
          </div>
          <div className="w-1/2 bp:w-fit bg-brand-primary-600 text-secondary-text p-2 rounded-md flex items-center justify-center gap-1 cursor-pointer hover:bg-brand-primary-400 hover:scale-105 transition-all duration-100 ease-in-out">
            <div>Upload File</div>
            <div>
              <FaUpload />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ObjectsManagementHeader;
