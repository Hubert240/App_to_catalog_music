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
          const user = getUser();
          const response = await catalogApi.numberOfAudio(user, user.id);
          setAudioCount(response.data);
        }
      } catch (error) {
        console.error("Błąd podczas pobierania liczby audio:", error);
      }
    };

    fetchAudioCount();
  }, [userIsAuthenticated, getUser]);

  const isAuthenticated = userIsAuthenticated();

  return (
    <div className={styles.homecontainer}>
      <h1>Witaj w aplikacji MusiCat</h1>
      {isAuthenticated ? (
        <>
          <p>Aktualnie przechowujesz {audioCount} plików</p>
          <div className={styles.description}>
            <div className={styles.tab}>
              <h2>Dodaj nowy plik:</h2>
              <p>W tej zakładce możesz wgrać plik audio oraz przypisać do niego metadane takie jak tytuł, artysta, album i inne.</p>
            </div>
            <div className={styles.tab}>
              <h2>Wgraj plik z metadanymi:</h2>
              <p>Ta zakładka automatycznie wgrywa plik audio i odczytuje metadane z pliku, takie jak tytuł, artysta i album.</p>
            </div>
            <div className={styles.tab}>
              <h2>Wyszukaj dane pliku:</h2>
              <p>W tej zakładce możesz wgrać plik audio, a aplikacja automatycznie poszuka jego metadane w MusicBrainz i Artchive.</p>
            </div>
          </div>
        </>
      ) : (
        <>
          <p>Zaloguj się, aby dodawać nowe pliki</p>
          <p>Jeśli nie masz konta, kliknij przycisk rejestracja</p>
        </>
      )}
    </div>
  );
}

export default Home;
