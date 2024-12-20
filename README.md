# quantum-gomoku

量子五目並べ

## 開発環境

### 必要なツール

#### Dockerを使用する場合

- Docker

#### Dockerを使用しない場合

- OpenJDK 21
- Gradle 8.10.2

### 開発環境の構築

開発環境の構築手順は以下のリンクを参照してください。

[開発環境構築手順](./docs/dev-env/setup-development-environment.md)

## ディレクトリ構成

```
(repository root)
├── docs              # ドキュメント
└── app
    └── src
        ├── main
        │   ├── java  # Javaソースコード
        │   │   └── jp/ac/metro_cit/adv_prog_2024/quantum_gomoku  # 五目並べのパッケージ
        │   │       ├── interfaces      # インターフェース定義
        │   │       ├── models          # モデル データを保持するためのクラス
        │   │       ├── services        # システムロジックを実装するクラス
        │   │       ├── ui              # UIを実装するクラス
        │   │       ├── communications  # 通信を行うクラス
        │   │       ├── controllers     # コントローラー 通信を受け取り、サービスを呼び出すクラス
        │   │       └── utils           # 全体で使用するユーティリティクラス
        │   └── resources # リソース
        └── test
            └── java  # テストコード
                └── jp/ac/metro_cit/adv_prog_2024/quantum_gomoku
                    ├── models
                    ├── services
                    ├── ui
                    ├── communications
                    ├── controllers
                    └── utils
```
