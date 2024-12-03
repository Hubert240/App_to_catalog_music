import React, { useState } from 'react';
import { catalogApi } from '../../services/CatalogApi';
import { useAuth } from '../../services/AuthContext';
import styles from './UploadAudioPage.module.css';

const UploadAudioPage = () => {
  const { getUser } = useAuth();
  const user = getUser(); 
  const [audioFiles, setAudioFiles] = useState([]); 
  const [error, setError] = useState(null); 
  const [success, setSuccess] = useState(null);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [totalFiles, setTotalFiles] = useState(0);
  const [uploadedFiles, setUploadedFiles] = useState(0);

  const handleFolderChange = (event) => {
    const files = Array.from(event.target.files);
    const audioFiles = files.filter(file => file.type.startsWith('audio/'));

    if (audioFiles.length === 0) {
      setError("Folder nie zawiera plików audio.");
      return;
    }

    setAudioFiles(audioFiles);
    setTotalFiles(audioFiles.length);
    setUploadedFiles(0);
    setUploadProgress(0);
    startUpload(audioFiles);
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];

    if (file && file.type.startsWith('audio/')) {
      setAudioFiles([file]);
      setTotalFiles(1);
      setUploadedFiles(0);
      setUploadProgress(0);
      startUpload([file]);
    } else {
      setError("Proszę wybrać plik audio.");
    }
  };

  const startUpload = async (files) => {
    let progressPerFile = 100 / files.length;

    setUploadProgress(0);
    setError(null);
    setSuccess(null);

    try {
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        await catalogApi.uploadAudio(user, file, (progressEvent) => {
          const fileProgress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          const totalFileProgress = (i + 1) * progressPerFile;
          setUploadProgress(Math.round(totalFileProgress));
        });

        setUploadedFiles((prev) => prev + 1);
      }

      setSuccess("Pliki audio zostały pomyślnie przesłane!");
    } catch (error) {
      setError("Wystąpił błąd podczas przesyłania plików: " + error.message);
      console.error(error);
    }
  };

  return (
    <div className={styles.uploadContainer}>
      <h2 className={styles.heading}>Prześlij Pliki Audio</h2>

      <div className={styles.fileInputContainer}>
        <label htmlFor="audioFolder" className={styles.fileLabel}>
          Wybierz folder z plikami Audio:
        </label>
        <input
          type="file"
          id="audioFolder"
          webkitdirectory="true"
          directory=""
          multiple
          onChange={handleFolderChange}
          className={styles.fileInput}
        />
      </div>

      <div className={styles.fileInputContainer}>
        <label htmlFor="audioFile" className={styles.fileLabel}>
          Wybierz pojedynczy plik audio:
        </label>
        <input
          type="file"
          id="audioFile"
          onChange={handleFileChange}
          className={styles.fileInput}
        />
      </div>

      {error && <p className={styles.errorMessage}>{error}</p>}
      {success && <p className={styles.successMessage}>{success}</p>}

      {uploadProgress > 0 && (
        <div className={styles.progressContainer}>
          <p>Postęp przesyłania: {uploadProgress}% ({uploadedFiles} z {totalFiles} plików)</p>
          <progress value={uploadProgress} max="100" className={styles.progressBar}></progress>
        </div>
      )}
    </div>
  );
};

export default UploadAudioPage;
