import { useRef } from "react";
import { FaSearch } from "react-icons/fa";
import { FaXmark } from "react-icons/fa6";

const ContainerSearch = ({ containerSearch, updateContainerSearch }) => {
  const searchInputRef = useRef(null);
  return (
    <div className="w-full h-fit mb-2">
      <div className="w-full h-fit rounded-md relative bg-primary-bg flex items-center">
        <input
          className="w-full text-brand-primary-400 bg-primary-bg pl-2 py-2 outline-none rounded-md"
          type="text"
          placeholder="Search ..."
          onChange={() => updateContainerSearch(searchInputRef.current.value)}
          ref={searchInputRef}
        />
        <div className="text-brand-primary-600 px-2">
          {containerSearch === "" ? (
            <FaSearch />
          ) : (
            <div
              className="cursor-pointer"
              onClick={() => {
                searchInputRef.current.value = "";
                updateContainerSearch("");
              }}
            >
              <FaXmark />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ContainerSearch;
