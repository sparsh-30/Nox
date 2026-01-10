import { Navigate, Outlet } from "react-router";
import useAuthStore from "../utils/store/AuthStore";

const AuthRoutes = () => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  return <Outlet />;
};

export default AuthRoutes;
