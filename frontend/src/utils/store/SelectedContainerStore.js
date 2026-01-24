import { create } from "zustand";

const useSelectedContainerStore = create((set) => ({
  selectedContainerId: "",
  setSelectedContainerId: (containerId) => set({ selectedContainerId: containerId }),
}));

export default useSelectedContainerStore;
