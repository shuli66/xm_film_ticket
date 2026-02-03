# 🔧 行尾符问题修复指南

## 问题说明

如果您在 Linux 服务器上执行部署脚本时遇到以下错误：

```
./build.sh: cannot execute: required file not found
./deploy.sh: line XX: ./build.sh: cannot execute: required file not found
```

这是因为脚本文件使用了 Windows 的行尾符（CRLF）而不是 Unix 的行尾符（LF）。

## 🚀 快速修复（在服务器上）

### 方法 1: 使用 dos2unix 工具（推荐）

```bash
# 安装 dos2unix
sudo apt-get update
sudo apt-get install dos2unix

# 转换所有 shell 脚本
dos2unix build.sh deploy.sh stop.sh

# 添加执行权限
chmod +x build.sh deploy.sh stop.sh

# 重新部署
./deploy.sh
```

### 方法 2: 使用 sed 命令

如果无法安装 dos2unix，可以使用 sed：

```bash
# 转换行尾符
sed -i 's/\r$//' build.sh
sed -i 's/\r$//' deploy.sh
sed -i 's/\r$//' stop.sh

# 添加执行权限
chmod +x build.sh deploy.sh stop.sh

# 重新部署
./deploy.sh
```

### 方法 3: 使用 tr 命令

```bash
# 转换行尾符
tr -d '\r' < build.sh > build.sh.tmp && mv build.sh.tmp build.sh
tr -d '\r' < deploy.sh > deploy.sh.tmp && mv deploy.sh.tmp deploy.sh
tr -d '\r' < stop.sh > stop.sh.tmp && mv stop.sh.tmp stop.sh

# 添加执行权限
chmod +x build.sh deploy.sh stop.sh

# 重新部署
./deploy.sh
```

## 🔄 永久解决方案

### 在 Windows 开发环境中

项目中已经添加了 `.gitattributes` 文件，确保 shell 脚本始终使用 LF 行尾符。

**重新规范化 Git 仓库**：

```bash
# 在 Windows 上执行
git add --renormalize .
git commit -m "Normalize line endings"
git push
```

然后在服务器上重新拉取：

```bash
# 在服务器上执行
git pull
chmod +x docker/*.sh
cd docker
./deploy.sh
```

### 配置 Git 全局设置

在 Windows 上配置 Git 自动转换：

```bash
# 在 Windows 上执行
git config --global core.autocrlf input
```

这样 Git 会在提交时自动将 CRLF 转换为 LF。

## 📋 一键修复脚本

创建一个修复脚本 `fix-line-endings.sh`（在服务器上）：

```bash
#!/bin/bash
echo "修复 shell 脚本的行尾符..."

# 检查 dos2unix 是否安装
if command -v dos2unix &> /dev/null; then
    echo "使用 dos2unix 转换..."
    dos2unix build.sh deploy.sh stop.sh 2>/dev/null || true
else
    echo "使用 sed 转换..."
    sed -i 's/\r$//' build.sh 2>/dev/null || true
    sed -i 's/\r$//' deploy.sh 2>/dev/null || true
    sed -i 's/\r$//' stop.sh 2>/dev/null || true
fi

# 添加执行权限
chmod +x build.sh deploy.sh stop.sh

echo "✓ 修复完成!"
echo "现在可以运行: ./deploy.sh"
```

使用方法：

```bash
# 创建修复脚本
cat > fix-line-endings.sh << 'EOF'
#!/bin/bash
echo "修复 shell 脚本的行尾符..."
if command -v dos2unix &> /dev/null; then
    dos2unix build.sh deploy.sh stop.sh 2>/dev/null || true
else
    sed -i 's/\r$//' build.sh deploy.sh stop.sh 2>/dev/null || true
fi
chmod +x build.sh deploy.sh stop.sh
echo "✓ 修复完成!"
EOF

# 转换修复脚本本身
sed -i 's/\r$//' fix-line-endings.sh
chmod +x fix-line-endings.sh

# 运行修复
./fix-line-endings.sh

# 部署
./deploy.sh
```

## ✅ 验证修复

检查文件的行尾符：

```bash
# 查看文件的行尾符类型
file build.sh deploy.sh stop.sh

# 应该显示类似：
# build.sh: Bourne-Again shell script, ASCII text executable
# 如果显示 "CRLF line terminators" 则需要修复
```

## 🎯 推荐流程

**在服务器上执行以下命令**：

```bash
# 1. 进入 docker 目录
cd docker

# 2. 安装 dos2unix（推荐）
sudo apt-get update && sudo apt-get install -y dos2unix

# 3. 转换脚本
dos2unix *.sh

# 4. 添加执行权限
chmod +x *.sh

# 5. 部署
./deploy.sh
```

---

**问题解决后，您就可以正常使用部署脚本了！**
