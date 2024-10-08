import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../../services/AuthContext'

function UserPage() {
  const Auth = useAuth()
  const user = Auth.getUser()



  

  return (
<div></div>
  )
}

export default UserPage