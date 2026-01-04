import React from "react";
import ObjectEntry from "./ObjectEntry";

const ObjectsTable = () => {
  const dummyData = [
    {
      objectName: "obj1",
      objectType: "FOLDER",
      size: 3456,
      createdAt: Date.now(),
    },
    {
      objectName: "obj2",
      objectType: "FILE",
      size: 67543,
      createdAt: Date.now(),
    },
    { objectName: "obj3", objectType: "FILE", size: 3, createdAt: Date.now() },
    {
      objectName: "obj4",
      objectType: "FOLDER",
      size: 3774,
      createdAt: Date.now(),
    },
    { objectName: "obj5", objectType: "FILE", size: 65, createdAt: Date.now() },
    {
      objectName: "obj6",
      objectType: "FOLDER",
      size: 3475,
      createdAt: Date.now(),
    },
    {
      objectName: "obj1",
      objectType: "FOLDER",
      size: 3456,
      createdAt: Date.now(),
    },
    {
      objectName: "obj2",
      objectType: "FILE",
      size: 67543,
      createdAt: Date.now(),
    },
    { objectName: "obj3", objectType: "FILE", size: 3, createdAt: Date.now() },
    {
      objectName: "obj4",
      objectType: "FOLDER",
      size: 3774,
      createdAt: Date.now(),
    },
    { objectName: "obj5", objectType: "FILE", size: 65, createdAt: Date.now() },
    {
      objectName: "obj6",
      objectType: "FOLDER",
      size: 3475,
      createdAt: Date.now(),
    },
  ];
  return (
    <div className="w-full h-[75vh] overflow-x-auto overflow-y-auto scrollbar-thin scrollbar-thumb-brand-primary-600 scrollbar-track-primary-bg">
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
        {dummyData.map((obj, index) => (
          <ObjectEntry key={index} isEven={index % 2 === 0} object={obj} />
        ))}
      </div>
    </div>
  );
};

export default ObjectsTable;
