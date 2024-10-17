import React, { useState } from 'react';
import { useAuth } from '../../services/AuthContext';
import { catalogApi } from '../../services/CatalogApi';
import { handleLogError } from '../../services/Helpers';
import styles from './AddAudioPage.module.css';

function AddAudioPage() {
  const Auth = useAuth();
  const user = Auth.getUser();

  const [audio, setAudio] = useState({
    artist: '',
    title: '',
    track: '',
    album: '',
    year: '',
    genre: '',
    comment: '',
    lyrics: '',
    composer: '',
    publisher: '',
    originalArtist: '',
    albumArtist: '',
    copyright: '',
    url: '',
    encoder: '',
  });

  const [file, setFile] = useState(null);
  const [coverArt, setCoverArt] = useState(null);
  const [isSuccess, setIsSuccess] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setAudio({
      ...audio,
      [name]: value,
    });
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleCoverArtChange = (e) => {
    setCoverArt(e.target.files[0]);
  };

  const handleAddAudio = async () => {
    if (!audio.title || !file) {
      setErrorMessage('Tytuł i plik audio są wymagane.');
      return;
    }

    const formData = new FormData();
    formData.append('artist', audio.artist);
    formData.append('title', audio.title);
    formData.append('audioFile', file);
    if (coverArt) {
      formData.append('coverArt', coverArt);
  }
    formData.append('userId', user.id);
    formData.append('track', audio.track);
    formData.append('album', audio.album);
    formData.append('year', audio.year);
    formData.append('genre', audio.genre);
    formData.append('comment', audio.comment);
    formData.append('lyrics', audio.lyrics);
    formData.append('composer', audio.composer);
    formData.append('publisher', audio.publisher);
    formData.append('originalArtist', audio.originalArtist);
    formData.append('albumArtist', audio.albumArtist);
    formData.append('copyright', audio.copyright);
    formData.append('url', audio.url);
    formData.append('encoder', audio.encoder);

    try {
      setIsSuccess(false);
      setErrorMessage('');
      await catalogApi.addAudio(user, formData);
      setIsSuccess(true);
      setAudio({
        artist: '',
        title: '',
        track: '',
        album: '',
        year: '',
        genre: '',
        comment: '',
        lyrics: '',
        composer: '',
        publisher: '',
        originalArtist: '',
        albumArtist: '',
        copyright: '',
        url: '',
        encoder: '',
      });
      setFile(null);
      setCoverArt(null);
    } catch (error) {
      handleLogError(error);
      setErrorMessage('Spróbuj ponownie.');
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Dodaj nowy plik</h2>
      <form className={styles.form}>
        <label htmlFor="artist" className={styles.label}>Artysta</label>
        <input
          type="text"
          id="artist"
          name="artist"
          placeholder="Artysta"
          value={audio.artist}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="title" className={styles.label}>Tytuł</label>
        <input
          type="text"
          id="title"
          name="title"
          placeholder="Tytuł"
          value={audio.title}
          onChange={handleInputChange}
          className={styles.textInput}
          required
        />

        <label htmlFor="track" className={styles.label}>Numer utworu</label>
        <input
          type="text"
          id="track"
          name="track"
          placeholder="Numer utworu"
          value={audio.track}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="album" className={styles.label}>Album</label>
        <input
          type="text"
          id="album"
          name="album"
          placeholder="Album"
          value={audio.album}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="year" className={styles.label}>Rok</label>
        <input
          type="text"
          id="year"
          name="year"
          placeholder="Rok"
          value={audio.year}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="genre" className={styles.label}>Gatunek</label>
        <input
          type="text"
          id="genre"
          name="genre"
          placeholder="Gatunek"
          value={audio.genre}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="comment" className={styles.label}>Komentarz</label>
        <input
          type="text"
          id="comment"
          name="comment"
          placeholder="Komentarz"
          value={audio.comment}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="lyrics" className={styles.label}>Tekst</label>
        <input
          type="text"
          id="lyrics"
          name="lyrics"
          placeholder="Tekst"
          value={audio.lyrics}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="composer" className={styles.label}>Kompozytor</label>
        <input
          type="text"
          id="composer"
          name="composer"
          placeholder="Kompozytor"
          value={audio.composer}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="publisher" className={styles.label}>Wydawca</label>
        <input
          type="text"
          id="publisher"
          name="publisher"
          placeholder="Wydawca"
          value={audio.publisher}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="originalArtist" className={styles.label}>Artysta oryginalny</label>
        <input
          type="text"
          id="originalArtist"
          name="originalArtist"
          placeholder="Artysta oryginalny"
          value={audio.originalArtist}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="albumArtist" className={styles.label}>Artysta albumu</label>
        <input
          type="text"
          id="albumArtist"
          name="albumArtist"
          placeholder="Artysta albumu"
          value={audio.albumArtist}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="copyright" className={styles.label}>Prawa autorskie</label>
        <input
          type="text"
          id="copyright"
          name="copyright"
          placeholder="Prawa autorskie"
          value={audio.copyright}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="url" className={styles.label}>URL</label>
        <input
          type="text"
          id="url"
          name="url"
          placeholder="URL"
          value={audio.url}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="encoder" className={styles.label}>Kodera</label>
        <input
          type="text"
          id="encoder"
          name="encoder"
          placeholder="Kodera"
          value={audio.encoder}
          onChange={handleInputChange}
          className={styles.textInput}
        />

        <label htmlFor="audioFile" className={styles.label}>Plik audio</label>
        <input
          type="file"
          id="audioFile"
          accept="audio/*"
          onChange={handleFileChange}
          className={styles.fileInput}
          required
        />

        <label htmlFor="coverArt" className={styles.label}>Okładka</label>
        <input
          type="file"
          id="coverArt"
          accept="image/*"
          onChange={handleCoverArtChange}
          className={styles.fileInput}
        />

        <button type="button" onClick={handleAddAudio} className={styles.button}>Dodaj</button>
      </form>

      {isSuccess && <p className={styles.successMessage}>Poprawnie dodano nowy plik</p>}
      {errorMessage && <p className={styles.errorMessage}>{errorMessage}</p>}
    </div>
  );
}

export default AddAudioPage;
