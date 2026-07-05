@echo off
.\gradlew.bat clean runDatagen -x test && (
    .\gradlew.bat --stop
) || (
    .\gradlew.bat --stop
    echo runDatagen.bat failed. Press any key to continue...
    pause > nul
)