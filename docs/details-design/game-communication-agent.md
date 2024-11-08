# 試合と通信をいい感じにするアレの設計

## ゲームの流れ

これは先行の場合

1. 名前を入力
2. 五目並べを開始ボタンを押す
3. 待機画面に移行
4. 相手が見つかるまで待機
5. 相手が見つかったらゲーム画面に移行
6. 石の色が決まる
7. 石を置く
8. 5つ並んだか判断する
   1. 並んでいたら勝ち（11に進む）
   2. 並んでいなかったら相手のターン
9. 相手の操作を待つ
10. 7に戻る
11. 勝敗表示
12. ばいばい

## メンバ変数

- 試合
- これを操作しているプレイヤー
- 相手プレイヤー
- Sender
- Receiver

## パブリックメソッド

これは外からアクセス可能なメソッド

<!-- - ReceiveData(obj: Object←これ未定): void
  - Receiverがこれを叩いてデータを受け取る -->
  
- CreatePlayer(name: String): Player
  - これを操作しているプレイヤーを定義する
  - 2でUIから呼び出される

- CreateRemotePlayer(name: String): Player
  - 相手プレイヤーを定義する
  - 5でReceiverから呼び出される

- PutStone(color: Color, x: int, y: int): GameStatus throws PutStoneException
  - 石を置く
  - 石を配置したあとの試合の状況を返す
  - おけなっかたらPutStoneExceptionを投げる
  - Senderを通して相手に石を置いたことを通知する
  - 7(UI), 9(Receiver)で呼び出される

- surrender(): void
  - ばいばい
  - 12でUIから呼び出される
  - 任意のタイミングでReceiverから呼び出される
  
- getGameStatus(): GameStatus
  - 現在の試合の状態を取得する
  - 任意のタイミングでUIから呼び出される
  
## プライベートメソッド

これは外からアクセスできないメソッド

- StartListen(): void
  - Recieverを用いて待ち受けを開始する
  - 3で呼び出される

- DecideColor(): Color
  - これはサーバ側で呼び出される
  - 石の色を決める
  - 6で呼び出される
