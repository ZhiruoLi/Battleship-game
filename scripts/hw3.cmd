@ECHO OFF
javadoc -version -d ..\docs ..\src\gol\*.java
javac -cp ..\src\gol\*.java
java -cp ..\src\gol.BattleshipApp %1
