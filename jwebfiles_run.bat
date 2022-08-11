md out2
cd out2

set main=..\manager\src\

javac -encoding "UTF-8" -d . -cp .;%main%; -sourcepath %main%; %main%*.java

java -cp .;%main%; Main

pause

cd ..
call j11.bat