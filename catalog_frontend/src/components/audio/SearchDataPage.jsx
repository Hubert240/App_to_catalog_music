import React, { useState } from 'react';
import { catalogApi } from '../../services/CatalogApi';
import { useAuth } from '../../services/AuthContext';
const SearchDataPage = () => {

    const { getUser } = useAuth();
    const user = getUser(); 
  const [audioFile, setAudioFile] = useState(null); 
  const [error, setError] = useState(null); 
  const [success, setSuccess] = useState(null);


  const handleFileChange = (event) => {
    setAudioFile(event.target.files[0]);
  };

  const handleUpload = async (event) => {
    event.preventDefault();
    setError(null); 
    setSuccess(null); 

    if (!audioFile) {
      setError("Proszę wybrać plik audio.");
      return;
    }

    try {
      const response = await catalogApi.searchAudioUpload(user, audioFile);
      setSuccess("Plik audio został pomyślnie przesłany!");
      console.log(response.data); 
    } catch (error) {
      setError("Wystąpił błąd podczas przesyłania pliku: " + error.message);
      console.error(error); 
    }
  };


  return (
    <div>

<div>
      <h2>Prześlij Plik Audio</h2>
      <form onSubmit={handleUpload}>
        <div>
          <label htmlFor="audioFile">Wybierz plik audio:</label>
          <input
            type="file"
            id="audioFile"
            accept="audio/*" 
            onChange={handleFileChange}
          />
        </div>
        <button type="submit">Wyślij</button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}
    </div>
    </div>
  );
};

export default SearchDataPage;
