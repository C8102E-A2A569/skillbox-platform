#!/bin/bash

BASE_URL_CATALOG="http://localhost:8082/catalog"
BASE_URL_PAYMENT="http://localhost:8080/payment"


# Получение списка направлений
echo "📌 Получение списка направлений:"
curl -X GET "$BASE_URL_CATALOG/directions" -H "Content-Type: application/json"
echo -e "\n"

# Получение курсов по направлению
echo "📌 Получение курсов по направлению:"
direction=$(echo -n "Программирование" | jq -sRr @uri)
curl -X GET "$BASE_URL_CATALOG/courses/$direction" -H "Content-Type: application/json"
echo -e "\n"

# Получение информации о курсе
echo "📌 Получение информации о курсе (id=1):"
curl -X GET "$BASE_URL_CATALOG/course/1" -H "Content-Type: application/json"
echo -e "\n"

# Запись пользователя на курс
echo "📌 Запись пользователя на курс с ВК"
curl -X POST "$BASE_URL_CATALOG/enroll/auth-method" -H "Content-Type: application/json" -d '{
  "userId": "2",
  "courseId": "1",
  "method": "vk",
  "tariff": "STARTER"
}'
echo -e "\n"

# Запись пользователя на курс вручную
echo "📌 Запись пользователя на курс с ручным заполнением данных"
curl -X POST "$BASE_URL_CATALOG/enroll" -H "Content-Type: application/json" -d '{
    "userId": "2",
    "courseId": "2",
    "name": "Дмитрий Борисович Афанасьев",
    "email": "dima@example.com",
    "tariff": "STARTER"
}'
echo -e "\n"

echo "📌 Создание платежа для пользователя:"
payment_response=$(curl -X POST "$BASE_URL_PAYMENT/create" -H "Content-Type: application/json" -d '{
  "userId": "2",
  "courseId": "1",
  "tariff": "STARTER",
  "name": "Дмитрий Борисович Афанасьев",
  "email": "dima@example.com"
}')
payment_link=$(echo "$payment_response" | jq -r '.paymentLink')

echo "Ссылка на оплату: $payment_link"

# Процесс оплаты
echo "📌 Выполнение оплаты для пользователя:"
payment_status=$(curl -X POST "$BASE_URL_PAYMENT/process" -G --data-urlencode "userId=2" --data-urlencode "paymentLink=$payment_link" --data-urlencode "amount=10")
echo "Статус платежа: $payment_status"
echo -e "\n"