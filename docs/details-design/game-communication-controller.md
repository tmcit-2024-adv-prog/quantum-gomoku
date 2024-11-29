# 試合と通信をいい感じにするアレの設計

## ゲームの流れ

### 先行

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
10. 5つ並んだか判断する
  1. 並んでいたら負け（11に進む）
  2. 並んでいなかったら7に戻る
11. 勝敗表示
12. ばいばい

### 後攻

1. 名前を入力
2. 五目並べを開始ボタンを押す
3. 待機画面に移行
4. 相手が見つかるまで待機
5. 相手が見つかったらゲーム画面に移行
6. 石の色が決まる
7. 相手の操作を待つ
8. 5つ並んだか判断する
  1. 並んでいたら負け（11に進む）
  2. 並んでいなかったら7に戻る
9. 石を置く
10. 5つ並んだか判断する
  1. 並んでいたら勝ち（11に進む）
  2. 並んでいなかったら7に戻る
11. 勝敗表示
12. ばいばい

### 投了処理

- 投了ボタンを押したときに相手に対して試合フェーズが終了済みになったGameStateを送信する
- 試合フェーズが終了済みになったGameStateを受け取った場合、勝敗判定を行なって5つ並んでいなかった場合は投了されたと判断する

## メンバ変数

- 試合 (game)
- これを操作しているプレイヤー (localPlayer)
- 相手プレイヤー (remotePlayer)
- Sender (sender)
- Receiver (receiver)
- UIに対して試合の状態を送るためのコールバック (gameStateCallback)

## パブリックメソッド

これは外からアクセス可能なメソッド

- GameCommunicationController(Player localPlayer, Player remotePlayer, Sender sender, Receiver receiver, GameStateCallback gameStateCallback)
  - コンストラクタ

- startGame(): GameState
  - ゲームを開始する
  - 1(UI)で呼び出される
  - GameStateCallbackが設定されていない場合は例外を投げる

- PutStone(color: Color, x: int, y: int): GameState throws PutStoneException
  - 石を置く
  - 石を配置したあとの試合の状況を返す
  - おけなっかたらPutStoneExceptionを投げる
  - 7(UI)で呼び出される

- surrender(): GameState
  - ばいばい
  - 降参時に呼び出される
  
- getGameState(): GameState
  - 現在の試合の状態を取得する
  - 任意のタイミングでUIから呼び出される

- setGameState(GameState state): void
  - 現在の試合の状態を設定する
  - 任意のタイミングでReceiverから呼び出される
  - Receiverはこのメソッドを利用して受け取ったデータを試合に反映する

- setGameStateCallback(GameStateCallback callback): void
  - UIに対して試合の状態を送るためのコールバックを設定する
  - 1の前にUIから呼び出される
  
## プライベートメソッド

これは外からアクセスできないメソッド

- startListen(): void
  - Recieverを用いて待ち受けを開始する
  - 3で呼び出される

- DecideColor(): Color
  - これはサーバ側で呼び出される
  - 石の色を決める
  - 6で呼び出される

## 備考

### UIに対して試合の状態を送るためのコールバック

イメージ↓

```java
public interface GameStateCallback {
    void onGameStateChanged(GameState state);
}

public class GUI implements GameStateCallback {
    public GUI() {
      // なんとかかんとか
    }

    @Override
    public void onGameStateChanged(GameState state) {
        this.display.showStone(state.board.getStone()[0][0]);
    }
}
```

### Sender

相手にデータを送るためのクラス

```java
public interface Sender {
    void send(GameState state);
}
```

### Receiver

相手からデータを受け取るためのクラス

```java
public interface Receiver {
    void startReceive();
}
```
