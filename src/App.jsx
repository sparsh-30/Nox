import React from "react";
import ProtectedRoutes from "./routes-wrapper/ProtectedRoutes";
import AuthRoutes from "./routes-wrapper/AuthRoutes";
import Dashboard from "./pages/Dashboard";
import Register from "./pages/Register";
import Login from "./pages/Login";
import useAuthInit from "./utils/hooks/useAuthInit";
import { Routes, Route } from "react-router";

const App = () => {
  useAuthInit();
  return (
    <div className="text-sm font-poppins font-semibold">
      <Routes>
        <Route element={<ProtectedRoutes />}>
          <Route element={<Dashboard />} path="/dashboard" />
        </Route>
        <Route element={<AuthRoutes />}>
          <Route element={<Register />} path="/auth/register" />
          <Route element={<Login />} path="/auth/login" />
        </Route>
      </Routes>
    </div>
  );
};

export default App;
