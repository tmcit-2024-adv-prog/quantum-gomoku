# 開発環境構築ガイド

このプロジェクトではDevcontianerを用いた開発を推奨しています。
そのため、このガイドではDevcontainerを用いた開発環境構築方法を記述します。
Devcontianer以外を用いた開発を行なう場合は、各自で環境構築を行ってください。

## 想定環境

このプロジェクトは以下の環境を想定しています。

- Visual Studio Code
- Devcontainer

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

#### macOS

[macOSへのDockerのインストール方法](./docker-installation-mac.md)

#### Linux

[LinuxへのDockerのインストール方法](./docker-installation-linux.md)
