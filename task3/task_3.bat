@echo off
Rem copies files from src to dest replacing existing files
copy ".\resources\src" ".\resources\dest"
Rem if tool.exe exists, renames it to launcher.exe
if exist ".\resources\dest\tool.exe" ren ".\resources\dest\tool.exe" "launcher.exe"
REm clean-ups “src” folder (deletes all files)
rmdir ".\resources\src" /s /q
Rem runs launcher.exe
start ".\resources\dest\launcher.exe" 







