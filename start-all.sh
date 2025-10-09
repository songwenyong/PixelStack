#!/bin/bash

# PixelStack Full Stack Startup Script
# This script starts both backend and frontend services

echo "======================================="
echo "  PixelStack Full Stack Startup"
echo "======================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check prerequisites
echo "Checking prerequisites..."
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java is not installed${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Java: $(java -version 2>&1 | head -n 1)${NC}"

# Check Node.js
if ! command -v node &> /dev/null; then
    echo -e "${RED}❌ Node.js is not installed${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Node.js: $(node --version)${NC}"

# Check MySQL
if command -v mysql &> /dev/null; then
    echo -e "${GREEN}✅ MySQL client found${NC}"
else
    echo -e "${YELLOW}⚠️  MySQL client not found${NC}"
fi

# Check Redis
if command -v redis-cli &> /dev/null; then
    if redis-cli ping &> /dev/null 2>&1; then
        echo -e "${GREEN}✅ Redis is running${NC}"
    else
        echo -e "${RED}❌ Redis is not running${NC}"
        echo "Please start Redis first:"
        echo "  macOS: brew services start redis"
        echo "  Linux: sudo systemctl start redis"
        exit 1
    fi
else
    echo -e "${YELLOW}⚠️  Redis CLI not found${NC}"
fi

echo ""
echo "======================================="
echo ""

# Ask user which services to start
echo "Which services would you like to start?"
echo "1) Backend only"
echo "2) Frontend only"
echo "3) Both (recommended)"
echo ""
read -p "Enter your choice (1-3): " choice

case $choice in
    1)
        echo ""
        echo "Starting backend service..."
        ./start-backend.sh
        ;;
    2)
        echo ""
        echo "Starting frontend service..."
        ./start-frontend.sh
        ;;
    3)
        echo ""
        echo "Starting both services..."
        echo ""
        echo -e "${YELLOW}Note: Backend will start first. Once it's running,${NC}"
        echo -e "${YELLOW}open a new terminal and run: ./start-frontend.sh${NC}"
        echo ""
        read -p "Press Enter to continue..."

        # Start backend in background
        echo ""
        echo "Starting backend..."
        ./start-backend.sh &
        BACKEND_PID=$!

        # Wait a bit for backend to start
        echo ""
        echo "Waiting for backend to initialize (30 seconds)..."
        sleep 30

        # Check if backend is still running
        if ps -p $BACKEND_PID > /dev/null; then
            echo ""
            echo -e "${GREEN}✅ Backend is running (PID: $BACKEND_PID)${NC}"
            echo ""
            echo "Starting frontend..."
            ./start-frontend.sh
        else
            echo ""
            echo -e "${RED}❌ Backend failed to start. Check logs above.${NC}"
            exit 1
        fi
        ;;
    *)
        echo "Invalid choice. Exiting."
        exit 1
        ;;
esac
