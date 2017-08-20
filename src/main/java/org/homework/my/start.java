package org.homework.my;

import org.homework.my.cassandra.CassandraClient;

import java.util.Arrays;

/**
 * Created by ml636r on 8/12/2017.
 */
public class start {

    public static void main(String [] argv){

        if(argv==null || argv.length==0){
            printOnError("error: invalid number of arguments.");
            System.exit(1);
        }

        CassandraClient client=null;
        try {
            client = getCassandraClient(argv, client);

            String option = argv[2];

            switch (option){
                case "download":
                    if(argv.length>3){
                        client.createMapping();
                        PageDownloader pagedownloader = new PageDownloader(client);
                        pagedownloader.downloadAndStorePages(Arrays.copyOfRange(argv,3,argv.length));
                    }
                    else{
                        printOnError("error: invalid number of arguments");
                    }
                    break;
                case "readFull":
                    if(argv.length==4){
                        client.createMapping();
                        ContentReader content = new ContentReader(client);
                        System.out.println(content.getPage(argv[3]));
                    }
                    else{
                        printOnError("error: invalid number of arguments");
                    }
                    break;
                case "readSlice":
                    if(argv.length==5){
                        client.createMapping();
                        ContentReader content = new ContentReader(client);
                        System.out.println(Arrays.toString(content.getPageSlice(argv[3],Integer.parseInt(argv[4]))));
                    }
                    else{
                        printOnError("error: invalid number of arguments");
                    }
                    break;
                case "clear":
                    client.clearAll();
                    break;
                default:
                    printOnError("error: the given paremeters did not match please review the usage.");
                    break;
            }

        }
        catch (Exception e){
            System.out.println("exception thrown.");
            e.printStackTrace();
        }
        finally {
            if(client!=null){
                client.close();
            }
        }
    }

    private static CassandraClient getCassandraClient(String[] argv, CassandraClient client) {
        String ip = argv[0];
        int port = Integer.parseInt(argv[1]);
        client = new CassandraClient(ip, port);
        client.connect();
        client.createSchema();
        return client;
    }


    public static void printOnError(String massege){
        System.out.println(massege);
        usage();

    }


    public static void usage(){
        System.out.println("Aplication Usege");
        System.out.println("1)download from web sites:");
        System.out.println("    usage: <cassandra ip> <cassandra port> download <urls seperated by space>");
        System.out.println("2)read whole site data from DB:");
        System.out.println("    usage: <cassandra ip> <cassandra port> readFull <site url>");
        System.out.println("3)read site slice data from DB:");
        System.out.println("    usage: <cassandra ip> <cassandra port> readSlice <site url> <slice number>");
        System.out.println("3)clear DB:");
        System.out.println("    usage: <cassandra ip> <cassandra port> clear <site url> <slice number>");
    }
}
