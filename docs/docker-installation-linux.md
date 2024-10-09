# LinuxへのDockerのインストール方法

この手順ではLinuxにDocker環境を構築する方法を説明します。

> [!IMPORTANT]
> このプロジェクトではDocker Desktopの使用は非推奨としています。
> Docker Desktopを使用する場合は各自の責任で行なってください。

## 前提条件

この手順を実行する前に以下の条件を満たしていることを確認してください。

- Debian系/CentOS系Linuxディストリビューション

## 検証環境

この手順は以下の環境で検証しています。

- Debian GNU/Linux 12 (bookworm) (x86_64)

## 手順

### 1. Dockerのインストール

以下のコマンドを実行してください。

```bash
sudo apt update
sudo apt install -y uidmap iptables
```

以下のコマンドを実行してDockerをインストールしてください。

```bash
curl -fsSL https://get.docker.com/rootless | sh
```

インストールが完了したら以下のコマンドを実行してShellを再起動してください。

```bash
exec $SHELL -l
```

### 2. Dockerの動作確認

以下のコマンドを実行してDockerが正常に動作していることを確認します。

```bash
docker info
```

出力例は以下のようになります

```bash
Client: Docker Engine - Community
 Version:    27.3.1
 Context:    default
 Debug Mode: false
 Plugins:
  buildx: Docker Buildx (Docker Inc.)
    Version:  v0.17.1
    Path:     /usr/libexec/docker/cli-plugins/docker-buildx
  compose: Docker Compose (Docker Inc.)
    Version:  v2.29.7
    Path:     /usr/libexec/docker/cli-plugins/docker-compose

Server:
 Containers: 38
  Running: 21
  Paused: 0
  Stopped: 17
 Images: 194
 Server Version: 27.3.1
 Storage Driver: overlay2
  Backing Filesystem: extfs
  Supports d_type: true
  Using metacopy: false
  Native Overlay Diff: true
  userxattr: false
 Logging Driver: json-file
 Cgroup Driver: systemd
 Cgroup Version: 2
 Plugins:
  Volume: local
  Network: bridge host ipvlan macvlan null overlay
  Log: awslogs fluentd gcplogs gelf journald json-file local splunk syslog
 Swarm: inactive
 Runtimes: runc runsc io.containerd.runc.v2
 Default Runtime: runc
 Init Binary: docker-init
 containerd version: 7f7fdf5fed64eb6a7caf99b3e12efcf9d60e311c
 runc version: v1.1.14-0-g2c9f560
 init version: de40ad0
 Security Options:
  apparmor
  seccomp
   Profile: builtin
  cgroupns
 Kernel Version: 6.1.0-23-amd64
 Operating System: Debian GNU/Linux 12 (bookworm)
 OSType: linux
 Architecture: x86_64
 CPUs: 20
 Total Memory: 62.44GiB
 Name: cpwr-dev0
 ID: c3f4c45b-1c16-45f6-a823-6a26cf6bfbd0
 Docker Root Dir: /var/lib/docker
 Debug Mode: false
 Username: cffnpwr
 Experimental: false
 Insecure Registries:
  127.0.0.0/8
 Live Restore Enabled: false
 Default Address Pools:
   Base: 10.0.0.0/8, Size: 24
```
