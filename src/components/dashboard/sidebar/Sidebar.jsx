import { useState } from "react";
import ContainersHeader from "./ContainersHeader";
import ContainerSearch from "./ContainerSearch";
import Containers from "./Containers";

const Sidebar = () => {
  const [containerSearch, SetContainerSearch] = useState("");
  const updateContainerSearch = (val) => {
    SetContainerSearch(val);
  };
  return (
    <div className="w-full h-full px-5 flex-1 bg-brand-primary-600 text-secondary-text">
      <ContainersHeader />
      <ContainerSearch
        containerSearch={containerSearch}
        updateContainerSearch={updateContainerSearch}
      />
      <Containers containerSearch={containerSearch} />
    </div>
  );
};

export default Sidebar;
