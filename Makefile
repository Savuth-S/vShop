SRC_PATH=./src
BIN_PATH=./bin
OUT_PATH=./release
VPATH=${SRC_PATH}

LIBS_FLDR=lib
RES_FLDR=res

ROOT_PKG=sav
MAIN_PKG=${ROOT_PKG}/vshop
UTLS_PKG=${ROOT_PKG}/utils
MNGR_PKG=${ROOT_PKG}/manager

FILES=${MAIN_PKG}/Main.java \
	  ${MNGR_PKG}/Main.java 
CLASSES=$(FILES:%.java=%.class)
MAIN=Main
NAME=vshop

JC=javac
JCFLAGS=-g
JVM=java
JLIBS=-cp ./${LIBS_FLDR}/*:.
JJM=jar
JJMFLAGS=-cvfm
.SUFFIXES: .java .class

source: ${BIN_PATH} ${CLASSES}

${BIN_PATH}:
	@mkdir -p ${BIN_PATH}

%.class:
	cd ${SRC_PATH}; \
	${JC} ${JLIBS} ${JCFLAGS} ./$*.java -d .${BIN_PATH}

run: source
	@mkdir -p ${BIN_PATH}/${LIBS_FLDR} ${BIN_PATH}/${RES_FLDR}
	@cp -r ${SRC_PATH}/${LIBS_FLDR}/* ${BIN_PATH}/${LIBS_FLDR}/
	@cp -r ${SRC_PATH}/${RES_FLDR}/* ${BIN_PATH}/${RES_FLDR}/

	cd ${BIN_PATH}; \
	${JVM} ${JLIBS} ${MAIN_PKG}/${MAIN}
	
	@rm -r ${BIN_PATH}/${LIBS_FLDR} ${BIN_PATH}/${RES_FLDR} 

${OUT_PATH}:
	@mkdir -p ${OUT_PATH}

build: ${OUT_PATH} source uitls dbtools
	@mkdir -p ${BIN_PATH}/${RES_FLDR}
	@cp -r ${SRC_PATH}/${RES_FLDR}/* ${BIN_PATH}/${RES_FLDR}/

	cd ${BIN_PATH}; \
	${JJM} ${JJMFLAGS} .${OUT_PATH}/${NAME}.jar .${SRC_PATH}/${MAIN_PKG}/Manifest.txt ./${MAIN_PKG}/* ./${RES_FLDR}
	
	@rm -r ${BIN_PATH}/${RES_FLDR} 


MNGR_MAIN=Init
MNGR_NAME=vshop-dbmanager

dbtools: ${OUT_PATH} source
	cd ${BIN_PATH}; \
	${JJM} ${JJMFLAGS} .${OUT_PATH}/${MNGR_NAME}.jar .${SRC_PATH}/${MNGR_PKG}/Manifest.txt ./${MNGR_PKG}/*


UTLS_NAME=sav-utils

utils: ${OUT_PATH} source
	cd ${BIN_PATH}; \
	${JJM} -cvf .${OUT_PATH}/${UTLS_NAME}.jar ./${UTLS_PKG}/*
