package com.company;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    //process_log("C:\\Users\\chaos\\cs122b\\test\\logs\\log1.txt");
        process_logs("C:\\Users\\chaos\\cs122b\\test\\cs122b-spring21-team-87\\log_processing\\log-1.txt", "C:\\Users\\chaos\\cs122b\\test\\cs122b-spring21-team-87\\log_processing\\log-2.txt");
    }

    public static void process_log(String filename1)
    {
        try
        {
            Scanner scan = new Scanner(new File(filename1));
            long TSsum = 0;
            long TJsum = 0;
            long count = 0;
            while(scan.hasNextLine())
            {
                String temp = scan.nextLine();
                Scanner s = new Scanner(temp);
                TSsum += s.nextInt();
                TJsum += s.nextInt();
                count++;
                System.out.println(TSsum +" "+TJsum);
            }
            System.out.println(count);
            System.out.println("TS: "+(double)TSsum/count);
            System.out.println("TJ: "+(double)TJsum/count);
            scan.close();
        }
        catch(IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void process_logs(String filename1, String filename2)
    {
        try
        {
            Scanner scan1 = new Scanner(new File(filename1));
            long TSsum = 0;
            long TJsum = 0;
            long count = 0;
            while(scan1.hasNextLine())
            {
                String temp = scan1.nextLine();
                Scanner s = new Scanner(temp);
                TSsum += s.nextInt();
                TJsum += s.nextInt();
                count++;
                System.out.println(TSsum +" "+TJsum);
            }

            Scanner scan2 = new Scanner(new File(filename2));
            while(scan2.hasNextLine())
            {
                String temp = scan2.nextLine();
                Scanner s = new Scanner(temp);
                TSsum += s.nextInt();
                TJsum += s.nextInt();
                count++;
                System.out.println(TSsum +" "+TJsum);
            }

            System.out.println(count);
            System.out.println("TS: "+(double)TSsum/count);
            System.out.println("TJ: "+(double)TJsum/count);
            scan1.close();
            scan2.close();
        }
        catch(IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
