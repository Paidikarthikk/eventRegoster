#!/bin/sh
# Compiles and runs the College Event Registration System (Linux / macOS)
mkdir -p out
javac -d out $(find src -name "*.java") && java -cp out Main
