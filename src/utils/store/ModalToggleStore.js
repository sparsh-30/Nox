import { create } from "zustand";

const useModalToggleStore = create((set) => ({
  singleFieldModal: "",
  updateSingleFieldModal: (fieldType) => set({ singleFieldModal: fieldType }),
  closeSingleFieldModal: () => set({ singleFieldModal: "" }),
}));

export default useModalToggleStore;
