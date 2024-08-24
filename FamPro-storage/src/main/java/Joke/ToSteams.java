package Joke;

import java.io.*;
import java.util.Arrays;

public class ToSteams {


    public static void main(String[] args) throws UnsupportedEncodingException {
        char character = 'Ы';
        byte[] arr= String.valueOf(character).getBytes("utf-8");
        int i1=arr[0]+256;
        int i2=arr[1]+256;
        System.out.format("%d,%d",i1,i2);
        System.out.println(Arrays.toString(arr));
        try ( InputStream is = new ByteArrayInputStream(arr)) {
            int b= (is.read());
            while (b!=-1){
            System.out.println(b);
            b=  (is.read());
            }
        } catch (IOException e){
            System.out.println("opps");
        }
          }
}
