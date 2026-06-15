@echo off
.\gradlew.bat clean runClient -x test
.\gradlew.bat --stop
echo runclient.bat complete. Press any key to continue...
pause > nul