@echo off
echo 1: Build ^| 2: Run Client ^| 3: Run Test Client ^| 4: Run Server ^| 5: Clean ^| 6: Refresh Dependencies ^| 7: Run Datagen
set /p c="Choose task: "
if "%c%"=="1" set t=build
if "%c%"=="2" set t=clean runClient -x test
if "%c%"=="3" set t=clean runTestClient
if "%c%"=="4" set t=clean runServer -x test
if "%c%"=="5" set t=clean
if "%c%"=="6" set t=--refresh-dependencies
if "%c%"=="7" set t=runDatagen -x test
if not defined t exit /b

.\gradlew.bat %t% &&(
    .\gradlew.bat --stop
)||(
    .\gradlew.bat --stop
    echo Task failed. Press any key to continue...
    pause >nul
)