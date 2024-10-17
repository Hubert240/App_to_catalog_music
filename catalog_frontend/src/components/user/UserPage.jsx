import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../../services/AuthContext'

function UserPage() {
  const Auth = useAuth()
  const user = Auth.getUser()
  const userId = user.id;
  const name = user.username;
  const email = user.email;

  console.log(user);

  

  return (
<div>
<h2>Nazwa: {name}</h2>
<h2>id: {userId}</h2>
<p>Email: {email}</p>
</div>
  )
}

export default UserPage