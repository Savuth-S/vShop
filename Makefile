SRC_PATH=./src
BIN_PATH=./bin
OUT_PATH=./release
PKG_PATH=sav/manager
VPATH=${SRC_PATH}

LIBS_FLDR=lib
RES_FLDR=res

FILES=${PKG_PATH}/Main.java
CLASSES=$(FILES:%.java=%.class)
MAIN=Main
NAME=vshop-dbmanager

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
	${JVM} ${JLIBS} ${MNGR_PKG}/${MAIN}
	
	@rm -r ${BIN_PATH}/${LIBS_FLDR} ${BIN_PATH}/${RES_FLDR} 

${OUT_PATH}:
	@mkdir -p ${OUT_PATH}

build: ${OUT_PATH} source
	@mkdir -p ${BIN_PATH}/${RES_FLDR}
	@cp -r ${SRC_PATH}/${RES_FLDR}/* ${BIN_PATH}/${RES_FLDR}/

	cd ${BIN_PATH}; \
	${JJM} ${JJMFLAGS} .${OUT_PATH}/${NAME}.jar .${SRC_PATH}/${PKG_PATH}/Manifest.txt ./${PKG_PATH}/* ./${RES_FLDR}
	
	@rm -r ${BIN_PATH}/${RES_FLDR} 
