#!/bin/bash

# PixelStack Frontend Startup Script
# This script starts the Vue 3 development server

echo "================================"
echo "  PixelStack Frontend Startup"
echo "================================"
echo ""

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 18 or higher."
    exit 1
fi

echo "✅ Node.js version: $(node --version)"
echo "✅ npm version: $(npm --version)"

# Navigate to frontend directory
cd pixel-front || exit

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo ""
    echo "📦 Installing dependencies..."
    npm install
    if [ $? -ne 0 ]; then
        echo "❌ Failed to install dependencies"
        exit 1
    fi
fi

# Check if @vicons/ionicons5 is installed
if [ ! -d "node_modules/@vicons/ionicons5" ]; then
    echo ""
    echo "📦 Installing @vicons/ionicons5..."
    npm install @vicons/ionicons5
    if [ $? -ne 0 ]; then
        echo "⚠️  Warning: Failed to install @vicons/ionicons5"
    fi
fi

echo ""
echo "Starting development server..."
echo "================================"
echo ""
echo "Frontend will be available at: http://localhost:3000/"
echo ""

# Start Vite development server
npm run dev
