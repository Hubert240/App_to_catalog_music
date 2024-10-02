import React, { useState } from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../../services/AuthContext'
import { catalogApi } from '../../services/CatalogApi'
import { handleLogError } from '../../services/Helpers'

import styles from './RegistrationPage.module.css'

function Signup() {
  const Auth = useAuth()
  const isLoggedIn = Auth.userIsAuthenticated()

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [isError, setIsError] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')


  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!(username && password && name && email)) {
      setIsError(true)
      setErrorMessage('Wpisz wszystkie pola')
      return
    }

    const user = { username, password, name, email }

    try {
      const response = await catalogApi.signup(user)
      const { id, name, role } = response.data
      const authdata = window.btoa(username + ':' + password)
      const authenticatedUser = { id, name, role, authdata }

      Auth.userLogin(authenticatedUser)

      setIsError(false)
      setErrorMessage('')
    } catch (error) {
      handleLogError(error)
      if (error.response && error.response.data) {
        const errorData = error.response.data
        let errorMessage = 'Invalid fields'
        if (errorData.status === 409) {
          errorMessage = errorData.message
        } else if (errorData.status === 400) {
          errorMessage = errorData.errors[0].defaultMessage
        }
        setIsError(true)
        setErrorMessage(errorMessage)
      }
    }
  }

  if (isLoggedIn) {
    return <Navigate to='/' />
  }

  return (
    <div  className={styles.container}>
        <h2 className={styles.heading}>Rejestracja</h2>
        {isError && <div>{errorMessage}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
          <label className={styles.registrationLabel}>Nazwa:</label>
            <input className={styles.inputField}
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div className="form-group">
          <label className={styles.registrationLabel}>Hasło:</label>
            <input className={styles.inputField}
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <div className="form-group">
          <label className={styles.registrationLabel}>Nazwa:</label>
            <input className={styles.inputField}
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
          <div className="form-group">
          <label className={styles.registrationLabel}>Email:</label>
            <input className={styles.inputField}
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <button className={styles.registrationButton} type="submit" > Zarejestruj się </button>
        </form>
      </div>
  );
}


export default Signup