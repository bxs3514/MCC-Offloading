@echo off & setlocal EnableDelayedExpansion
for /f "delims=" %%i in ('"dir /a/s/b/on *.jar"') do (
set file=%%~nxi
dx --dex --output=classes.dex %%~nxi
aapt add %%~nxi classes.dex
del classes.dex
)