import React, { useState } from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../../services/AuthContext'
import { catalogApi } from '../../services/CatalogApi'
import { handleLogError } from '../../services/Helpers'

import styles from './LoginPage.module.css'

function Login() {
  const Auth = useAuth()
  const isLoggedIn = Auth.userIsAuthenticated()

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [isError, setIsError] = useState(false)


  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!(username && password)) {
      setIsError(true)
      return
    }

    try {
      const response = await catalogApi.authenticate(username, password)
      const { id, name, role } = response.data
      const authdata = window.btoa(username + ':' + password)
      const authenticatedUser = { id, name, role, authdata }

      Auth.userLogin(authenticatedUser)

      setUsername('')
      setPassword('')
      setIsError(false)
    } catch (error) {
      handleLogError(error)
      setIsError(true)
    }
  }

  if (isLoggedIn) {
    return <Navigate to={'/'} />
  }

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Login</h2>
      {isError && <p className="error-message">Nazwa lub hasło są błędne</p>}
      <form onSubmit={handleSubmit} >
        <div className="form-group"> 
        <label className={styles.loginLabel}>Nazwa: </label>
          <input className={styles.inputField} type="text" value={username} onChange={(e) => setUsername(e.target.value)}/>
        </div>
        <div className="form-group">
        <label className={styles.loginLabel}>Hasło: </label>
          <input className={styles.inputField} type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
        </div>
        <button className={styles.loginButton} type="submit">Login</button>
      </form>
    </div>
  );
}


export default Login