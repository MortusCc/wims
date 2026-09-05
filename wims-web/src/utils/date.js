// 日期格式化工具 —— 兼容后端 Jackson 的 ISO 字符串与时间戳两种输出
export function formatDate(value) {
  if (value === null || value === undefined || value === '') return ''
  let d
  if (typeof value === 'number') {
    // 时间戳(毫秒或秒)
    d = new Date(value < 1e12 ? value * 1000 : value)
  } else {
    d = new Date(value)
  }
  if (isNaN(d.getTime())) return String(value)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}
