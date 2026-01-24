import { useEffect } from "react";
import ObjectEntry from "./ObjectEntry";
import useSelectedContainerStore from "../../../utils/store/SelectedContainerStore";
import useParentPathStore from "../../../utils/store/ParentPathStore";
import useObjectStore from "../../../utils/store/ObjectStore";
import { axiosInstance } from "../../../utils/AxiosConfig";

const ObjectsTable = ({ objectSearch }) => {
  const selectedContainerId = useSelectedContainerStore(
    (state) => state.selectedContainerId
  );
  const currentParentPath = useParentPathStore(
    (state) => state.currentParentPath
  );

  const objects = useObjectStore((state) => state.objects);
  const updateObjectsList = useObjectStore((state) => state.updateObjectsList);

  const filteredItems = objects.filter((object) =>
    objectSearch.trim() === ""
      ? true
      : object.objectName.toLowerCase().includes(objectSearch.toLowerCase())
  );

  useEffect(() => {
    axiosInstance
      .get("/api/v1/object/get/children", {
        params: {
          containerId: selectedContainerId,
          parentPath: currentParentPath,
        },
      })
      .then((res) => updateObjectsList(res.data.payload))
      .catch((err) => console.log(err));
  }, [selectedContainerId, currentParentPath]);

  return (
    <div className="w-full h-fit max-h-[75vh] overflow-x-auto overflow-y-auto scrollbar-thin scrollbar-thumb-brand-primary-600 scrollbar-track-primary-bg">
      <div className="min-w-140">
        <div className="w-full p-2 flex items-center bg-brand-primary-200 rounded-t-lg">
          <div className="w-10"></div>
          <div className="flex w-full">
            <div className="w-1/4">Name</div>
            <div className="w-1/4">Type</div>
            <div className="w-1/4">Size</div>
            <div className="w-1/4">Created At</div>
          </div>
        </div>
        {filteredItems.map((obj, index) => (
          <ObjectEntry key={index} isEven={index % 2 === 0} object={obj} />
        ))}
      </div>
    </div>
  );
};

export default ObjectsTable;
