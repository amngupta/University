all: DNSlookup.jar run
DNSlookup.jar:
	javac -d ./ ./src/dnsClient/*.java
	jar cmvf MANIFEST.MF DNSlookup.jar ./dnsClient/

run:
	java -jar DNSlookup.jar 199.7.83.42 www.cs.ubc.ca -t

clean:
	rm -Rf ./dnsClient/
	rm -f ./DNSlookup.jar