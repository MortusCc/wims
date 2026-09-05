// 后端接口封装 —— 统一 axios 实例,自动携带网关鉴权参数 token=1
// 网关 AuthFilter 要求所有请求带 token=1 才放行(否则 401),后端路径均为相对路径:
// dev 走 vite 代理,生产走 nginx 反代,同源无跨域
import axios from 'axios'

const api = axios.create({ timeout: 10000 })

// 请求拦截:统一注入 token 参数(网关全局鉴权,见 gateway-service/filter/AuthFilter)
api.interceptors.request.use((config) => {
  config.params = { ...(config.params || {}), token: '1' }
  return config
})

// ============ 商品(product-client,经网关 /product/**) ============
export const queryAllProduct = () => api.get('/product/queryAllProduct')
export const findByProductId = (id) => api.get(`/product/findByProductId/${id}`)

// ============ 库存(inventory-service,经网关 /stock/**) ============
export const getStockList = () => api.get('/stock/list')
export const getStock = (productId) => api.get(`/stock/${productId}`)
// 库存增减:{"productId":1,"quantity":20}
export const increaseStock = (data) => api.post('/stock/increase', data)
export const decreaseStock = (data) => api.post('/stock/decrease', data)

// ============ 入库(inbound-service,经网关 /inbound/**) ============
export const getInboundList = () => api.get('/inbound/list')
// 新建入库单:{"productId":1,"quantity":100,"operator":"张伟"}
export const createInbound = (data) => api.post('/inbound', data)

// ============ 出库(outbound-service,经网关 /outbound/**) ============
export const getOutboundList = () => api.get('/outbound/list')
// 新建出库单:{"productId":3,"quantity":50,"operator":"王强"}
export const createOutbound = (data) => api.post('/outbound', data)

// ============ 预警(alert-service,经网关 /alert/**) ============
export const getAlertList = () => api.get('/alert/list')
export const checkAlert = () => api.get('/alert/check')
export const dealAlert = (id) => api.get(`/alert/deal/${id}`)

// ============ 统计(statistics-service,经网关 /statistics/**) ============
export const showTodayStatistics = () => api.get('/statistics/showToday')
export const genDailyReport = () => api.get('/statistics/daily')

// 统一错误信息提取
export const errMsg = (e) => (e?.response?.data?.message || e?.message || '请求失败')
