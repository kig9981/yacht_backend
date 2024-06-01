#!/bin/bash

response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -H "Content-Type: application/json" -X GET "http://localhost:8080/rooms")

# 응답과 상태 코드 분리
http_body=$(echo "$response" | sed -e 's/HTTP_CODE:.*//g')
http_code=$(echo "$response" | tr -d '\n' | sed -e 's/.*HTTP_CODE://')

# 상태 코드 확인
if [ "$http_code" -eq 200 ]; then
    echo "Request was successful."
    echo "Response Body: $http_body"

    echo "Room list:"
    echo "$http_body" | jq -r '.[] | "Room ID: \(.roomId), Host User Id: \(.hostUserId)"'
else
  echo "Request failed with status code $http_code"
  echo "Response Body: $http_body"
fi