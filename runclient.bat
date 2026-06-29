@echo off
.\gradlew.bat clean runClient -x test && (
    .\gradlew.bat --stop
) || (
    .\gradlew.bat --stop
    echo runclient.bat failed. Press any key to continue...
    pause > nul
)