import React, { useEffect, useState } from 'react';
import { useAuth } from '../../services/AuthContext';
import { catalogApi } from '../../services/CatalogApi';
import styles from './UserPage.module.css';

function UserPage() {
  const Auth = useAuth();
  const user = Auth.getUser();
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [audioCount, setAudioCount] = useState(0);

  useEffect(() => {
  
    if (user && !userData) {
      const fetchUserData = async () => {
        try {
          const response = await catalogApi.getUsers(user, user.username);
          setUserData(response.data);
          console.log(response.data)
        } catch (err) {
          console.error('Error fetching user data:', err);
          setError('Nie udało się załadować danych użytkownika.');
        } finally {
          setLoading(false);
        }
      };
      const fetchAudioCount = async () => { try { const response = await catalogApi.numberOfAudio(user,user.id);
         setAudioCount(response.data); console.log(response.data); 
        } catch (err) 
         { console.error('Error fetching audio count:', err); 
        setError('Nie udało się załadować liczby audio.'); } };
         fetchAudioCount();

      fetchUserData();
    }
  }, [user, userData]);

  if (loading) {
    return <p>Ładowanie danych...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  if (!userData) {
    return <p>Brak danych do wyświetlenia.</p>;
  }

  const userInfo = userData[0];
  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Twoje dane</h2>
      <p className={styles.email}>Nazwa: {userInfo.username}</p>
      <p className={styles.email}>Email: {userInfo.email}</p>
      <p className={styles.email}>Liczba  twoich plików audio: {audioCount}</p>
    </div>
  );
}

export default UserPage;
