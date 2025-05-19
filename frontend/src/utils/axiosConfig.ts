
import axios from 'axios';

export const baseUrl = "localhost:8080";

const axiosInstance = axios.create({
    baseURL: `${baseUrl}`,
    timeout: 5000,
    headers: {
        'Content-Type':'application/json'
    }
}
)

axiosInstance.interceptors.request.use((config)=> {
    const token = localStorage.getItem('token');
    if (token)
        config.headers.Authorization = `Bearer ${token}`;
    return config;
})

axiosInstance.interceptors.response.use((res) => res, (err) => {
   return Promise.reject(err);
})

export default axiosInstance;
