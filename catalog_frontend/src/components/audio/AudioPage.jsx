import React, { useState, useEffect } from 'react';
import { handleLogError } from '../../services/Helpers';
import { catalogApi } from '../../services/CatalogApi';
import { useAuth } from '../../services/AuthContext';
import { Link } from 'react-router-dom'
import styles from './AudioPage.module.css'

function AudioPage() {
  const Auth = useAuth();
  const user = Auth.getUser();

  const [audio, setAudio] = useState([]);
  const [title, setTitle] = useState('');
  const [searchTitle, setSearchTitle] = useState('');


  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      setSearchTitle(title);
    }, 500);

    return () => clearTimeout(delayDebounceFn);
  }, [title]);

  useEffect(() => {
    handleGetAudio();
  }, [searchTitle]);

  const handleGetAudio = async () => {
    try {
      const response = await catalogApi.getAudio(user, searchTitle);
      const audioData = response.data;
      setAudio(audioData);
    } catch (error) {
      handleLogError(error);
    }
  };

 
  const handleDeleteAudio = async (id) => {
    try {
      await catalogApi.deleteAudio(user, id);
      setAudio(audio.filter((audioFile) => audioFile.id !== id));
    } catch (error) {
      handleLogError(error);
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Lista Audio</h2>
      <div className={styles.searchInput}>
        <input
          type="text"
          placeholder="Wyszukaj po tytule"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <div>
      <Link to="/addaudio" className={styles.addButton}>
        Dodaj nowy plik
      </Link>
    </div>
    <div>
      <Link to="/uploadaudio" className={styles.addButton}>
        Wgraj nowy plik
      </Link>
    </div>
      </div>
      <div className={styles.table}>
        <div className={styles.header}>
          <div className={styles.column}>Tytuł</div>
          <div className={styles.column}>Artysta</div>
          <div className={styles.column}> </div>
          <div className={styles.column}> </div>
        </div>
        {audio.map((audioFile) => (
          <div key={audioFile.id} className={styles.row}>
            <div className={styles.column}>{audioFile.title}</div>
            <div className={styles.column}>{audioFile.artist}</div>
      
            <div className={styles.column}>
            <div className={styles.buttonContainer}>
              <Link className={styles.detailsButton} to={`/audiodetails/${audioFile.id}`}>
                Zobacz szczegóły
              </Link>
             </div>
             </div>
            <div className={styles.column}> 
              <button className={styles.deleteButton} onClick={() => handleDeleteAudio(audioFile.id)}>Usuń</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
  
}

export default AudioPage;
