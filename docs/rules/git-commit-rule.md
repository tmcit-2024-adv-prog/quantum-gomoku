# Gitコミットのルール

## はじめに

この文書は、Gitコミットのルールについて記述したものです。
絶対にこれに従う必要はありませんが、コミットメッセージに何を書くべきかわからない場合はこれに従ってください。

## コミットメッセージの書き方

コミットメッセージは以下のフォーマットに従って書いてください。
また、一行で説明できない、あるいは「なんとかとなんとかとなんとかと……なんとかの変更」のように多量の変更が必要な場合はコミットを分割してください。

```
<prefix>: <subject>
```

### prefix

コミットメッセージの先頭には以下のプレフィックスのいずれかをつけてください。

| プレフィックス    | 意味                                           |
| -------------- | --------------------------------------------- |
| chore          | ドキュメントの生成やビルドプロセス、ライブラリなどの変更 |
| ci             | CI用の設定やスクリプトに関する変更                   |
| docs           | ドキュメントのみの変更                             |
| feat           | 新機能                                          |
| fix            | 不具合の修正                                     |
| perf           | パフォーマンス改善を行うためのコードの変更             |
| refactor       | バグ修正や機能の追加を行わないコードの変更             |
| style          | コードの処理に影響しない変更（スペースや書式設定など）   |
| test           | テストコードの変更                                |

### subject

コミットメッセージの内容はどこ（何）をどうしたかを簡潔に書いてください。

#### Good

- `feat: なんとか機能を追加`
- `fix: なんとかの不具合を修正`
- `docs: なんとかのドキュメントを修正`

#### Bad

- `なんかもうめっちゃ変えた`
- `wip`
- `:art:`
- `:v:`
