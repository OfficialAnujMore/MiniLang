#!/usr/bin/env bash
set -e

echo "Compiling MiniLang sources..."
mkdir -p out
javac $(find . -maxdepth 1 -name "MiniLang.java") \
      $(find ast lexer parser runtime -name "*.java") \
      -d out
echo "Build complete. Classes in out"
