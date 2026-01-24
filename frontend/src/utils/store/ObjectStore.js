import { create } from "zustand";

const useObjectStore = create((set) => ({
  objects: [],

  updateObjectsList: (newObjectsList) => set({ objects: newObjectsList }),

  addNewObject: (newObject) =>
    set((state) => ({
      objects: [...state.objects, newObject],
    })),

  removeObject: (objectId) =>
    set((state) => ({
      objects: state.objects.filter((obj) => obj.objectId !== objectId),
    })),
}));

export default useObjectStore;
