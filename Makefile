all: kk

kk: Driver.java
	javac Driver.java
	chmod 777 kk

clean:
	rm *.class
