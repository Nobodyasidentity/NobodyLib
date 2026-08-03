@echo off
.\gradlew.bat clean runTestClient -x test &&(
    .\gradlew.bat --stop
)||(
    .\gradlew.bat --stop
    echo runtestclient.bat failed. Press any key to continue...
    pause > nul
)