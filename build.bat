@echo off
.\gradlew.bat clean build -x test && (
    .\gradlew.bat --stop
    echo Build.bat complete. Press any key to continue...
    pause > nul
) || (
    .\gradlew.bat --stop
    echo Build.bat failed. Press any key to continue...
    pause > nul
)