@echo off
setlocal

set JETTY_PORT=8083
%M2_HOME%\bin\mvn --activate-profiles=jetty -Djetty.port=%JETTY_PORT% jetty:run

