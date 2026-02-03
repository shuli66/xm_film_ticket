# Docker 和 Docker Compose 安装指南

## 📋 系统要求

- Ubuntu 20.04+ 或 Debian 11+
- 64 位系统
- 至少 2GB RAM
- 至少 10GB 可用磁盘空间

## 🐳 安装 Docker

### 方法 1: 使用官方脚本（推荐）

```bash
# 下载并运行 Docker 官方安装脚本
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# 将当前用户添加到 docker 组（避免每次都用 sudo）
sudo usermod -aG docker $USER

# 重新登录或运行以下命令使组权限生效
newgrp docker

# 启动 Docker 服务
sudo systemctl start docker
sudo systemctl enable docker

# 验证安装
docker --version
docker run hello-world
```

### 方法 2: 使用 APT 安装（Ubuntu/Debian）

```bash
# 更新包索引
sudo apt-get update

# 安装依赖
sudo apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# 添加 Docker 官方 GPG 密钥
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# 设置 Docker 仓库
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# 更新包索引
sudo apt-get update

# 安装 Docker Engine
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# 将当前用户添加到 docker 组
sudo usermod -aG docker $USER
newgrp docker

# 验证安装
docker --version
```

## 📦 安装 Docker Compose

Docker Compose 有两个版本：

### Docker Compose V2（推荐 - 作为 Docker 插件）

如果您使用方法 2 安装 Docker，Docker Compose V2 已经自动安装了。

验证安装：

```bash
docker compose version
```

如果没有安装，手动安装：

```bash
# 安装 Docker Compose 插件
sudo apt-get update
sudo apt-get install -y docker-compose-plugin

# 验证
docker compose version
```

### Docker Compose V1（独立版本）

如果您需要使用旧版本的 `docker-compose` 命令：

```bash
# 下载 Docker Compose 二进制文件
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# 添加执行权限
sudo chmod +x /usr/local/bin/docker-compose

# 创建符号链接（可选）
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# 验证安装
docker-compose --version
```

## 🚀 一键安装脚本

创建并运行以下脚本：

```bash
#!/bin/bash

echo "========================================="
echo "  安装 Docker 和 Docker Compose"
echo "========================================="
echo ""

# 检查是否为 root 用户
if [ "$EUID" -ne 0 ]; then
    echo "请使用 sudo 运行此脚本"
    exit 1
fi

# 安装 Docker
echo "安装 Docker..."
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
rm get-docker.sh

# 安装 Docker Compose 插件
echo "安装 Docker Compose..."
apt-get update
apt-get install -y docker-compose-plugin

# 将当前用户添加到 docker 组
if [ -n "$SUDO_USER" ]; then
    usermod -aG docker $SUDO_USER
    echo "已将用户 $SUDO_USER 添加到 docker 组"
fi

# 启动 Docker 服务
systemctl start docker
systemctl enable docker

echo ""
echo "========================================="
echo "  安装完成!"
echo "========================================="
echo ""
echo "Docker 版本:"
docker --version
echo ""
echo "Docker Compose 版本:"
docker compose version
echo ""
echo "⚠️  重要: 请重新登录以使 docker 组权限生效"
echo "或运行: newgrp docker"
```

保存为 `install-docker.sh`，然后运行：

```bash
chmod +x install-docker.sh
sudo ./install-docker.sh
```

## ✅ 验证安装

```bash
# 检查 Docker 版本
docker --version

# 检查 Docker Compose 版本（V2）
docker compose version

# 或检查 Docker Compose V1
docker-compose --version

# 测试 Docker
docker run hello-world

# 检查 Docker 服务状态
sudo systemctl status docker
```

## 🔧 常见问题

### 问题 1: 权限被拒绝

**症状**: `permission denied while trying to connect to the Docker daemon socket`

**解决方案**:

```bash
# 将用户添加到 docker 组
sudo usermod -aG docker $USER

# 重新登录或运行
newgrp docker

# 或者重启系统
sudo reboot
```

### 问题 2: Docker 服务未启动

**症状**: `Cannot connect to the Docker daemon`

**解决方案**:

```bash
# 启动 Docker 服务
sudo systemctl start docker

# 设置开机自启
sudo systemctl enable docker

# 检查状态
sudo systemctl status docker
```

### 问题 3: docker-compose 命令不存在

**症状**: `docker-compose: command not found`

**解决方案**:

```bash
# 尝试使用 Docker Compose V2
docker compose version

# 如果 V2 可用，项目脚本会自动使用它
# 或者安装 V1 版本（见上文）
```

## 📝 安装后配置

### 配置 Docker 镜像加速（可选，中国大陆用户推荐）

```bash
# 创建或编辑 Docker 配置文件
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "https://hub-mirror.c.163.com"
  ]
}
EOF

# 重启 Docker 服务
sudo systemctl daemon-reload
sudo systemctl restart docker
```

### 配置 Docker 日志限制（推荐）

```bash
# 编辑 Docker 配置文件
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
  }
}
EOF

# 重启 Docker 服务
sudo systemctl daemon-reload
sudo systemctl restart docker
```

## 🎯 下一步

安装完成后，返回项目目录继续部署：

```bash
cd /path/to/xm_film_ticket

# 修复脚本行尾符
dos2unix *.sh
# 或使用: sed -i 's/\r$//' *.sh

# 添加执行权限
chmod +x *.sh

# 部署应用
./deploy.sh
```

---

**安装完成后，您就可以开始部署项目了！**
