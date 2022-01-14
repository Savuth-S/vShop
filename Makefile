SRC_PATH=./src
OUT_PATH=./bin
VPATH=${SRC_PATH}:${OUT_PATH}

PKG_PATH=main/java
LIBS_FLDR=lib
RES_FLDR=res

FILES= Main.java
CLASSES= $(FILES:%.java=%.class)
MAIN= Main

JC=javac
JFLAGS=-g
JVM=java
JLIBS=-cp ./${LIBS_FLDR}/*:.

.SUFFIXES: .java .class

all: ${OUT_PATH} ${CLASSES}

${OUT_PATH}: ${OUT_PATH}/${LIBS_FLDR} ${OUT_PATH}/${RES_FLDR}
	@mkdir -p ${OUT_PATH}

${OUT_PATH}/${LIBS_FLDR}:
	@cp -r ${SRC_PATH}/${LIBS_FLDR}/ ${OUT_PATH}/

${OUT_PATH}/${RES_FLDR}:
	@cp -r ${SRC_PATH}/${RES_FLDR}/ ${OUT_PATH}/

%.class:
	cd ${SRC_PATH}; $(JC) $(JLIBS) ${JFLAGS} ./${PKG_PATH}/$*.java -d .${OUT_PATH}
	@cp -r ${SRC_PATH}/${LIBS_FLDR}/ ${OUT_PATH}/
	@cp -r ${SRC_PATH}/${RES_FLDR}/ ${OUT_PATH}/

run: all
	cd ${OUT_PATH}; ${JVM} ${JLIBS} ${PKG_PATH}/${MAIN}
