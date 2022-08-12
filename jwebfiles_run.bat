set temp4class=out2
set srcdir=\manager\src\

md %temp4class%
cd %temp4class%

set relsrc=..%srcdir%

javac -encoding "UTF-8" -d . -cp .;%relsrc%; -sourcepath %relsrc%; %relsrc%*.java

cd ..\
java -cp .;%temp4class%; Main

pause


call jwebfiles_run.bat