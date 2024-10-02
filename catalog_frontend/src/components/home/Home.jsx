import React, { useState, useEffect } from 'react';
import { catalogApi } from '../../services/CatalogApi';
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
    <div>
      <h1>Witaj na stronie głównej</h1>
      <p>Liczba plików audio: {audioCount}</p>
    </div>
  );
}

export default Home;
