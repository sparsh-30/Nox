import { useState } from "react";
import ContainersHeader from "./ContainersHeader";
import ContainerSearch from "./ContainerSearch";
import Containers from "./Containers";
import SidebarFooter from "./SidebarFooter";

const Sidebar = () => {
  const [containerSearch, setContainerSearch] = useState("");

  return (
    <div className="h-full w-full bg-brand-primary-600 text-secondary-text flex flex-col overflow-hidden">
      <div className="px-5 pt-4 shrink-0">
        <ContainersHeader />
        <ContainerSearch
          containerSearch={containerSearch}
          updateContainerSearch={setContainerSearch}
        />
      </div>
      <div className="flex-1 overflow-y-auto overflow-x-hidden px-5">
        <Containers containerSearch={containerSearch} />
      </div>
      <div className="shrink-0">
        <SidebarFooter />
      </div>
    </div>
  );
};

export default Sidebar;
