import React, { useState } from 'react';
import { useAuth } from '../../services/AuthContext';
import { catalogApi } from '../../services/CatalogApi';
import { handleLogError } from '../../services/Helpers';

function AddAudioPage() {
  const Auth = useAuth();
  const user = Auth.getUser();

  const [audio, setAudio] = useState({
    artist: '',
    title: ''
  });
  const [file, setFile] = useState(null);
  const [coverArt, setCoverArt] = useState(null);

  const [isSuccess, setIsSuccess] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setAudio({
      ...audio,
      [name]: value
    });
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleCoverArtChange = (e) => {
    setCoverArt(e.target.files[0]);
  };


  const handleAddAudio = async () => {
    if (!file) {
      setErrorMessage('Wybierz plik');
      return;
    }

    const formData = new FormData();
    formData.append('artist', audio.artist);
    formData.append('title', audio.title);
    formData.append('audioFile', file);
    formData.append('coverArt', coverArt);
    formData.append('userId', user.id);
    try {
      setIsSuccess(false);
      setErrorMessage('');
      await catalogApi.addAudio(user, formData);
      setIsSuccess(true);
      setAudio({ artist: '', title: '' });
      setFile(null);
      setCoverArt(null);
    } catch (error) {
      handleLogError(error);
      setErrorMessage('Spróbuj ponownie.');
    }
  };

  return (
    <div>
      <h2>Dodaj nowy plik</h2>
      <form>
        <input
          type="text"
          name="artist"
          placeholder="Artist"
          value={audio.artist}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="title"
          placeholder="title"
          value={audio.title}
          onChange={handleInputChange}
        />
        <input
          type="file"
          accept="audio/*"
          onChange={handleFileChange}
        />
        <input
          type="file"
          accept="image/*"
          onChange={handleCoverArtChange}
        />
        <button type="button" onClick={handleAddAudio}>Dodaj</button>
      </form>
      
      {isSuccess && <p>Poprawnie dodano nowy plik</p>}
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
    </div>
  );
}

export default AddAudioPage;
