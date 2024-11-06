import React, { useState, useEffect } from 'react';
import { handleLogError } from '../../services/Helpers';
import { catalogApi } from '../../services/CatalogApi';
import { useAuth } from '../../services/AuthContext';
import { Link } from 'react-router-dom';
import styles from './AudioPage.module.css';

function AudioPage() {
  const Auth = useAuth();
  const user = Auth.getUser();
  const userId = user.id;

  const [audio, setAudio] = useState([]);
  const [title, setTitle] = useState('');
  const [searchTitle, setSearchTitle] = useState('');
  const [sortField, setSortField] = useState('title'); // Domyślne pole do sortowania
  const [isAscending, setIsAscending] = useState(true); // Domyślny kierunek sortowania
  const [selectedSortOption, setSelectedSortOption] = useState(''); // Dla kolumny sortowania z listą rozwijaną

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
      const response = await catalogApi.getAudio(user, userId, searchTitle);
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

  const handleSort = (field) => {
    const newIsAscending = sortField === field ? !isAscending : true;

    setSortField(field);
    setIsAscending(newIsAscending);

    const sortedAudio = [...audio].sort((a, b) => {
      const aValue = a[field];
      const bValue = b[field];

      if (typeof aValue === 'string' && typeof bValue === 'string') {
        return newIsAscending ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
      } else {
        return newIsAscending ? aValue - bValue : bValue - aValue;
      }
    });

    setAudio(sortedAudio);
  };

  const handleSelectSortOption = (event) => {
    const selectedOption = event.target.value;
    setSelectedSortOption(selectedOption);
    handleSort(selectedOption);
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
      </div>
      <div className={styles.table}>
        <div className={styles.headerAudio}>
          <div className={styles.columnAudio}>
            Tytuł
            <button onClick={() => handleSort('title')} className={styles.sortButton}>
              {sortField === 'title' ? (isAscending ? 'A-Z' : 'Z-A') : 'A-Z'}
            </button>
          </div>
          <div className={styles.columnAudio}>
            Artysta
            <button onClick={() => handleSort('artist')} className={styles.sortButton}>
              {sortField === 'artist' ? (isAscending ? 'A-Z' : 'Z-A') : 'A-Z'}
            </button>
          </div>
          <div className={styles.columnAudio}>
            Album
            <button onClick={() => handleSort('album')} className={styles.sortButton}>
              {sortField === 'album' ? (isAscending ? 'A-Z' : 'Z-A') : 'A-Z'}
            </button>
          </div>
          <div className={styles.columnAudio}>
            Rok
            <button onClick={() => handleSort('year')} className={styles.sortButton}>
              {sortField === 'year' ? (isAscending ? '🠕' : '🠗') : '🠕'}
            </button>
          </div>
          <div className={styles.columnAudio}>
            Sortowanie
            <select
              value={selectedSortOption}
              onChange={handleSelectSortOption}
              className={styles.selectSort}
            >
              <option value="title">Tytuł</option>
              <option value="artist">Artysta</option>
              <option value="album">Album</option>
              <option value="year">Rok</option>
            </select>
          </div>
        </div>
        {audio.map((audioFile) => (
          <div key={audioFile.id} className={styles.rowAudio}>
            <div className={styles.columnAudio}>{audioFile.title}</div>
            <div className={styles.columnAudio}>{audioFile.artist}</div>
            <div className={styles.columnAudio}>{audioFile.album}</div>
            <div className={styles.columnAudio}>{audioFile.year}</div>
            <div className={styles.columnAudio}>
              <Link className={styles.detailsButton} to={`/audiodetails/${audioFile.id}`}>
                Zobacz szczegóły
              </Link>
            </div>
            <div className={styles.columnAudio}>
              <button className={styles.deleteButton} onClick={() => handleDeleteAudio(audioFile.id)}>
                Usuń
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default AudioPage;
