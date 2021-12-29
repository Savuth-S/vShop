#!/bin/bash

cd /home/carlos/git/WPOSShop/

cd src/
javac -cp lib/*:. main/java/Main.java -d ../bin/debug/
cd ../bin/debug/
java main.java.Main
