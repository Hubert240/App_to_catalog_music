import React, { useState } from 'react';
import { useAuth } from '../../services/AuthContext';
import { catalogApi } from '../../services/CatalogApi';
import { handleLogError } from '../../services/Helpers';
import styles from './AddAudioPage.module.css';

function AddAudioPage() {
  const Auth = useAuth();
  const user = Auth.getUser();
  const genres = ["Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "Alternative Rock",
     "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", 
     "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad",
      "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "Acapella", "Euro-House", "Dance Hall"];


  const [audio, setAudio] = useState({
    artist: '',
    title: '',
    track: '',
    album: '',
    year: '',
    genreDescription: '',
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
  const [audioFileName, setAudioFileName] = useState("");
  const [coverArtFileName, setCoverArtFileName] = useState("");

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    if (name === 'year' && value && !/^\d*$/.test(value)) {
      setErrorMessage('Rok może zawierać tylko cyfry.');
      return;
    }
    setErrorMessage('');
    setAudio({
      ...audio,
      [name]: value,
    });
  };


  
  
  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    setFile(selectedFile);
    setAudioFileName(selectedFile ? selectedFile.name : "");
  };

  const handleCoverArtChange = (e) => {
    const selectedCover = e.target.files[0];
    setCoverArt(selectedCover);
    setCoverArtFileName(selectedCover ? selectedCover.name : "");
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
    formData.append('genreDescription', audio.genreDescription);
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
        year: null,
        genreDescription: '',
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
    <div>
      <label htmlFor="title" className={styles.label}>Tytuł</label>
        <input
          type="text"
          id="title"
          name="title"
          placeholder="Tytuł"
          value={audio.title}
          onChange={handleInputChange}
          className={styles.inputAddPage}
          required
        />
      </div>
      <div>
        <label htmlFor="artist" className={styles.label}>Artysta</label>
        <input
          type="text"
          id="artist"
          name="artist"
          placeholder="Artysta"
          value={audio.artist}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
  
        <div>
        <label htmlFor="track" className={styles.label}>Numer utworu</label>
        <input
          type="text"
          id="track"
          name="track"
          placeholder="Numer utworu"
          value={audio.track}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="album" className={styles.label}>Album</label>
        <input
          type="text"
          id="album"
          name="album"
          placeholder="Album"
          value={audio.album}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="year" className={styles.label}>Rok</label>
        <input
          type="text"
          id="year"
          name="year"
          placeholder="Rok"
          value={audio.year}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="genreDescription" className={styles.label}>Gatunek</label>
        <select
          id="genreDescription"
          name="genreDescription"
          value={audio.genreDescription}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        >
          <option value="">Wybierz gatunek</option>
          {genres.map(genre => (
            <option key={genre} value={genre}>
              {genre}
            </option>
          ))}
        </select>

        </div>
        <div>
        <label htmlFor="comment" className={styles.label}>Komentarz</label>
        <input
          type="text"
          id="comment"
          name="comment"
          placeholder="Komentarz"
          value={audio.comment}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="lyrics" className={styles.label}>Tekst</label>
        <input
          type="text"
          id="lyrics"
          name="lyrics"
          placeholder="Tekst"
          value={audio.lyrics}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="composer" className={styles.label}>Kompozytor</label>
        <input
          type="text"
          id="composer"
          name="composer"
          placeholder="Kompozytor"
          value={audio.composer}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="publisher" className={styles.label}>Wydawca</label>
        <input
          type="text"
          id="publisher"
          name="publisher"
          placeholder="Wydawca"
          value={audio.publisher}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="originalArtist" className={styles.label}>Artysta oryginalny</label>
        <input
          type="text"
          id="originalArtist"
          name="originalArtist"
          placeholder="Artysta oryginalny"
          value={audio.originalArtist}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="albumArtist" className={styles.label}>Artysta albumu</label>
        <input
          type="text"
          id="albumArtist"
          name="albumArtist"
          placeholder="Artysta albumu"
          value={audio.albumArtist}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="copyright" className={styles.label}>Prawa autorskie</label>
        <input
          type="text"
          id="copyright"
          name="copyright"
          placeholder="Prawa autorskie"
          value={audio.copyright}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="url" className={styles.label}>URL</label>
        <input
          type="text"
          id="url"
          name="url"
          placeholder="URL"
          value={audio.url}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label htmlFor="encoder" className={styles.label}>Format kodowania</label>
        <input
          type="text"
          id="encoder"
          name="encoder"
          placeholder="Format kodowania"
          value={audio.encoder}
          onChange={handleInputChange}
          className={styles.inputAddPage}
        />
        </div>
        <div>
        <label></label>
        </div>
        <div>
        <label htmlFor="audioFile" className={styles.label}>Plik audio</label>
        <label htmlFor="audioFile" className={styles.customButton}>Wybierz plik audio</label>
        <input
          type="file"
          id="audioFile"
          accept="audio/*"
          onChange={handleFileChange}
          className={styles.fileInput}
          required
        />
        {audioFileName && <p className={styles.fileName}>{audioFileName}</p>}
        </div>
        
        <div>
        <label htmlFor="coverArt" className={styles.label}>Okładka</label>
        <label htmlFor="coverArt" className={styles.customButton}>Wybierz okładkę</label>
        <input
          type="file"
          id="coverArt"
          accept="image/*"
          onChange={handleCoverArtChange}
          className={styles.fileInput}
        />
         {coverArtFileName && <p className={styles.fileName}>{coverArtFileName}</p>}
        </div>
        <div className={styles.centerDiv}>
        <button type="button" onClick={handleAddAudio} className={styles.addButton}>Dodaj</button>
        </div>
      </form>

      {isSuccess && <p className={styles.successMessage}>Poprawnie dodano nowy plik</p>}
      {errorMessage && <p className={styles.errorMessage}>{errorMessage}</p>}
    </div>
  );
}

export default AddAudioPage;
