package com.lmax.qconsf2014.csv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

import javolution.text.TypeFormat;

import org.openjdk.jmh.annotations.Benchmark;

public class CsvIteratorBenchmark
{
    public static void print(String[] ss)
    {
        System.out.println(Arrays.toString(ss));
    }
    
    @Benchmark
    public double main() throws IOException
    {
        InputStream in = Csv.load();
        
        Iterable<String[]> parseAsIterable = CsvParser.parseAsIterable(in);
        
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
            
            double bid = TypeFormat.parseDouble(strings[1]);
            double ask = TypeFormat.parseDouble(strings[3]);
            
            spreadTotal += (ask - bid);
            spreadCount++;
        }
        
        return spreadTotal / spreadCount;
    }
}
