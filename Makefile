access.log:
	python gen-sample.py

target/access-log-parser-1.0-SNAPSHOT-jar-with-dependencies.jar:
	mvn package

test: access.log target/access-log-parser-1.0-SNAPSHOT-jar-with-dependencies.jar
	java -jar target/access-log-parser-1.0-SNAPSHOT-jar-with-dependencies.jar --enable-preview < access.log

clean:
	rm access.log
	rm -r target/
