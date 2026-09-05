# WIMS 前端 —— 多阶段构建:Node 编译 → Nginx 运行
# 阶段 1:安装依赖并构建静态资源(使用国内 npm 镜像加速)
FROM docker.m.daocloud.io/library/node:20-alpine AS build
WORKDIR /app
# 镜像内 npm 源切换到 npmmirror,避免构建超时
# 注意:不能设置 NODE_ENV=production,npm 会据此跳过 devDependencies(vite)导致构建失败
ENV NPM_CONFIG_REGISTRY=https://registry.npmmirror.com
COPY package.json ./
RUN npm install --ignore-scripts
COPY . .
RUN npm run build

# 阶段 2:Nginx 托管静态文件,并把 API 请求转发到集群内网关
FROM docker.m.daocloud.io/library/nginx:1.21-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
