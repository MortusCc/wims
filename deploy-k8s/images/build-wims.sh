#!/bin/bash
# wims 微服务批量构建并推送镜像脚本（在 /tmp/imgs/ 目录下运行）
# 用法: ./build-wims.sh [服务名列表]
# 不传参数则构建全部 9 个服务,也可只传部分,如: ./build-wims.sh product-service gateway-service
registry=crpi-xltuiqx28nz2hmg1.cn-hangzhou.personal.cr.aliyuncs.com
namespace=my-wims
version=v1.1

service_list="eureka-service gateway-service product-service product-client inbound-service outbound-service inventory-service alert-service statistics-service"
service_list=${1:-${service_list}}

for service in ${service_list}; do
  image_name="${registry}/${namespace}/${service}:${version}"
  echo "=================== 构建 ${image_name} ==================="
  # -f 指定该服务的 Dockerfile;构建上下文为当前目录(内含 jar 与 Dockerfile)
  nerdctl build -f Dockerfile-${service} -t ${image_name} .
  if [ $? -ne 0 ]; then
    echo "!!! 构建失败: ${service}"; exit 1
  fi
  nerdctl push ${image_name}
  if [ $? -ne 0 ]; then
    echo "!!! 推送失败: ${service}"; exit 1
  fi
  echo "√ 完成: ${image_name}"
done

echo "=================== 全部完成 ==================="
nerdctl images | grep "${namespace}" | head -20
