import React, { useState } from 'react';
import { catalogApi } from '../../services/CatalogApi';
import { useAuth } from '../../services/AuthContext';

const UploadAudioPage = () => {
  const { getUser } = useAuth();
  const user = getUser(); 
  const [audioFiles, setAudioFiles] = useState([]); 
  const [error, setError] = useState(null); 
  const [success, setSuccess] = useState(null);
  const [uploadProgress, setUploadProgress] = useState(0); // Postęp całkowity
  const [totalFiles, setTotalFiles] = useState(0); // Liczba plików do przesłania
  const [uploadedFiles, setUploadedFiles] = useState(0); // Liczba przesłanych plików

  const handleFolderChange = (event) => {
    const files = Array.from(event.target.files);
    const mp3Files = files.filter(file => file.type === 'audio/mp3' || file.name.endsWith('.mp3'));

    if (mp3Files.length === 0) {
      setError("Folder nie zawiera plików MP3.");
      return;
    }

    setAudioFiles(mp3Files);
    setTotalFiles(mp3Files.length); // Ustawienie liczby plików do przesłania
    setUploadedFiles(0); // Resetowanie liczby przesłanych plików
    setUploadProgress(0); // Resetowanie postępu

    // Uruchamiamy pasek postępu po załadowaniu folderu
    setUploadProgress(0); // Ustawiamy początkowy postęp na 0%
    startUpload(mp3Files); // Rozpoczynamy przesyłanie plików
  };

  const startUpload = async (files) => {
    let progressPerFile = 100 / files.length; // Ilość postępu dla każdego pliku

    setUploadProgress(0); // Resetowanie paska postępu
    setError(null); 
    setSuccess(null);

    try {
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        await catalogApi.uploadAudio(user, file, (progressEvent) => {
          const fileProgress = Math.round((progressEvent.loaded * 100) / progressEvent.total); // Obliczamy postęp dla pojedynczego pliku
          const totalFileProgress = (i + 1) * progressPerFile; // Obliczamy postęp dla wszystkich plików

          setUploadProgress(Math.round(totalFileProgress)); // Aktualizujemy pasek postępu
        });

        setUploadedFiles((prev) => prev + 1); // Zwiększamy liczbę przesłanych plików
      }

      setSuccess("Wszystkie pliki audio MP3 zostały pomyślnie przesłane!");
    } catch (error) {
      setError("Wystąpił błąd podczas przesyłania plików: " + error.message);
      console.error(error);
    }
  };

  return (
    <div>
      <h2>Prześlij Pliki MP3 z Folderu</h2>
      <div>
        <label htmlFor="audioFolder">Wybierz folder z plikami MP3:</label>
        <input
          type="file"
          id="audioFolder"
          webkitdirectory="true" // Umożliwia wybór całego folderu
          directory=""
          multiple
          onChange={handleFolderChange}
        />
      </div>

      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}

      {/* Pasek postępu */}
      {uploadProgress > 0 && (
        <div>
          <p>Postęp przesyłania: {uploadProgress}% ({uploadedFiles} z {totalFiles} plików)</p>
          <progress value={uploadProgress} max="100"></progress>
        </div>
      )}
    </div>
  );
};

export default UploadAudioPage;
