JC = javac
JV = java
MAIN = Main

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
        MIPSModel.java \
        MIPSView.java \
        MIPSController.java \
        Main.java

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	$(JV) $(MAIN) $(file) $(step)

clean:
	$(RM) *.class
