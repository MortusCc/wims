// WIMS 微服务项目 Jenkins 流水线(草稿,结构参考指导书 6.5.4.3 示例)
// 使用前请在 Jenkins「凭据管理」中创建三个凭据,ID 必须与本文件一致:
//   github-ssh                 SSH 用户名+私钥(类型: SSH Username with private key)
//   aliyun-registry-credentials 阿里云个人版仓库用户名/密码(类型: Username with password)
//   k8s-cert-pfx               PKCS#12 证书(类型: Certificate)
// 说明:version 直接使用 BUILD_NUMBER,镜像 tag 每次构建都唯一,
//       避免集群 IfNotPresent 拉取策略命中旧镜像的缓存坑。
pipeline {
    agent any
    parameters {
        booleanParam(name: 'Checkout', defaultValue: true, description: '拉取代码')
        booleanParam(name: 'Build', defaultValue: true, description: '生成jar')
        booleanParam(name: 'BuildImages', defaultValue: true, description: '生成镜像')
        booleanParam(name: 'BuildWeb', defaultValue: true, description: '生成前端镜像(第7章选做)')
        booleanParam(name: 'Pushimage', defaultValue: true, description: '推送镜像')
        booleanParam(name: 'Deploy', defaultValue: true, description: '部署服务')
    }
    environment {
        docker_registry = 'crpi-xltuiqx28nz2hmg1.cn-hangzhou.personal.cr.aliyuncs.com'
        repository = 'my-wims'
        version = "${env.BUILD_NUMBER}"
        // 9 个微服务;调试阶段可临时只留前几个(如 eureka-service gateway-service product-service product-client)
        service_list = 'eureka-service gateway-service product-client product-service inbound-service outbound-service inventory-service alert-service statistics-service'
    }
    stages {
        stage('Checkout') {
            when {
                expression { params.Checkout == true }
            }
            steps {
                git credentialsId: 'github-ssh',
                    url: 'git@github.com:MortusCc/wims.git'
            }
        }
        stage('Build') {
            when {
                expression { params.Build == true }
            }
            steps {
                // 与本地构建命令保持一致(跳过测试,注入 p.env=k8s 激活 application-k8s 配置)
                sh 'mvn clean package -DskipTests -Dp.env=k8s'
            }
        }
        stage('BuildImages') {
            when {
                expression { params.BuildImages == true }
            }
            steps {
                script {
                    def work_dir = pwd()
                    for (service in service_list.split()) {
                        dir("$work_dir/$service") {
                            def image_name = "${docker_registry}/${repository}/${service}:${version}"
                            echo "${image_name}"
                            sh "nerdctl build -f Dockerfile -t ${image_name} ."
                        }
                    }
                }
            }
        }
        stage("BuildWeb") {
            when {
                expression { params.BuildWeb == true }
            }
            steps {
                script {
                    def work_dir = pwd()
                    dir("$work_dir/wims-web") {
                        def image_name = "${docker_registry}/${repository}/web:${version}"
                        echo "${image_name}"
                        sh "nerdctl build -f Dockerfile -t ${image_name} ."
                    }
                }
            }
        }
        stage("Pushimage") {
            when {
                expression { params.Pushimage == true }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'aliyun-registry-credentials',
                        passwordVariable: 'ALIYUN_PASSWORD',
                        usernameVariable: 'ALIYUN_USERNAME')]) {
                    // 单引号字符串:敏感变量由 shell 从作业环境变量展开,不进入 Groovy 字符串(消除 Jenkins 敏感变量插值告警)
                    sh 'nerdctl login --username="$ALIYUN_USERNAME" --password="$ALIYUN_PASSWORD" "$docker_registry"'
                    script {
                        for (service in service_list.split()) {
                            sh "nerdctl push ${docker_registry}/${repository}/${service}:${version}"
                        }
                        // 第 7 章选做:前端镜像一并推送
                        sh "nerdctl push ${docker_registry}/${repository}/web:${version}"
                    }
                }
            }
        }
        stage("Deploy") {
            when {
                expression { params.Deploy == true }
            }
            steps {
                script {
                    withCredentials([certificate(aliasVariable: '', credentialsId: 'k8s-cert-pfx', keystoreVariable: 'CERTIFICATE_KEYSTORE', passwordVariable: 'CERTIFICATE_PASSWORD')]) {
                        def work_dir = pwd()
                        dir("$work_dir/deploy-k8s") {
                            // 将 yaml 中的镜像版本号替换为本轮构建版本(新 tag 避免缓存坑)
                            sh "sed -i 's|:v1\\.1|:${version}|g' *.yaml"
                            sh 'kubectl apply -f .'
                            script {
                                // 从集群动态获取 Deployment 列表再逐个等待滚动完成,
                                // 不能硬编码名字:集群中 Eureka 的 Deployment 名为 eureka-deployment
                                def deps = sh(returnStdout: true, script: 'kubectl get deployment -n stockmgr -o name').trim()
                                for (dep in deps.split('\n')) {
                                    def depName = dep.tokenize('/')[1]
                                    // 超时放宽到 300s:节点首次拉取新 tag 镜像 + Pod 启动排队可能超过 120s(踩坑见 Build #10)
                                    sh "kubectl rollout status deployment/${depName} -n stockmgr --timeout=300s"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
