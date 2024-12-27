import React, { useState, useEffect } from 'react';
import { catalogApi } from '../../services/CatalogApi';
import styles from './Home.module.css';
import { useAuth } from '../../services/AuthContext';

function Home() {
  const [audioCount, setAudioCount] = useState(0);
  const { userIsAuthenticated, getUser } = useAuth();

  useEffect(() => {
    const fetchAudioCount = async () => {
      try {
        if (userIsAuthenticated()) {
          const user = getUser(); // Get the user inside the useEffect hook
          const response = await catalogApi.numberOfAudio(user,user.id);
          setAudioCount(response.data);
        }
      } catch (error) {
        console.error("Błąd podczas pobierania liczby audio:", error);
      }
    };

    fetchAudioCount();
  }, [userIsAuthenticated, getUser]); // Add dependencies to useEffect

  return (
    <div className={styles.homecontainer}>
      <h1>Witaj w aplikacji MusixList</h1>
      {userIsAuthenticated() && <p>Aktualnie przechowujesz {audioCount} plików</p>}
      {!userIsAuthenticated() && <p>Zarejestruj się aby katalogować swoje pliki</p>}
    </div>
  );
}

export default Home;
