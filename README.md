# Spring Boot APIキー認証サンプル

[English](docs/README.md)

## 概要
このプロジェクトは、APIキー認証を実装したSpring Bootのサンプルアプリケーションです。
セキュリティと教育的価値の両立を目指し、シンプルながら実践的な実装を提供しています。

## 技術スタック
- Java 17以上
- Spring Boot 3.4.3
- Maven
- Spring Web
- Spring Data JPA
- SpringDoc OpenAPI
- Spring Boot DevTools
- その他の主要な依存関係は[pom.xml](pom.xml)を参照してください

## 前提条件
- JDK 17以上がインストールされていること
- Mavenがインストールされていること
- （推奨）IntelliJ IDEA

## セットアップ
1. プロジェクトをクローンします
2. `application.properties`にAPIキーを設定します:
   ```properties
   api.key=your-api-key-here
   ```

## アプリケーションの起動方法
以下のコマンドでアプリケーションを起動できます：

```bash
# Mavenラッパーを使用する場合
./mvnw spring-boot:run

# システムにインストールされたMavenを使用する場合
mvn spring-boot:run
```

起動後、アプリケーションは `http://localhost:8080` でアクセス可能になります。

## テストスクリプトの実行方法

### cURLによるAPIテスト
以下のスクリプトを使用してAPIをテストできます：

```bash
# Windowsの場合
scripts\curl\filter-test.sh

# Unix/Linuxの場合
./scripts/curl/filter-test.sh
```

なお、APIリクエスト時には必ず`x-api-key`ヘッダーを指定する必要があります：

```bash
curl -H "x-api-key: your-api-key-here" http://localhost:8080/api/items
```

### 単体テストの実行

```bash
# Mavenラッパーを使用する場合
./mvnw test

# システムにインストールされたMavenを使用する場合
mvn test
```

## API仕様
OpenAPI（Swagger）のドキュメントは以下のURLで確認できます：
- http://localhost:8080/swagger-ui.html
- http://localhost:8080/v3/api-docs

## エラーレスポンス
APIは以下の形式でエラーレスポンスを返します：

```json
{
    "status": 401,
    "message": "Missing required header: x-api-key"
}
```

主なエラーレスポンス：
- 401 Unauthorized: 認証エラー（APIキーがない、または不正な場合）
- 404 Not Found: リソースが見つからない場合
- 500 Internal Server Error: サーバー内部エラー

## 既知の課題
- APIキーは現在、`application.properties`に平文で保存されています
- 本番環境ではよりセキュアなAPIキー管理方法の実装が必要です
- レート制限の実装が必要かもしれません

## プロジェクト構造
- `src/main/java/com/example/demo/controller`: APIエンドポイントの実装
- `src/main/java/com/example/demo/service`: ビジネスロジック
- `src/main/java/com/example/demo/model`: データモデル
- `src/main/java/com/example/demo/interceptor`: 認証用インターセプター
- `src/main/java/com/example/demo/exception`: エラーハンドリング
- `src/main/java/com/example/demo/config`: アプリケーション設定

## ライセンス
このプロジェクトはMITライセンスの下で公開されています。
