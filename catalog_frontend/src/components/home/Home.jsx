import React, { useState, useEffect } from 'react';
import { catalogApi } from '../../services/CatalogApi';
import styles from './Home.module.css';

function Home() {
  const [audioCount, setAudioCount] = useState(0);

  useEffect(() => {
    const fetchAudioCount = async () => {
      try {
        const response = await catalogApi.numberOfAudio();
        setAudioCount(response.data);
      } catch (error) {
        console.error("Błąd podczas pobierania liczby audio:", error);
      }
    };

    fetchAudioCount();
  }, []);

  return (
    <div className={styles['home-container']}>
      <h1>Witaj na stronie głównej MusixList</h1>
      <p>Liczba plików audio: {audioCount}</p>
      <div className={styles['neon-border']}></div> {/* Efekt neonowego obramowania */}
    </div>
  );
}

export default Home;
