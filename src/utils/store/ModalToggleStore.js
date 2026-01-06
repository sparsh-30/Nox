import { create } from "zustand";

const useModalToggleStore = create((set) => ({
  singleFieldModal: "",
  confirmationModal: "",
  fileInputModal: "",
  miscellaneousData: "",

  updateSingleFieldModal: (fieldType) => set({ singleFieldModal: fieldType }),
  closeSingleFieldModal: () => set({ singleFieldModal: "" }),

  updateConfirmationModal: (fieldType) => set({ confirmationModal: fieldType }),
  closeConfirmationModal: () => set({ confirmationModal: "" }),

  updateFileInputModal: (fieldType) => set({ fileInputModal: fieldType }),
  closeFileInputModal: () => set({ fileInputModal: "" }),

  updateMiscellaneousData: (data) => set({ miscellaneousData: data }),
  clearMiscellaneousData: () => set({ miscellaneousData: "" }),
}));

export default useModalToggleStore;
