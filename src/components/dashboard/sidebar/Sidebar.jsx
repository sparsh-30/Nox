import { useState } from "react";
import ContainersHeader from "./ContainersHeader";
import ContainerSearch from "./ContainerSearch";
import Containers from "./Containers";
import SidebarFooter from "./SidebarFooter";

const Sidebar = () => {
  const [containerSearch, SetContainerSearch] = useState("");
  const updateContainerSearch = (val) => {
    SetContainerSearch(val);
  };
  return (
    <div className="w-full h-full flex-1 bg-brand-primary-600 text-secondary-text flex flex-col justify-between">
      <div className="px-5">
        <ContainersHeader />
        <ContainerSearch
          containerSearch={containerSearch}
          updateContainerSearch={updateContainerSearch}
        />
        <Containers containerSearch={containerSearch} />
      </div>
      <div>
        <SidebarFooter />
      </div>
    </div>
  );
};

export default Sidebar;
