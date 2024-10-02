import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../../services/AuthContext'

function UserPage() {
  const Auth = useAuth()
  const user = Auth.getUser()
  const isUser = user.role === 'USER'



  
  if (!isUser) {
    return <Navigate to='/' />
  }

  return (
<div></div>
  )
}

export default UserPage