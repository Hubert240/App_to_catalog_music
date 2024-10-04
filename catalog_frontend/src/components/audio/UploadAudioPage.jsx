import React, { useState } from 'react';
import { catalogApi } from '../../services/CatalogApi'; // Zakładam, że ta funkcja jest w CatalogApi.js
import { useAuth } from '../../services/AuthContext';

const UploadAudioPage = () => {
  const { getUser } = useAuth();
  const user = getUser(); // Uzyskaj dane użytkownika
  const [audioFile, setAudioFile] = useState(null); // Stan dla pliku audio
  const [error, setError] = useState(null); // Stan dla błędów
  const [success, setSuccess] = useState(null); // Stan dla sukcesu

  const handleFileChange = (event) => {
    setAudioFile(event.target.files[0]); // Ustaw plik audio
  };

  const handleUpload = async (event) => {
    event.preventDefault();
    setError(null); // Resetuj błędy
    setSuccess(null); // Resetuj sukces

    if (!audioFile) {
      setError("Proszę wybrać plik audio.");
      return;
    }

    try {
      const response = await catalogApi.uploadAudio(user, audioFile); // Wywołaj funkcję uploadAudio
      setSuccess("Plik audio został pomyślnie przesłany!");
      console.log(response.data); // Loguj odpowiedź
    } catch (error) {
      setError("Wystąpił błąd podczas przesyłania pliku: " + error.message);
      console.error(error); // Loguj błąd
    }
  };

  return (
    <div>
      <h2>Prześlij Plik Audio</h2>
      <form onSubmit={handleUpload}>
        <div>
          <label htmlFor="audioFile">Wybierz plik audio:</label>
          <input
            type="file"
            id="audioFile"
            accept="audio/*" // Akceptuj tylko pliki audio
            onChange={handleFileChange}
          />
        </div>
        <button type="submit">Wyślij</button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}
    </div>
  );
};

export default UploadAudioPage;
