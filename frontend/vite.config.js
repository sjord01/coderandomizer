import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
    plugins: [react()],
    server: {
        port: 5173,
        proxy: {
            // When running in Docker Compose the frontend container must talk to the backend service by its service name.
            // Use 'http://backend:8080' so the proxy resolves to the backend container on the compose network.
            '/api': {
                target: 'http://backend:8080',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, '/api')
            }
        }
    }
})