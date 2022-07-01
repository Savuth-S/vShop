SRC_PATH=./src
BIN_PATH=./bin
OUT_PATH=./release
PKG_PATH=sav/utils
VPATH=${SRC_PATH}

FILES=${PKG_PATH}/Crypt.java 
CLASSES=$(FILES:%.java=%.class)
NAME=sav-utils

JC=javac
JCFLAGS=-g
JVM=java
JLIBS=-cp ./${LIBS_FLDR}/*:.
JJM=jar
JJMFLAGS=-cvf
.SUFFIXES: .java .class

source: ${BIN_PATH} ${CLASSES}

${BIN_PATH}:
	@mkdir -p ${BIN_PATH}

%.class:
	cd ${SRC_PATH}; \
	${JC} ${JLIBS} ${JCFLAGS} ./$*.java -d .${BIN_PATH}

${OUT_PATH}:
	@mkdir -p ${OUT_PATH}

build: ${OUT_PATH} source 
	@mkdir -p ${BIN_PATH}/${RES_FLDR}
	@cp -r ${SRC_PATH}/${RES_FLDR}/* ${BIN_PATH}/${RES_FLDR}/

	cd ${BIN_PATH}; \
	${JJM} ${JJMFLAGS} .${OUT_PATH}/${NAME}.jar ./${PKG_PATH}/*
	
	@rm -r ${BIN_PATH}/${RES_FLDR} 
