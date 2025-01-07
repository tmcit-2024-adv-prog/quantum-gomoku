# テストについて

## テストの種類

### ユニットテスト

単体テストとも呼ばれ、プログラムの最小単位である関数やメソッドなどを単体でテストすることです。

### シナリオテスト

複数のクラスや関数を組み合わせて実際のシナリオを再現することで、システム全体の挙動をテストすることです。

## ユニットテストの実装について

### テスト全般

今回はJavaの単体テストフレームワークであるJUnitを使用します。
`app/src/test/java/jp/ac/metro_cit/adv_prog_2024/gomoku`のディレクトリ内にテストクラスを作成し、テストメソッドを実装していきます。

テストを書くときはAAAパターンやGiven-When-Thenパターンを意識しましょう。
準備→実行→判定の流れです。

また、テストは1項目1メソッドで書いたほうが良いです。
1つのメソッドに複数のテスト項目を含めるとテストが落ちている場合の原因の切り分けの難易度が上がります。

加えて、テストメソッドには`@DisplayName`アノテーションで表示名を適切に付与してください。
`./gradlew test`でテストを実行したときにどのテストで落ちているのかがわかりやすくなります。
このときの表示名は`[正常系] hogehogeのときにfugafugaになる`、`[異常系] piyopiyoのときにhogefuga例外が投げられる`のようにすると良いです。
正常系・異常系については後述します。

### 実装の網羅

単体テストではホワイトボックステストを前提とします。
ホワイトボックステストはコードの中の構造等に着目して行なうテストです。
ホワイトボックステストで一般的に知られている観点は以下の4つがあります。

- 命令網羅：それぞれの命令文が一度は実行される
- 分岐網羅：それぞれの分岐において真偽が一度は実行される
- 条件網羅：それぞれの分岐の条件文において真偽が一度は実行される
- 複合条件網羅：それぞれの分岐の条件文のすべての組み合わせに対して実行される

個人的には上2つの命令網羅と分岐網羅がテストできていれば問題ないと思います。
条件網羅に関しては必要そうな場合にやればいいと思います。

それぞれでどれだけ網羅できているかを表すカバレッジというのがありますが、命令網羅率と分岐網羅率は85%を目安にすれば良いと思います。
どうしても網羅できない場合もあるので100%を目指す必要はありません。

### テストの例（命令網羅の側面から）

以下は素数判定を行なうメソッドとそのテストクラスの例です。

#### 実装

```java
public class Prime {
  public boolean isPrime(int n) {
    if (n < 2) {
      return false;
    }
    for (int i = 2; i <= Math.sqrt(n); i++) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }
}
```

#### テスト

```java
public class PrimeTest() {
  @Test
  @DisplayName("[正常系] 0が入力されたとき、それは素数ではない")
  void test0IsNotPrime() {
    // Primeクラスが存在
    Prime prime = new Prime();

    // 0の素数判定の結果
    boolean result = prime.isPrime(0);

    // 0は素数ではない
    assetFalse(result);
  }

  @Test
  @DisplayName("[正常系] 2が入力されたとき、それは素数ではない")
  void test2IsPrime() {
    // Primeクラスが存在
    Prime prime = new Prime();

    // 2の素数判定の結果
    result = prime.isPrime(2);

    // 2は素数
    assetTrue(result);
  }

  @Test
  @DisplayName("[正常系] 17が入力されたとき、それは素数ではない")
  void test17IsPrime() {
    // Primeクラスが存在
    Prime prime = new Prime();

    // 17の素数判定の結果
    result = prime.isPrime(17);

    // 17は素数
    assetTrue(result);
  }

  @Test
  @DisplayName("[正常系] 999が入力されたとき、それは素数ではない")
  void test999IsNotPrime() {
    // Primeクラスが存在
    Prime prime = new Prime();

    // 999の素数判定の結果
    result = prime.isPrime(999);

    // 999は素数ではない
    assetFalse(result);
  }
}
```

### 正常系・異常系

命令網羅を意識すれば問題ない話ですが、正常系（本来想定される動作）だけでなく異常系（想定していないエラー等で例外が発生した場合）の処理に対してもテストを実装しましょう。
このときの表示名には`[正常系]`、`[異常系]`のようにprefixをつけるとテスト項目の判別がしやすくなって良いと思います。

### テストの例（正常系・異常系の観点から）

以下は例外が発生しうるメソッドとそのテストクラスの例です。

#### 実装

```java
public class Division {
  public float divide(int a, int b) {
    if (b == 0) {
      throw new IllegalArgumentException("0で割ることはできません");
    }
    return (float) a / b;
  }
}
```

#### テスト

```java
public class DivisionTest() {
  @Test
  @DisplayName("[正常系] 10を2で割ったとき、5になる")
  void test10DividedBy2Is5() {
    // Divisionクラスが存在
    Division division = new Division();

    // 10を2で割った結果
    float result = division.divide(10, 2);

    // 10を2で割ると5
    assertEquals(5, result);
  }

  @Test
  @DisplayName("[異常系] 10を0で割るとIllegalArgmentExceptionが発生する")
  void test10DividedBy0ThrowsIllegalArgumentException() {
    // Divisionクラスが存在
    Division division = new Division();

    // 10を0で割るとIllegalArgmentExceptionが発生
    assertThrows(IllegalArgumentException.class, () -> division.divide(10, 0));
  }
}
```


## VSCodeを使ったカバレッジの確認

VSCodeを使用している場合はテストの網羅率（カバレッジ）を確認することができます。

左側のバーのフラスコのアイコンをクリックし、該当のテストを右クリックして「カバレッジを使用してテストを実行」を押すとカバレッジを確認することができます。
これを実行すると実装のコードの背景が色分けされ、どの部分がテストされているかがわかりやすくなります。
赤色の部分がテストされていない部分です。

<details>

<summary>カバレッジの確認方法</summary>

https://github.com/user-attachments/assets/997692e0-858c-4b22-bfce-4c3023cdf119

</details>
