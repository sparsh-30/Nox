import { useState } from "react";
import ObjectsManagementHeader from "./ObjectsManagementHeader";
import ObjectsTable from "./ObjectsTable";

const ObjectsManagementWindow = () => {
  const [objectSearch, setObjectSearch] = useState("");
  const updateObjectSearch = (val) => {
    setObjectSearch(val);
  };
  return (
    <div className="w-9/10 h-fit p-8 my-5 mx-auto mb-5 bg-primary-bg rounded-2xl">
      <ObjectsManagementHeader
        objectSearch={objectSearch}
        updateObjectSearch={updateObjectSearch}
      />
      <ObjectsTable />
    </div>
  );
};

export default ObjectsManagementWindow;
