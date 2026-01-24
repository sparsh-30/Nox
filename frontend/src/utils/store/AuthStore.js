import { create } from "zustand";

const useAuthStore = create((set) => ({
  authToken: null,
  isAuthenticated: false,
  authenticateUser: (token) => set({ authToken: token, isAuthenticated: !!token }),
  logOutUser: () => set({ authToken: "", isAuthenticated: false }),
}));

export default useAuthStore;
