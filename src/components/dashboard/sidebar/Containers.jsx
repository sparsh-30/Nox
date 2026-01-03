import { useEffect } from "react";
import ContainerEntry from "./ContainerEntry";
import useContainerStore from "../../../utils/store/ContainerStore";
import useSelectedContainerStore from "../../../utils/store/SelectedContainerStore";
import { axiosInstance } from "../../../utils/AxiosConfig";

const Containers = ({ containerSearch }) => {
  const containers = useContainerStore((state) => state.containers);
  const updateContainersList = useContainerStore(
    (state) => state.updateContainersList
  );
  const selectedContainerId = useSelectedContainerStore((state) => state.selectedContainerId);

  const dummyData = [
    {containerId:1, containerName: "cont1"},
    {containerId:2, containerName: "cont2"},
    {containerId:3, containerName: "cont3"},
    {containerId:4, containerName: "cont4"},
    {containerId:5, containerName: "cont5"},
    {containerId:6, containerName: "cont6"},
    {containerId:7, containerName: "cont7"},
    {containerId:8, containerName: "cont8"},
    {containerId:9, containerName: "cont9"},
    {containerId:10, containerName: "cont10"},
    {containerId:11, containerName: "cont11"},
    {containerId2:1, containerName: "cont12"}
  ]

  const filteredItems = containers.filter((container) =>
    containerSearch.trim() === ""
      ? true
      : container.containerName
          .toLowerCase()
          .includes(containerSearch.toLowerCase())
  );

  useEffect(() => {
    updateContainersList(dummyData);
    // axiosInstance
    //   .get("/api/v1/container/containers")
    //   .then((res) => updateContainersList(res.data.payload))
    //   .catch((err) => console.log(err));
  }, []);

  return (
    <div>
      {filteredItems.map((container) => {
        return (
          <ContainerEntry
            containerName={container.containerName}
            key={container.containerId}
            containerId={container.containerId}
            isSelected={container.containerId === selectedContainerId ? true : false}
          />
        );
      })}
    </div>
  );
};

export default Containers;
