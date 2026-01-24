import { useEffect } from "react";
import ContainerEntry from "./ContainerEntry";
import useContainerStore from "../../../utils/store/ContainerStore";
import useUserInfoStore from "../../../utils/store/UserInfoStore";
import useSelectedContainerStore from "../../../utils/store/SelectedContainerStore";
import { axiosInstance } from "../../../utils/AxiosConfig";

const Containers = ({ containerSearch }) => {
  const containers = useContainerStore((state) => state.containers);
  const updateContainersList = useContainerStore(
    (state) => state.updateContainersList
  );
  const selectedContainerId = useSelectedContainerStore(
    (state) => state.selectedContainerId
  );
  const userId = useUserInfoStore((state) => state.userId);

  const filteredItems = containers.filter((container) =>
    containerSearch.trim() === ""
      ? true
      : container.containerName
          .toLowerCase()
          .includes(containerSearch.toLowerCase())
  );

  useEffect(() => {
    axiosInstance
      .get("/api/v1/container/get", {
        params: {
          userId: userId,
        },
      })
      .then((res) => {
        updateContainersList(res.data.payload);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <div>
      {filteredItems.map((container) => {
        return (
          <ContainerEntry
            containerName={container.containerName}
            key={container.containerId}
            containerId={container.containerId}
            isSelected={
              container.containerId === selectedContainerId ? true : false
            }
          />
        );
      })}
    </div>
  );
};

export default Containers;
