#!/bin/bash

clear

cd src/
javac -cp lib/*:. main/java/Main.java -d ../bin/

cp -r ./lib/ ../bin/
cp -r ./res/ ../bin/

cd ../bin/
java -cp lib/*:. main.java.Main
