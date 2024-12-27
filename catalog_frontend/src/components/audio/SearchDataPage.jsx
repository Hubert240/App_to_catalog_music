import React, { useState } from 'react';
import { catalogApi } from '../../services/CatalogApi';
import { useAuth } from '../../services/AuthContext';
import styles from './SearchDataPage.module.css';

const SearchDataPage = () => {
  const { getUser } = useAuth();
  const user = getUser();

  const [uploadProgress, setUploadProgress] = useState(0);
  const [uploadedFiles, setUploadedFiles] = useState(0);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  // Funkcja do obsługi przesyłania pojedynczego pliku
  const handleSingleFileUpload = async (event) => {
    const file = event.target.files[0];
    if (!file || !file.type.startsWith('audio/')) {
      setError('Proszę wybrać poprawny plik audio.');
      return;
    }

    setError(null);
    setSuccess(null);

    try {
      await catalogApi.searchAudioUpload(user, file);
      setSuccess('Plik audio został pomyślnie przesłany!');
    } catch (err) {
      setError('Wystąpił błąd podczas przesyłania pliku: ' + err.message);
      console.error(err);
    }
  };

  // Funkcja do obsługi przesyłania folderu z plikami
  const handleFolderUpload = async (event) => {
    const files = Array.from(event.target.files);
    const audioFiles = files.filter((file) => file.type.startsWith('audio/'));

    if (audioFiles.length === 0) {
      setError('Folder nie zawiera plików audio.');
      return;
    }

    setError(null);
    setSuccess(null);
    setUploadProgress(0);
    setUploadedFiles(0);

    try {
      const progressPerFile = 100 / audioFiles.length;

      for (let i = 0; i < audioFiles.length; i++) {
        const file = audioFiles[i];
        await catalogApi.searchAudioUpload(user, file);
        setUploadedFiles((prev) => prev + 1);
        setUploadProgress((i + 1) * progressPerFile);
      }

      setSuccess('Pliki audio zostały pomyślnie przesłane!');
    } catch (err) {
      setError('Domena ArtArchive nie jest aktulanie dostępna, spróbuj później.');
      console.error(err);
    }
  };

  return (
    <div className={styles.uploadContainer}>
      <h2 className={styles.heading}> Prześlij Pliki Audio</h2>
      <p>Prześlij plik audio, a brakujące metadane zostaną pobrane z MusicBrainz i ArtArchive.</p>
      <div className={styles.fileInputContainer}>


      <label htmlFor="audioFolder" className={styles.fileLabel}>
          Wybierz folder z plikami Audio:
        </label>
        <label htmlFor="audioFolder" className={styles.customButton}>Wybierz folder</label>
        <input
          type="file"
          webkitdirectory="true"
          directory="true"
          multiple
          onChange={handleFolderUpload} // Folder przesyłany automatycznie po wybraniu
          className={styles.fileInput}
          id="audioFolder"
        />
      </div>
      <div>
      <label htmlFor="audioFile" className={styles.fileLabel}>
          Wybierz pojedynczy plik audio:
        </label>
        <label htmlFor="audioFile" className={styles.customButton}>Wybierz plik audio</label>
        
        <input
          type="file"
          accept="audio/*"
          id="audioFile"
          onChange={handleSingleFileUpload} // Plik przesyłany automatycznie po wybraniu
          className={styles.fileInput}
        />
      </div>

      {/* Pasek postępu dla folderu */}
      {uploadedFiles > 0 && (
        <p>
          Przesłano {uploadedFiles} plików. Postęp: {uploadProgress.toFixed(2)}%
        </p>
      )}

      {/* Komunikaty o błędach i sukcesach */}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}
    </div>
  );
};

export default SearchDataPage;
