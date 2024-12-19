# macOSへのDockerのインストール方法

この手順ではmacOSにDocker環境を構築する方法を説明します。

> [!IMPORTANT]
> このプロジェクトではDocker Desktopの使用は非推奨としています。
> Docker Desktopを使用する場合は各自の責任で行なってください。

## 前提条件

この手順を実行する前に以下の条件を満たしていることを確認してください。

- macOS
- homebrewがインストール済

## 検証環境

この手順は以下の環境で検証しています。

- MacBook Air (15-inch, M2, 2023) macOS Sequoia 15.0 (arm64)

## 手順

### 1. Dockerのインストール

以下のコマンドを実行してDockerをインストールします。

```bash
brew install docker docker-compose docker-buildx
```

### 2. Colimaのインストール

以下のコマンドを実行してColimaをインストールします。

```bash
brew install colima
```

### 3. Colimaの起動

以下のコマンドを実行してColimaを起動します。

```bash
colima start
```

### 4. Buildx関連ファイルの権限変更

以下のコマンドを実行してBuildx関連ファイルの権限を変更します。

```bash
sudo chown -R $(id -u):$(id -g) ~/.docker/buildx
```

### 5. Dockerの動作確認

以下のコマンドを実行してDockerが正常に動作していることを確認します。

```bash
docker info
```

出力例は以下のようになります

```bash
Client: Docker Engine - Community
 Version:    27.3.1
 Context:    colima
 Debug Mode: false
 Plugins:
  buildx: Docker Buildx (Docker Inc.)
    Version:  v0.17.1
    Path:     /Users/cffnpwr/.docker/cli-plugins/docker-buildx
  compose: Docker Compose (Docker Inc.)
    Version:  2.29.7
    Path:     /Users/cffnpwr/.docker/cli-plugins/docker-compose

Server:
 Containers: 0
  Running: 0
  Paused: 0
  Stopped: 0
 Images: 0
 Server Version: 26.1.1
 Storage Driver: overlay2
  Backing Filesystem: extfs
  Supports d_type: true
  Using metacopy: false
  Native Overlay Diff: true
  userxattr: false
 Logging Driver: json-file
 Cgroup Driver: cgroupfs
 Cgroup Version: 2
 Plugins:
  Volume: local
  Network: bridge host ipvlan macvlan null overlay
  Log: awslogs fluentd gcplogs gelf journald json-file local splunk syslog
 Swarm: inactive
 Runtimes: io.containerd.runc.v2 runc
 Default Runtime: runc
 Init Binary: docker-init
 containerd version: e377cd56a71523140ca6ae87e30244719194a521
 runc version: v1.1.12-0-g51d5e94
 init version: de40ad0
 Security Options:
  apparmor
  seccomp
   Profile: builtin
  cgroupns
 Kernel Version: 6.8.0-31-generic
 Operating System: Ubuntu 24.04 LTS
 OSType: linux
 Architecture: aarch64
 CPUs: 2
 Total Memory: 1.91GiB
 Name: colima
 ID: 43803d4a-2e50-4133-a5f5-1895e5727e8f
 Docker Root Dir: /var/lib/docker
 Debug Mode: false
 Username: cffnpwr
 Experimental: false
 Insecure Registries:
  127.0.0.0/8
 Live Restore Enabled: false
```
