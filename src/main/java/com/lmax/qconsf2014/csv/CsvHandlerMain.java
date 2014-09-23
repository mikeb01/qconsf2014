package com.lmax.qconsf2014.csv;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javolution.text.TypeFormat;

import org.openjdk.jmh.annotations.Benchmark;

public class CsvHandlerMain
{
    @Benchmark
    public double main() throws IOException
    {
        String filename = "2014-07-21-21-00-00-000-4001-bid-ask-tick-depth1.csv";
        InputStream in = new BufferedInputStream(new FileInputStream(filename));
        
        double[] avg = { 0.0 };
        
        CsvHandler handler = new CsvHandler()
        {
            double bid = 0;
            double ask = 0;
            double spreadTotal = 0;
            long spreadCount = 0;
            
            @Override
            public void onValue(int row, int column, CharSequence value)
            {
                try
                {
                    if (row == 0 || value.length() < 1)
                    {
                        return;
                    }
                    
                    switch (column)
                    {
                    case 1:
                        bid = TypeFormat.parseDouble(value);
                        break;
                    case 3:
                        ask = TypeFormat.parseDouble(value);
                        break;
                    }
                }
                catch (NumberFormatException e)
                {
                    System.err.printf("%d, %d, %s%n", row, column, value);
                    throw e;
                }
            }
            
            @Override
            public void onEndRow(int row)
            {
                if (bid > 0 && ask > 0)
                {
                    spreadTotal = (ask - bid);
                    spreadCount++;
                }
            }
            
            @Override
            public void onEndDocument()
            {
                avg[0] = spreadTotal / spreadCount;
            }
        };
        CsvParser.parse(filename, in, handler);        
        
        return avg[0];
    }
}
