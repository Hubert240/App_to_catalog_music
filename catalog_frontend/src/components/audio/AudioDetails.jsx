import React, { useState, useEffect } from 'react';
import { useParams,useNavigate } from 'react-router-dom';
import { catalogApi } from '../../services/CatalogApi';
import { handleLogError } from '../../services/Helpers';
import styles from './AudioDetails.module.css';
import { useAuth } from '../../services/AuthContext';

function AudioDetails() {
  const { id } = useParams();
  const [audioDetails, setAudioDetails] = useState(null);
  const Auth = useAuth();
  const user = Auth.getUser();
  const navigate = useNavigate();

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
    return <p>Ładowanie...</p>;
  }

  const handleBackToList = () => {
    navigate('/audio');
  };

  

  const handleDownloadAudio = () => {
    catalogApi.downloadAudio(user, id)
      .then(() => {
      })
      .catch(error => {
        handleLogError(error);
      });
  };

  
  return (
    <div>
 
    <div className={styles.detailsContainer}>
    <div className={styles.backContainer}>
      <button onClick={handleBackToList} className={styles.backButton}>
      ⭠ Powróć do listy
      </button>
      </div>
      <h2 className={styles.title}>{audioDetails.title}</h2>
      <div className={styles.downloadButtonContainer}>
      </div>
      <div className={styles.detailsGrid}>
        <div><span className={styles.labelAudio}>Artysta:</span><span className={styles.value}>{audioDetails.artist}</span></div>
        <div><span className={styles.labelAudio}>Numer utworu:</span><span className={styles.value}>{audioDetails.track}</span></div>
        <div><span className={styles.labelAudio}>Album:</span><span className={styles.value}>{audioDetails.album}</span></div>
        <div><span className={styles.labelAudio}>Rok:</span><span className={styles.value}>{audioDetails.year}</span></div>
        <div><span className={styles.labelAudio}>Gatunek:</span><span className={styles.value}>{audioDetails.genre}</span></div>
        <div><span className={styles.labelAudio}>Komentarz:</span><span className={styles.value}>{audioDetails.comment}</span></div>
        <div><span className={styles.labelAudio}>Tekst:</span><span className={styles.value}>{audioDetails.lyrics}</span></div>
        <div><span className={styles.labelAudio}>Kompozytor:</span><span className={styles.value}>{audioDetails.composer}</span></div>
        <div><span className={styles.labelAudio}>Wydawca:</span><span className={styles.value}>{audioDetails.publisher}</span></div>
        <div><span className={styles.labelAudio}>Artysta oryginalny:</span><span className={styles.value}>{audioDetails.originalArtist}</span></div>
        <div><span className={styles.labelAudio}>Artysta albumu:</span><span className={styles.value}>{audioDetails.albumArtist}</span></div>
        <div><span className={styles.labelAudio}>Prawa autorskie:</span><span className={styles.value}>{audioDetails.copyright}</span></div>
        <div><span className={styles.labelAudio}>URL:</span><span className={styles.value}>{audioDetails.url}</span></div>
        <div><span className={styles.labelAudio}>Format kodowania:</span><span className={styles.value}>{audioDetails.encoder}</span></div>
      </div>

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
             <button onClick={handleDownloadAudio} className={styles.downloadButton}>
          Pobierz plik audio
        </button>
    </div>
    
    </div>
  );
}

export default AudioDetails;
