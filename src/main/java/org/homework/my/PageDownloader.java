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

    public List<byte[]> DownloadAndStorePages(String ... urls) throws IOException {
        URL url;
        InputStream is = null;
        BufferedInputStream br;
        String line;
        int size=0;
        int tenkb=1024*10;
        byte [] bytes=new byte[tenkb];
        List<byte []> input=new ArrayList<byte []>();
        for(String address: urls){
            try {

                //temp
                File output= new File(System.getProperty("user.dir")+File.separator+"output");
                Files.deleteIfExists(output.toPath());
                URL oracle = new URL(addProtocol(address));
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    Files.write(output.toPath(), inputLine.getBytes(), StandardOpenOption.CREATE,StandardOpenOption.APPEND);
                }
                in.close();


                //temp end
                url = new URL(addProtocol(address));
                is = url.openStream();  // throws an IOException
                br = new BufferedInputStream(is,tenkb);
                while ( ( size = br.read(bytes)) !=-1){
//                    if(size <= tenkb){
//                        bytes= Arrays.copyOfRange(bytes,0,size);
//                    }
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
        return input;
    }

    private String addProtocol(String url){
        if(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://") ){
            return url;
        }
        else{
            System.out.println("provided url is missing protocol adding: http://"+url);
            return "http://"+url;
        }
    }

    private void storePage(List<byte[]> input, String address) {
        int slice=0;
        for(byte [] data: input){
            client.save(new Slice(address,slice, data));
            slice++;
        }
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
