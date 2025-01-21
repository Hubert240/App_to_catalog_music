import React from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../services/AuthContext'
import styles from './Navbar.module.css'
import logo from './logo_musixlist.png'


function Navbar() {
  const { userIsAuthenticated, userLogout } = useAuth()

  const logout = () => {
    userLogout()
  }



  return (
    <nav className={styles.nav}>
      <div className={styles.logo}>
        <Link to="/">
          <img src={logo} alt="Logo" className={styles.logoImage} />
        </Link>
      </div>
      <ul className={styles.menu}>
        {userIsAuthenticated() && <li><Link to="/audio">Pliki</Link></li>}
        {userIsAuthenticated() && <li><Link to="/addaudio">Dodaj nowy plik</Link></li>}
        {userIsAuthenticated() && <li><Link to="/uploadaudio">Wgraj nowy plik</Link></li>}
        {userIsAuthenticated() && <li><Link to="/searchData">Wyszukaj dane pliku</Link></li>}
        {!userIsAuthenticated() && <li><Link to="/login">Logowanie</Link></li>}
        {!userIsAuthenticated() && <li><Link to="/signup">Rejestracja</Link></li>}
        {userIsAuthenticated() && <li><Link to="/userpage">Profil</Link></li>}
        {userIsAuthenticated() && <li><Link to="/" onClick={logout}>Wyloguj się</Link></li>}
      </ul>
    </nav>
  );
}

export default Navbar
