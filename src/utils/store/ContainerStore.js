import { create } from "zustand";

const useContainerStore = create((set) => ({
  containers: [],

  updateContainersList: (newContainersList) =>
    set({ containers: newContainersList }),

  addNewContainer: (newContainer) =>
    set((state) => ({
      containers: [...state.containers, newContainer],
    })),

  removeContainer: (containerId) =>
    set((state) => ({
      containers: state.containers.filter(
        (cont) => cont.containerId !== containerId
      ),
    })),
}));

export default useContainerStore;
