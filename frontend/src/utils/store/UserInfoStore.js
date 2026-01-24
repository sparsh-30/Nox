import { create } from "zustand";

const useUserInfoStore = create((set) => ({
  username: "",
  userId: "",
  updateUserInfo: (user) =>
    set({ username: user.username, userId: user.userId }),
  removeUserInfo: () => set({ username: "", userId: "" }),
}));

export default useUserInfoStore;
