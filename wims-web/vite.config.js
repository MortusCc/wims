import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// Vite 配置 —— 开发场景通过代理访问集群网关,避免前端跨域问题
// 生产环境:nginx 容器内同样做反向代理(见 nginx.conf),页面与接口同源
export default defineConfig({
  plugins: [vue()],
  server: {
    host: '0.0.0.0',
    port: 5173,
    // 本地开发:把业务接口代理到集群网关(NodePort 30099)
    proxy: {
      '/product': { target: 'http://192.168.79.133:30099', changeOrigin: true },
      '/stock': { target: 'http://192.168.79.133:30099', changeOrigin: true },
      '/inbound': { target: 'http://192.168.79.133:30099', changeOrigin: true },
      '/outbound': { target: 'http://192.168.79.133:30099', changeOrigin: true },
      '/alert': { target: 'http://192.168.79.133:30099', changeOrigin: true },
      '/statistics': { target: 'http://192.168.79.133:30099', changeOrigin: true }
    }
  },
  build: {
    outDir: 'dist',
    // 静态资源带 hash,避免浏览器缓存
    assetsDir: 'static'
  }
})
