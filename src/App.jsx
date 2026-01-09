import React from 'react'
import Dashboard from './pages/Dashboard'
import Register from './pages/Register'
import Login from './pages/Login'
import { Routes, Route } from 'react-router'

const App = () => {
  return (
    <div className='text-sm font-poppins font-semibold'>
      {/* <Dashboard /> */}
      {/* <Register /> */}
      {/* <Login /> */}
      <Routes>
        <Route element={<Register />} path='/auth/register' />
        <Route element={<Login />} path='/auth/login' />
      </Routes>
    </div>
  )
}

export default App