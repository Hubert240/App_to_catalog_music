const prod = {
  url: {
    API_BASE_URL: 'test',
  }
}

const dev = {
  url: {
    API_BASE_URL: 'http://localhost:8090'
  }
}

export const config = process.env.NODE_ENV === 'development' ? dev : prod