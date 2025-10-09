#!/bin/bash

# PixelStack Backend Startup Script
# This script starts the Spring Boot backend service

echo "================================"
echo "  PixelStack Backend Startup"
echo "================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install JDK 1.8 or higher."
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"

# Check if MySQL is accessible
echo ""
echo "Checking MySQL connection..."
if ! command -v mysql &> /dev/null; then
    echo "⚠️  MySQL client not found. Make sure MySQL server is running."
else
    echo "✅ MySQL client found"
fi

# Check if Redis is running
echo ""
echo "Checking Redis connection..."
if command -v redis-cli &> /dev/null; then
    if redis-cli ping &> /dev/null; then
        echo "✅ Redis is running"
    else
        echo "❌ Redis is not running. Please start Redis:"
        echo "   macOS: brew services start redis"
        echo "   Linux: sudo systemctl start redis"
        exit 1
    fi
else
    echo "⚠️  Redis CLI not found. Make sure Redis server is running."
fi

echo ""
echo "Starting backend service..."
echo "================================"
echo ""

# Navigate to backend directory
cd pixel-backend || exit

# Run Spring Boot application
echo "Running: mvn spring-boot:run"
echo ""
mvn spring-boot:run

# If Maven fails, provide helpful message
if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Failed to start backend service."
    echo ""
    echo "Troubleshooting tips:"
    echo "1. Check if port 8080 is already in use: lsof -i :8080"
    echo "2. Verify database 'ims' exists and is accessible"
    echo "3. Check MySQL credentials in: src/main/resources/application-dev.yml"
    echo "4. Review logs above for specific error messages"
    exit 1
fi
