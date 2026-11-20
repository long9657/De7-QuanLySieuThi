@echo off
rem Chạy thử QLST (Tomcat 9 nhúng + H2) tại http://localhost:8080/
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.12"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
mvn -q -B compile exec:java
