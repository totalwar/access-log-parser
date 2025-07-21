JAVA_PROPS = --enable-preview --add-modules jdk.incubator.vector
JAR = target/access-log-parser-1.0-SNAPSHOT-jar-with-dependencies.jar

access.log:
	python gen-sample.py

target/access-log-parser-1.0-SNAPSHOT-jar-with-dependencies.jar:
	mvn package

test: access.log $(JAR)
	cat access.log | java $(JAVA_PROPS) -jar $(JAR) -u 99.9 -t 45 < access.log

clean:
	rm access.log
	rm -r target/
