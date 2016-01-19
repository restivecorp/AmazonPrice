# AmazonPrice
Monitor prices amazon wish lists it's a project that can track wish lists Amazon and notifies you when any of the products changes price.

## How to use?

1. Download code
2. Import into eclipse IDE
3. Create a script sql with wishlist in db/apricedb.script. For example: INSERT INTO DESEO VALUES('http://www.amazon.es/registry/wishlist/XXXXXXXXXXXX')
4. Compiles and generates a .jar
5. Create a directory (for example C:/APT) with this files:
	+ db (contais db script)
	+ log (contais log file)
	- apt.jar
	- tags.properties
6. Edit tags.properties (channel and pushetta Token)
7. Execute jar: java -jar apt.jar

You can schedule a task to be ejeecute automatically on your PC if you wish. 
The program will run and notify you when the product gets changes.
