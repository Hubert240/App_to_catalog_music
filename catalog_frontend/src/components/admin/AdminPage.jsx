import React, { useEffect, useState } from 'react'
import { Navigate } from 'react-router-dom'
import { Container } from 'semantic-ui-react'
import { useAuth } from '../../services/AuthContext'
import { catalogApi } from '../../services/CatalogApi'
import { handleLogError } from '../../services/Helpers'

function AdminPage() {
  const Auth = useAuth()
  const user = Auth.getUser()
  const isAdmin = user.role === 'ADMIN'

  const [users, setUsers] = useState([])
  const [userUsernameSearch, setUserUsernameSearch] = useState('')
  const [isUsersLoading, setIsUsersLoading] = useState(false)

  useEffect(() => {
    handleGetUsers()
  }, [])

  const handleGetUsers = async () => {
    try {
      setIsUsersLoading(true)
      const response = await catalogApi.getUsers(user)
      const users = response.data
      setUsers(users)
    } catch (error) {
      handleLogError(error)
    } finally {
      setIsUsersLoading(false)
    }
  }

  const handleDeleteUser = async (username) => {
    try {
      await catalogApi.deleteUser(user, username)
      await handleGetUsers()
    } catch (error) {
      handleLogError(error)
    }
  }

  const handleSearchUser = async () => {
    try {
      const response = await catalogApi.getUsers(user, userUsernameSearch)
      const data = response.data
      const users = data instanceof Array ? data : [data]
      setUsers(users)
    } catch (error) {
      handleLogError(error)
      setUsers([])
    }
  }

  if (!isAdmin) {
    return <Navigate to='/' />
  }

  return (
    <Container>
      <h1>Panel Administratora</h1>
      <div>
        <input
          type="text"
          value={userUsernameSearch}
          onChange={(e) => setUserUsernameSearch(e.target.value)}
          placeholder="Wyszukaj użytkownika"
        />
        <button onClick={handleSearchUser}>Szukaj</button>
      </div>
      {isUsersLoading ? (
        <p>Ładowanie użytkowników...</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Nazwa</th>
              <th>Akcje</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.username}>
                <td>{user.username}</td>
                <td>
                  <button onClick={() => handleDeleteUser(user.username)}>Usuń</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </Container>
  )
}

export default AdminPage;
