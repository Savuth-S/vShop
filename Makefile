SRC_PATH=./src
BIN_PATH=./bin
OUT_PATH=./release
VPATH=${SRC_PATH}

PKG_PATH=sav/vshop
LIBS_FLDR=lib
RES_FLDR=res

FILES=Main.java
CLASSES=$(FILES:%.java=%.class)
MAIN=Main
NAME=vShop

JC=javac
JCFLAGS=-g
JVM=java
JLIBS=-cp ./${LIBS_FLDR}/*:.
JJM=jar
JJMFLAGS=cf

.SUFFIXES: .java .class

source: ${BIN_PATH} ${CLASSES}

${BIN_PATH}: ${BIN_PATH}/${LIBS_FLDR} ${BIN_PATH}/${RES_FLDR}
	@mkdir -p ${BIN_PATH}

${BIN_PATH}/${LIBS_FLDR}:
	@cp -r ${SRC_PATH}/${LIBS_FLDR}/ ${BIN_PATH}/

${BIN_PATH}/${RES_FLDR}:
	@cp -r ${SRC_PATH}/${RES_FLDR}/ ${BIN_PATH}/

%.class:
	cd ${SRC_PATH}; $(JC) $(JLIBS) ${JFLAGS} ./${PKG_PATH}/$*.java -d .${OUT_PATH}
	@cp -r ${SRC_PATH}/${LIBS_FLDR}/ ${OUT_PATH}/
	@cp -r ${SRC_PATH}/${RES_FLDR}/ ${OUT_PATH}/

run: source
	cd ${BIN_PATH}; ${JVM} ${JLIBS} ${PKG_PATH}/${MAIN}

build: ${OUT_PATH} source
	${JJM} ${JJMFLAGS} ${OUT_PATH}/${NAME}.jar ${BIN_PATH}/*

${OUT_PATH}:
	@mkdir -p ${OUT_PATH}
