# 開発環境構築ガイド

このプロジェクトではDevcontianerを用いた開発を推奨しています。
そのため、このガイドではDevcontainerを用いた開発環境構築方法を記述します。
Devcontianer以外を用いた開発を行なう場合は、各自で環境構築を行ってください。

## 想定環境

このプロジェクトは以下の環境を想定しています。

- Windows / macOS / Linux / Linux over SSH
- Visual Studio Code
- Devcontainer
- Githubのアカウントがあること

## 開発環境構築手順

### 1. Visual Studio Codeのインストール

以下のリンクからVisual Studio Codeをダウンロードし、インストールしてください。

[Visual Studio Code](https://code.visualstudio.com/)

### 2. Remote - Containers拡張機能のインストール

Visual Studio Codeを起動し、左側のアクティビティバーから拡張機能を開きます。
検索バーに`Dev Containers`と入力し、拡張機能をインストールしてください。
あるいは、以下のリンクからインストールすることもできます。

[Dev Containers](vscode:extension/ms-vscode-remote.remote-containers)

### 3. Dockerのインストール

Dockerのインストール方法はホストマシンのOSによって異なるため、各自のOSに合わせてインストールしてください。

#### Windows

[WindowsへのDockerのインストール方法](./docker-installation-win.md)

このあとの手順では明示されていない限りWSL2内のシェル（このガイドではUbuntu）のシェルを使用してください。

#### macOS

[macOSへのDockerのインストール方法](./docker-installation-mac.md)

#### Linux

[LinuxへのDockerのインストール方法](./docker-installation-linux.md)

#### Linux over SSH

この場合は、リモートのLinuxマシンにSSH接続可能な状態を前提とします。
リモートのLinuxマシンに[LinuxへのDockerのインストール方法](./docker-installation-linux.md)に従ってDockerをインストールしてください。

### 4. Gitのインストール

以下の手順でGitをインストールしてください。

#### Windows (WSL2) / Linux / Linux over SSH

以下のコマンドを実行してGitをインストールしてください。

Debian系（Ubuntuなど）の場合

```bash
sudo apt udpate
sudo apt install -y git
```

RedHat系（CentOSなど）の場合

```bash
sudo dnf check-update
sudo dnf install -y git
```

#### macOS

macOSの場合は、Homebrewを使用してGitをインストールしてください。

```bash
brew install git
```

### 5. GithubへのSSH接続の設定

GithubへのSSH接続の設定を行います。

#### SSHキーの生成

以下のコマンドを実行してSSHキーを生成してください。

```bash
ssh-keygen -t ed25519
```

Enterを押すと、デフォルトのファイルパス（`~/.ssh/id_ed25519`）でSSHキーが生成されます。
Enterを押すと、パスフレーズを設定するよう求められますが、設定しない場合はEnterを押してください。

#### SSHキーの登録

以下のコマンドを実行して、公開鍵を表示してください。

```bash
cat ~/.ssh/id_ed25519.pub
```

表示された公開鍵をコピーし、[Githubの設定画面](https://github.com/settings/keys)からSSHキーを登録してください。

#### SSH接続のテスト

以下のコマンドを実行して、GithubへのSSH接続が成功しているか確認してください。

```bash
ssh -T git@github.com
```

### 5. このリポジトリをクローン

開発環境を構築したいディレクトリでシェルを開く、あるいはシェル内で`cd`コマンドを用いて移動してください。
Linux over SSHの場合は、リモートのLinuxマシンのシェルで操作してください。

以下のコマンドを実行し、このリポジトリをクローンしてください。

```bash
git clone git@github.com:tmcit-2024-adv-prog/quantum-gomoku.git
```

### 6. Visual Studio Codeで開く

#### Windows (WSL2) / macOS / Linux

以下のコマンドを実行して、このリポジトリをVisual Studio Codeで開いてください。

```bash
cd quantum-gomoku
code .
```

#### Linux over SSH

手元のマシンでVisual Studio Codeを起動し、左下の`><`のアイコンをクリックし、`ホストに接続する...`を選択してください。
リモートのLinuxマシンを選択して接続し、先ほどクローンしたこのリポジトリを開いてください。

### 7. Devcontainerを開く

Visual Studio Codeでこのリポジトリを開いた状態で、左下の`><`のアイコンをクリックし、`Reopen in Container`を選択してください。

### 8. 動作検証

Devcontainerが正常に起動したら、以下のコマンドを実行して動作検証を行ってください。

```bash
java -version
```

以下のような出力が表示されれば、Javaが正常にインストールされています。

```
openjdk version "21.0.4" 2024-07-16 LTS
OpenJDK Runtime Environment Corretto-21.0.4.7.1 (build 21.0.4+7-LTS)
OpenJDK 64-Bit Server VM Corretto-21.0.4.7.1 (build 21.0.4+7-LTS, mixed mode, sharing)
```

### 9. 開発環境構築完了

これで開発環境の構築が完了しました。
開発を行う際は、このDevcontainerを使用してください。

お疲れ様でした。
