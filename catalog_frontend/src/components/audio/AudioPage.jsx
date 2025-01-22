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
  const [page, setPage] = useState(1);
  const [sortField, setSortField] = useState('title');
  const [isAscending, setIsAscending] = useState(true);
  const [hasMore, setHasMore] = useState(true);

  const [filters, setFilters] = useState({
    artist: '',
    album: '',
    year: '',
    title: '',
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    handleGetAudio();
  }, [filters, page]);

  const handleGetAudio = async () => {
    if (loading) return;

    try {
      setLoading(true);
      const response = await catalogApi.getAudio(user, userId, filters, page - 1);
      console.log("Otrzymane dane:", response.data);
      const audioData = response.data;

      if (audioData.length > 0) {
        setAudio((prevAudio) => [...prevAudio, ...audioData]);
      } else {
        console.log('Brak nowych utworów do załadowania.');
        setHasMore(false);
      }
      setLoading(false);
    } catch (error) {
      handleLogError(error);
      setLoading(false);
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

      if (typeof aValue === 'number' && typeof bValue === 'number') {
        return newIsAscending ? aValue - bValue : bValue - aValue;
      } else if (typeof aValue === 'string' && typeof bValue === 'string') {
        return newIsAscending
          ? aValue.localeCompare(bValue)
          : bValue.localeCompare(aValue);
      }
      return 0;
    });
    setAudio(sortedAudio);
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
  
    setFilters({
      ...filters,
      [name]: value,
    });
  
    setAudio([]);
    setPage(1);
    setHasMore(true);
  };
  

  const handleResetFilters = () => {
    setFilters({
      artist: '',
      album: '',
      year: '',
      title: '',
    });
    setAudio([]); 
    setPage(1); 
    setHasMore(true);
  };

  const handleIncreasePage = () => {
    if (loading) return;
    setPage(prevPage => prevPage + 1);
  };

  useEffect(() => {
    const handleScroll = () => {
 
      if (
        window.innerHeight + window.scrollY >= document.documentElement.scrollHeight - 10 &&
        !loading
      ) {
        handleIncreasePage();
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [loading]);

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Lista Audio</h2>

      <div className={styles.filters}>
        <div className={styles.filterItem}>
          <label htmlFor="title">Tytuł:</label>
          <input
            type="text"
            id="title"
            name="title"
            value={filters.title}
            placeholder="Filtruj po tytułach"
            onChange={handleFilterChange}
          />
        </div>
        <div className={styles.filterItem}>
          <label htmlFor="artist">Artysta:</label>
          <input
            type="text"
            id="artist"
            name="artist"
            value={filters.artist}
            placeholder="Filtruj po artystach"
            onChange={handleFilterChange}
          />
        </div>
        <div className={styles.filterItem}>
          <label htmlFor="album">Album:</label>
          <input
            type="text"
            id="album"
            name="album"
            value={filters.album}
            placeholder="Filtruj po albumach"
            onChange={handleFilterChange}
          />
        </div>
        <div className={styles.filterItem}>
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
        <button onClick={handleResetFilters} className={styles.resetButton}>
          Resetuj <br /> filtry
        </button>
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
              {sortField === 'year' ? (isAscending ? '🠕' : '↓') : '🠕'}
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
              <button
                onClick={() => handleDeleteAudio(audioFile.id)}
                className={styles.deleteButton}
              >
                Usuń
              </button>
            </div>
          </div>
        ))}

        {loading && hasMore && <div>Ładowanie...</div>}


      </div>
    </div>
  );
}

export default AudioPage;
