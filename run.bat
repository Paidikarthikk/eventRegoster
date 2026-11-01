@echo off
REM Compiles and runs the College Event Registration System (Windows)
if not exist out mkdir out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
del sources.txt
if errorlevel 1 exit /b 1
java -cp out Main
