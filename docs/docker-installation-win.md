# WindowsへのDockerのインストール方法

この手順ではWindowsにDocker環境を構築する方法を説明します。

> [!IMPORTANT]
> このプロジェクトではDocker Desktopの使用は非推奨としています。
> Docker Desktopを使用する場合は各自の責任で行なってください。

## 前提条件

この手順を実行する前に以下の条件を満たしていることを確認してください。

- Windows 10 Home/Pro
- Windows 11 Home/Pro

## 検証環境

この手順は以下の環境で検証しています。

- Windows 11 Home (x86_64)

## 手順

### 1. Windows Subsystem for Linux 2 (WSL 2)の有効化

Windows Subsystem for Linux 2 (WSL 2)を有効化します。
管理者権限でPowerShellを起動し、以下のコマンドを実行してください。

```powershell
dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
```

コマンドの実行後、システムを再起動してください。

### 2. WSL 2のインストール

WSL 2をインストールします。
管理者権限でPowerShellを起動し、以下のコマンドを実行してください。

```powershell
wsl --install
```

システムの再起動を求められるので、再起動してください。

### 3. WSL 2のデフォルトバージョンを設定

管理者権限でPowerShellを起動し、以下のコマンドを実行してください。

```powershell
wsl --set-default-version 2
```

### 4. Ubuntuのセットアップ

WSL2ではデフォルトでUbutnuがインストールされているためセットアップを行ないます。

PowerShellを起動し、以下のコマンドを実行してください。

```powershell
wsl
```

Ubuntuが起動するので表示される指示に従ってユーザー名とパスワードを設定してください。

### 5. Systemdの有効化

PowerShellを起動し、以下のコマンドを実行してください。

```powershell
wsl --update
```

Ubutnuを起動し、以下のコマンドを実行してください。

```bash
cat /etc/wsl.conf
```

このコマンドの実行結果を確認し、`systemd`の設定が`true`になっていればSystemdが有効になっています。
Systemdが有効になっていない場合は、vimやnano等のエディタを使用して下記のように`systemd`の設定を`true`に設定してください。

```ini
[boot]
systemd=true
```

ファイルの編集を行なった場合はPowerShellから以下のコマンドを実行してUbuntuを停止してください。

```powershell
wsl --shutdown Ubuntu
```

### 6. Dockerのインストール

Ubutnuを起動し、以下のコマンドを実行してください。

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

### 7. Dockerの動作確認

Ubutnu上で以下のコマンドを実行し、Dockerの情報が表示されることを確認してください。

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
