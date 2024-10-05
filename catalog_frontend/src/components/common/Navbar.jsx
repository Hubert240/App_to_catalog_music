import React from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../services/AuthContext'
import "./Navbar.module.css"

function Navbar() {
  const { userIsAuthenticated, userLogout } = useAuth()

  const logout = () => {
    userLogout()
  }



  return (
    <nav>
      <ul>
      <li><Link to="/">Katalog</Link></li>
        {userIsAuthenticated() && <li><Link to="/adminpage">Adminpage</Link></li>}
        {userIsAuthenticated() && <li><Link to="/userpage">Userpage</Link></li>}
        {userIsAuthenticated() && <li><Link to="/audio">Pliki</Link></li>}
        {!userIsAuthenticated() && <li><Link to="/login">Logowanie</Link></li>}
          {!userIsAuthenticated() && <li><Link to="/signup">Rejestracja</Link></li>}
          {userIsAuthenticated() && <li><Link to="/" onClick={logout}>Wyloguj się</Link></li>}
        </ul>
    </nav>
  )
}

export default Navbar
