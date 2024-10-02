import React, { useState, useEffect } from 'react';
import { handleLogError } from '../../services/Helpers';
import { catalogApi } from '../../services/CatalogApi';
import { useAuth } from '../../services/AuthContext';

function AudioPage() {
  const Auth = useAuth();
  const user = Auth.getUser();

  const [audio, setAudio] = useState([]);
  const [title, setTitle] = useState('');
  const [searchTitle, setSearchTitle] = useState('');

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

  const handleSearch = () => {
    setSearchTitle(title);
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
    <div>
      <h2>Lista Audio</h2>
      <div>
        <input
          type="text"
          placeholder="Wyszukaj po tytule"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <button onClick={handleSearch}>Szukaj</button>
      </div>
      <table>
        <thead>
          <tr>
            <th>Tytuł</th>
            <th>Artysta</th>
            <th>Użytkownik</th>
            <th>Okładka</th>
            <th>Plik</th>
            <th>Akcje</th>
          </tr>
        </thead>
        <tbody>
          {audio.map((audioFile) => (
            <tr key={audioFile.id}>
              <td>{audioFile.title}</td>
              <td>{audioFile.artist}</td>
              <td>{audioFile.user ? audioFile.user.username : 'Nieznany'}</td>
              <td>
                {audioFile.coverArt && (
                  <img
                    src={`data:image/png;base64,${audioFile.coverArt}`}
                    alt={`Okładka: ${audioFile.title}`}
                    style={{ width: '50px', height: '50px' }}
                  />
                )}
              </td>
              <td>
                {audioFile.audioFile && (
                  <audio controls>
                    <source src={`data:audio/wav;base64,${audioFile.audioFile}`} type="audio/wav" />
                  </audio>
                )}
              </td>
              <td>
                <button onClick={() => handleDeleteAudio(audioFile.id)}>Usuń</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AudioPage;
