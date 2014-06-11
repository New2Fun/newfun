@echo off

if "%JAVA_HOME%" == "" goto javaerror
if not exist "%JAVA_HOME%\bin\java.exe" goto javaerror
goto run

:javaerror
echo.
echo Error: JAVA_HOME environment variable not set, 2dcode not started.
echo.
goto end

:run
"%JAVA_HOME%\bin\java" -jar 2dcode.jar %*

:end


