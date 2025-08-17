#!/usr/bin/env bash
set -e

# Default program if none provided
PROGRAM=${1:-samples/program.ml}

if [ ! -f "$PROGRAM" ]; then
  echo "Program file '$PROGRAM' not found."
  echo "Usage: ./run.sh <your_program.ml>"
  exit 1
fi

echo "Running MiniLang interpreter on $PROGRAM..."
java -cp out MiniLang "$PROGRAM"
