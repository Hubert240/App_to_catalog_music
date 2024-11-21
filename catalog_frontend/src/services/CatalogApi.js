import axios from 'axios'
import { config } from '../Constants'

export const catalogApi = {
  authenticate,
  signup,
  numberOfUsers,
  getUsers,
  deleteUser,
  getAudio,
  addAudio,
  deleteAudio,
  numberOfAudio,
  uploadAudio,
  getAudioDetails,
  searchAudioData,
  searchAudioUpload,
}

function authenticate(username, password) {
  return instance.post('/auth/authenticate', { username, password }, {
    headers: { 'Content-type': 'application/json' }
  })
}

function signup(user) {
  return instance.post('/auth/signup', user, {
    headers: { 'Content-type': 'application/json' }
  })
}

function numberOfUsers() {
  return instance.get('/public/numberOfUsers')
}



function getUsers(user, username) {
  const url = username ? `/api/users/${username}` : '/api/users'
  return instance.get(url, {
    headers: { 'Authorization': basicAuth(user) }
  })
}

function deleteUser(user, username) {
  return instance.delete(`/api/users/${username}`, {
    headers: { 'Authorization': basicAuth(user) }
  })
}




function deleteAudio(user, id){
  return instance.delete(`/api/audio/${id}`,{
    headers: {'Authorization': basicAuth(user)}
  })
}

function addAudio(user, audio) {
  return instance.post('/api/audio/add', audio, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': basicAuth(user)
    }
  })
}

function uploadAudio(user, audioFile, onProgress) {
  const formData = new FormData();
  formData.append('audioFile', audioFile);
  formData.append('userId', user.id);

  return instance.post('/api/audio/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': basicAuth(user)
    },
    onUploadProgress: onProgress
  });
}




function getAudio(user, userId, filters) {
  let url = `/api/audio?userId=${userId}`;
  if (filters.title) {
    url += `&title=${filters.title}`;
  }
  if (filters.artist) {
    url += `&artist=${filters.artist}`;
  }
  if (filters.album) {
    url += `&album=${filters.album}`;
  }
  if (filters.year) {
    url += `&year=${filters.year}`;
  }

  return instance.get(url, {
    headers: { 'Authorization': basicAuth(user) },
  });
}



function getAudioDetails(user,id){
  const url = `/api/audio/audio/${id}`
  return instance.get(url,{
    headers:{'Authorization':basicAuth(user)}
  })
}


function searchAudioData(user,title,artist){
  const url = `/music`
  return instance.get(url,{
    headers:{'Authorization':basicAuth(user)
  },
  params:{title:title,artist:artist}});
}

function searchAudioUpload(user, audioFile) {
  const formData = new FormData();
  formData.append('audioFile', audioFile);
  formData.append('userId', user.id);

  return instance.post('/music/uploadFile', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': basicAuth(user)
    }
  });
}






function numberOfAudio() {
  return instance.get('/public/numberOfAudio')
}



const instance = axios.create({
  baseURL: config.url.API_BASE_URL
})


function basicAuth(user) {
  return `Basic ${user.authdata}`
}