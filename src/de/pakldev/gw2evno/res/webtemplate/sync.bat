@echo off
:START
cls
robocopy . "..\..\..\..\..\..\out\production\GW2EventNotifier\de\pakldev\gw2evno\res\webtemplate" /MIR
ping localhost -n 10 > NUL
GOTO:START