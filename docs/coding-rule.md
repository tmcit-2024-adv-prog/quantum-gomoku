# コーディング規約

## はじめに

基本的には[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)（[非公式日本語訳](https://kazurof.github.io/GoogleJavaStyle-ja/)）に従ってください。

## アクセス修飾子

- アクセス修飾子は原則としてprivateを使用してください。
- 外部から直接読み書きする必要がある場合はpublicを使用してください。
  - 外部から書き込みを行なう場合、適切に値のバリデーションを実装し、値のバリデーションはその値を保持するクラス内で行なってください。
    例: `setAge(int age)`メソッド内で`age`の値が0未満の場合は例外をスローする。
    ```java
    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("age must be greater than or equal to 0");
        }
        this.age = age;
    }
    ```
- 外部から読み込みのみ行う場合は、publicなgetterを実装してください。
  例: `getAge()`メソッド
  ```java
  public int getAge() {
      return age;
  }
  ```

## Javadoc

- 外部に公開するクラスやメソッドにはJavadocを記述してください。
  - メソッドの引数、戻り値、例外、戻り値の意味などを記述してください。
    例: `add(int a, int b)`メソッドのJavadoc
    ```java
    /**
     * 2つの引数を加算した結果を返します。
     *
     * @param a 加算する値
     * @param b 加算する値
     * @return 2つの引数を加算した結果
     */
    public int add(int a, int b) {
        return a + b;
    }
    ```

## 原則

以下の原則に従ってコードを記述してください。

- デッドコードの削除
  - 未使用の変数、メソッド、クラスなどのコードは削除する
- SOLID原則
  - 単一責任の原則
    - 各クラスやモジュールは 1 つの明確な責務や役割のみを担う
  - オープン/クローズドの原則
    - 新しい要件や機能を追加する際に、既存のコードを変更せずに拡張できるように設計されている
  - リスコフの置換原則
    - サブタイプは、そのスーパータイプと置換可能でなければならない
  - インターフェース分離の原則
    - クラスは不必要なインターフェースを持っていない
  - 依存性逆転の原則
    - 高レベルのモジュールは、低レベルのモジュールに依存してはならない。両方は抽象に依存すべきである
- DRY原則
  - 同じコードが複数の場所で繰り返されていない
- KISS原則
  - コードはシンプルかつ明確に問題を解決している
- YAGNI原則
  - 不要な機能を実装しない
  - 未来の機能を実装するための余分なコードを書かない
