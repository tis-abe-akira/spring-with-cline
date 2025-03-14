#!/bin/bash

# APIキー認証フィルターのテストスクリプト

# 色の定義
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# APIキー
API_KEY="1234567890abcdef"

# テスト結果カウンター
PASSED=0
FAILED=0

# テスト関数
run_test() {
  local test_name="$1"
  local expected_code="$2"
  local curl_command="$3"
  
  echo -e "\n${YELLOW}テスト: $test_name${NC}"
  echo "リクエスト送信中..."
  
  # curlコマンドを実行（詳細出力を抑制）
  response=$(eval "$curl_command -s -w '\n%{http_code}'")
  
  # レスポンスとステータスコードを分離
  http_body=$(echo "$response" | sed '$ d')
  http_code=$(echo "$response" | tail -n1)
  
  echo "ステータスコード: $http_code"
  
  # 結果の判定
  if [ "$http_code" -eq "$expected_code" ]; then
    echo -e "${GREEN}✅ 成功: 期待通りのステータスコード $expected_code が返されました${NC}"
    PASSED=$((PASSED + 1))
    return 0
  else
    echo -e "${RED}❌ 失敗: 期待されるステータスコード $expected_code ではなく $http_code が返されました${NC}"
    FAILED=$((FAILED + 1))
    return 1
  fi
}

echo -e "${BLUE}==================================================${NC}"
echo -e "${BLUE}      APIキー認証フィルター動作確認テスト      ${NC}"
echo -e "${BLUE}==================================================${NC}"

# テスト1: x-api-keyヘッダーなし
curl_command="curl -X POST \"http://localhost:8080/api/items\" \\
  -H \"Content-Type: application/json\" \\
  -H \"Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.example.token\" \\
  -H \"deviceUuid: 550e8400-e29b-41d4-a716-446655440000\" \\
  -H \"devicePlatform: Android\" \\
  -d '{\"name\": \"Test Item\", \"description\": \"This is a test item\"}'"

run_test "x-api-keyヘッダーなし" 400 "$curl_command"

# テスト2: 不正なx-api-keyヘッダー
curl_command="curl -X POST \"http://localhost:8080/api/items\" \\
  -H \"Content-Type: application/json\" \\
  -H \"Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.example.token\" \\
  -H \"deviceUuid: 550e8400-e29b-41d4-a716-446655440000\" \\
  -H \"devicePlatform: Android\" \\
  -H \"x-api-key: invalid-api-key\" \\
  -d '{\"name\": \"Test Item\", \"description\": \"This is a test item\"}'"

run_test "不正なx-api-keyヘッダー" 400 "$curl_command"

# テスト3: 正しいx-api-keyヘッダー
curl_command="curl -X POST \"http://localhost:8080/api/items\" \\
  -H \"Content-Type: application/json\" \\
  -H \"Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.example.token\" \\
  -H \"deviceUuid: 550e8400-e29b-41d4-a716-446655440000\" \\
  -H \"devicePlatform: Android\" \\
  -H \"x-api-key: $API_KEY\" \\
  -d '{\"name\": \"Test Item\", \"description\": \"This is a test item\"}'"

run_test "正しいx-api-keyヘッダー" 201 "$curl_command"

# テスト結果のサマリー
echo -e "\n${BLUE}==================================================${NC}"
echo -e "${BLUE}                テスト結果サマリー                ${NC}"
echo -e "${BLUE}==================================================${NC}"
echo -e "${GREEN}成功: $PASSED${NC}"
echo -e "${RED}失敗: $FAILED${NC}"

if [ $FAILED -eq 0 ]; then
  echo -e "\n${GREEN}✅ すべてのテストが成功しました！${NC}"
  echo -e "${GREEN}APIキー認証フィルターは正しく機能しています。${NC}"
else
  echo -e "\n${RED}❌ 一部のテストが失敗しました。${NC}"
  echo -e "${RED}APIキー認証フィルターの設定を確認してください。${NC}"
fi

echo -e "\n${BLUE}==================================================${NC}"
