import { useState } from "react";
import ObjectsManagementHeader from "./ObjectsManagementHeader";

const ObjectsManagementWindow = () => {
  const [objectSearch, setObjectSearch] = useState("");
  const updateObjectSearch = (val) => {
    setObjectSearch(val);
  };
  return (
    <div className="w-9/10 h-screen p-8 my-5 mx-auto mb-5 bg-primary-bg rounded-2xl">
      <ObjectsManagementHeader
        objectSearch={objectSearch}
        updateObjectSearch={updateObjectSearch}
      />
    </div>
  );
};

export default ObjectsManagementWindow;
