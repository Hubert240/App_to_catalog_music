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
  const [sortField, setSortField] = useState('title');
  const [isAscending, setIsAscending] = useState(true);
  const [filterVisible, setFilterVisible] = useState(false);
  const [filters, setFilters] = useState({
    artist: '',
    album: '',
    year: '',
  });

  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      setSearchTitle(title);
    }, 500);

    return () => clearTimeout(delayDebounceFn);
  }, [title]);

  useEffect(() => {
    handleGetAudio();
  }, [searchTitle, filters]);

  const handleGetAudio = async () => {
    try {
      const response = await catalogApi.getAudio(user, userId, searchTitle, filters);
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
      return newIsAscending
        ? aValue.localeCompare(bValue)
        : bValue.localeCompare(aValue);
    });
    setAudio(sortedAudio);
  };

  const handleFilterChange = (e) => {
    setFilters({
      ...filters,
      [e.target.name]: e.target.value,
    });
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

      <button
        onClick={() => setFilterVisible(!filterVisible)}
        className={styles.filterButton}
      >
        Filtruj
      </button>

      {filterVisible && (
  <div className={styles.overlay}>
    <div className={styles.filterPopup}>
      <div className={styles.columnAudioResults}>
        <div className={styles.columnItem}>
          <label htmlFor="artist">Artysta:</label>
          <input
            type="text"
            id="artist"
            name="artist"
            value={filters.artist}
            onChange={handleFilterChange}
          />
        </div>
        <div className={styles.columnItem}>
          <label htmlFor="album">Album:</label>
          <input
            type="text"
            id="album"
            name="album"
            value={filters.album}
            onChange={handleFilterChange}
          />
        </div>
        <div className={styles.columnItem}>
        <label htmlFor="year">Rok:</label>
        <select
          id="year"
          name="year"
          value={filters.year}
          onChange={handleFilterChange}
        >
          <option value="">Wybierz rok</option>
          {[...Array(new Date().getFullYear() - 1900 + 1)].map((_, index) => {
            const year = 1900 + index;
            return (
              <option key={year} value={year}>
                {year}
              </option>
            );
          })}
        </select>
      </div>
      </div>

      <button
        onClick={() => {
          handleGetAudio(); // Pobrać dane po kliknięciu
          setFilterVisible(false); // Zamknij okienko po kliknięciu
        }}
        className={styles.applyButton}
      >
        Zatwierdź
      </button>

      <button
        onClick={() => setFilterVisible(false)}
        className={styles.closeButton}
      >
        Zamknij
      </button>
    </div>
  </div>
)}

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
              {sortField === 'year' ? (isAscending ? '🠕' : '🠖') : '🠕'}
            </button>
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
