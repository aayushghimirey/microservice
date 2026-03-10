@echo off
call mvn clean compile -DskipTests > build_output.log 2>&1
echo DONE
