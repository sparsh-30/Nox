import { create } from "zustand";

const useModalToggleStore = create((set) => ({
  singleFieldModal: "",
  confirmationModal: "",
  miscellaneousData: "",

  updateSingleFieldModal: (fieldType) => set({ singleFieldModal: fieldType }),
  closeSingleFieldModal: () => set({ singleFieldModal: "" }),

  updateConfirmationModal: (fieldType) => set({ confirmationModal: fieldType }),
  closeConfirmationModal: () => set({ confirmationModal: "" }),

  updateMiscellaneousData: (data) => set({ miscellaneousData: data }),
  clearMiscellaneousData: () => set({ miscellaneousData: "" }),
}));

export default useModalToggleStore;
