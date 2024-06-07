@echo off
@setlocal enableextensions
@cd /d "%~dp0"

echo Running Application
java -jar recipe-cost-calculator-app/target/recipe-cost-calculator-app-1.0-SNAPSHOT.jar

echo Operation Completed!
pause