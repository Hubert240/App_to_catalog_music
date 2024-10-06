import React from 'react'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './services/AuthContext'
import PrivateRoute from './services/PrivateRoute'
import Navbar from './components/common/Navbar'
import Home from './components/home/Home'
import Login from './components/authorization/LoginPage'
import Signup from './components/authorization/RegistrationPage'
import AdminPage from './components/admin/AdminPage'
import UserPage from './components/user/UserPage'
import AudioPage from './components/audio/AudioPage'
import AddAudioPage from './components/audio/AddAudioPage'
import UploadAudioPage from './components/audio/UploadAudioPage'
import AudioDetails from './components/audio/AudioDetails'

function App() {
  return (
    <AuthProvider>
      <Router>
        <Navbar />
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/login' element={<Login />} />
          <Route path='/signup' element={<Signup />} />
          <Route path="/adminpage" element={<PrivateRoute><AdminPage /></PrivateRoute>} />
          <Route path="/userpage" element={<PrivateRoute><UserPage /></PrivateRoute>} />
          <Route path="/audio" element={<PrivateRoute><AudioPage/></PrivateRoute>}/>
          <Route path="/addaudio" element={<PrivateRoute><AddAudioPage/></PrivateRoute>}/>
          <Route path="/uploadaudio" element={<PrivateRoute><UploadAudioPage/></PrivateRoute>}/>
          <Route path="/audiodetails/:id" element={<PrivateRoute><AudioDetails /></PrivateRoute>} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </Router>
    </AuthProvider>
  )
}

export default App
