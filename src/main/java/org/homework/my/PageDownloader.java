package org.homework.my;

import org.homework.my.cassandra.CassandraClient;
import org.homework.my.cassandra.Slice;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ml636r on 8/19/2017.
 */
public class PageDownloader {
    private CassandraClient client;

    /**
     * constructor
     * @param client C* client
     */
    public PageDownloader(CassandraClient client) {
        this.client=client;
    }

    /**
     * the method downloads and stores the pages
     * @param urls a list of the urls we want to download
     * @throws IOException thrwes an exception incase
     */
    public void downloadAndStorePages(String ... urls) throws IOException {

        for(String address: urls){
           downloadAndStore(address);
        }
    }

    /**
     * the method downloads and stores a single page
     * @param address address of a page to download
     * @throws IOException
     */
    public void downloadAndStore(String address) throws IOException{
        InputStream is = null;
        BufferedInputStream br;
        int size=0;
        int tenkb=1024*10;
        byte [] bytes=new byte[tenkb];
        List<byte []> input=new ArrayList<byte []>();
        try {
            URL  url = new URL(addProtocol(address));
            is = url.openStream();
            br = new BufferedInputStream(is,tenkb);
            while ( ( size = br.read(bytes)) !=-1){
                if(size <= tenkb){
                    bytes= Arrays.copyOfRange(bytes,0,size);
                }
                input.add(bytes);
                bytes=new byte[tenkb];
            }
        } finally {
            if (is != null){
                is.close();
            }
        }
        storePage(input, address);
    }


    /**
     * the method adds a default http protocol to URLS that were provided with out it
     * @param url
     * @return
     */
    private String addProtocol(String url){
        if(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://") ){
            return url;
        }
        else{
            System.out.println("provided url is missing protocol adding: http://"+url);
            return "http://"+url;
        }
    }

    /**
     * the method stores the read bytes according to the urle they were read from.
     * @param input a list of byte arrays
     * @param address the adress where the byte arayes were read.
     */
    private void storePage(List<byte[]> input, String address) {
        System.out.println("deleting all recordes for address:"+address+" if they exist");
        client.delete(address);
        int slice=0;
        System.out.println("Saving slices for:"+address);
        for(byte [] data: input){
            client.save(new Slice(address,slice, data));
            slice++;
        }
        System.out.println("Saving slices for:"+address+" completed sucsesfully slices created:"+slice);
    }

    private void print(List<byte[]> input) {
        StringBuffer sb = new StringBuffer();
        for(byte [] val: input){
            sb.append(new String(val));
        }
        try {
            File output= new File(System.getProperty("user.dir")+File.separator+"output");
            Files.deleteIfExists(output.toPath());
            Files.write(output.toPath(), sb.toString().getBytes(), StandardOpenOption.CREATE);
            System.out.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
