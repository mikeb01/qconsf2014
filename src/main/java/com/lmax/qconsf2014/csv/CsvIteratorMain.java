package com.lmax.qconsf2014.csv;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

import javolution.text.TypeFormat;

import org.openjdk.jmh.annotations.Benchmark;

public class CsvIteratorMain
{
    public static void print(String[] ss)
    {
        System.out.println(Arrays.toString(ss));
    }
    
    @Benchmark
    public double main() throws IOException
    {
        String filename = "2014-07-21-21-00-00-000-4001-bid-ask-tick-depth1.csv";
        InputStream in;
        try
        {
            in = new BufferedInputStream(new FileInputStream(filename));
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("Run 'gunzip *.gz' before running this test");
        }
        
        Iterable<String[]> parseAsIterable = CsvParser.parseAsIterable(filename, in);
        
        double spreadTotal = 0.0;
        long spreadCount = 0;
        
        Iterator<String[]> iterator = parseAsIterable.iterator();
        iterator.next(); // Skip header.
        
        while (iterator.hasNext())
        {
            String[] strings = iterator.next();
            
            if (strings[1].isEmpty() || strings[3].isEmpty())
            {
                continue;
            }
            
            try
            {
                double bid = TypeFormat.parseDouble(strings[1]);
                double ask = TypeFormat.parseDouble(strings[3]);
                
                spreadTotal += (ask - bid);
                spreadCount++;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return spreadTotal / spreadCount;
    }
}
