# ContentToCassandra

the aplication reads a list of urls from the commend line and downloades them by 10kb slices to cassandra.

pre requisite for the aplication:
- Java 1.8
- Maven
- C* tested on version 3.9.0 on windows

to exacute the aplication run:

    mvn clean install

undrt the target dir you will find the jar with depandencys (needed becouse i am using the DS driver).

to exacute the aplication run

    java -jar ContentToCassandra-1.0-SNAPSHOT-jar-with-dependencies.jar 

### aplication options:

#### download from web sites:
    <cassandra ip> <cassandra port> download <urls seperated by space>
#### read whole site data from DB:
    <cassandra ip> <cassandra port> readFull <site url>
#### read site slice data from DB:
    <cassandra ip> <cassandra port> readSlice <site url> <slice number>
#### clear DB:
    <cassandra ip> <cassandra port> clear <site url> <slice number>
