@echo off
:START
cls
robocopy . "C:\Users\Pascal\Documents\IntelliJ IDEA Projects\GW2EventNotifier\out\production\GW2EventNotifier\de\pakldev\gw2evno\res\webtemplate" /MIR
ping localhost -n 10 > NUL
GOTO:START