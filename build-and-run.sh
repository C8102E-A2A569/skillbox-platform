#!/bin/bash

echo "Building catalog-service..."
cd catalog-service
./mvnw clean package -DskipTests
cd ..

echo "Building payment-service..."
cd payment-service
./mvnw clean package -DskipTests
cd ..

echo "Building Docker images and starting containers..."
docker-compose down
docker-compose build --no-cache
docker-compose up -d

echo "Waiting for services to start..."
sleep 10

echo "Checking service status..."
docker-compose ps

echo "You can access the services at:"
echo "Catalog Service: http://localhost:8082/swagger-ui/index.html"
echo "Payment Service: http://localhost:8080/swagger-ui/index.html"
