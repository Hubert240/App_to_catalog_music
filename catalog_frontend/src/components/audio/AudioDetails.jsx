import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { catalogApi } from '../../services/CatalogApi'; // Zakładam, że API jest obsługiwane przez catalogApi
import { handleLogError } from '../../services/Helpers';
import styles from './AudioDetails.module.css'; // CSS dla stylów
import { useAuth } from '../../services/AuthContext';

function AudioDetails() {
  const { id } = useParams(); // Pobierz ID z trasy
  const [audioDetails, setAudioDetails] = useState(null);
  const Auth = useAuth();
  const user = Auth.getUser();

  useEffect(() => {
    fetchAudioDetails();
  }, [id]);

  const fetchAudioDetails = async () => {
    try {
      const response = await catalogApi.getAudioDetails(user, id);
      setAudioDetails(response.data);
    } catch (error) {
      handleLogError(error);
    }
  };




  if (!audioDetails) {
    return <p>Ładowanie...</p>; // Wyświetl komunikat, gdy dane się ładują
  }

  return (
    <div className={styles.detailsContainer}>
      <h2 className={styles.title}>{audioDetails.title}</h2>
      <p className={styles.artist}>Artysta: {audioDetails.artist}</p>
      <p className={styles.user}>Użytkownik: {audioDetails.user ? audioDetails.user.username : 'Nieznany'}</p>

      {audioDetails.coverArt && (
        <div className={styles.cover}>
          <img
            src={`data:image/png;base64,${audioDetails.coverArt}`}
            alt={`Okładka: ${audioDetails.title}`}
            className={styles.coverArt}
          />
        </div>
      )}

      {audioDetails.audioFile && (
        <div className={styles.audioPlayer}>
          <audio controls>
            <source src={`data:audio/wav;base64,${audioDetails.audioFile}`} type="audio/wav" />
          </audio>
        </div>
      )}

      {/* Dodatkowe szczegóły */}
      <p className={styles.description}>Opis pliku audio...</p>
    </div>
  );
}

export default AudioDetails;
