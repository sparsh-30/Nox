import { create } from "zustand";

const useParentPathStore = create((set) => ({
  currentParentPath: "/",
  setCurrentParentPath: (parentPath) => set({ currentParentPath: parentPath }),
}));

export default useParentPathStore;
