/*
 * Written by Michael Barker, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.lmax.qconsf2014.csv;

import java.io.IOException;
import java.io.InputStream;

import javolution.text.TypeFormat;

import org.openjdk.jmh.annotations.Benchmark;

public class CsvObservableBenchmark
{
    @Benchmark
    public double main() throws IOException
    {
        InputStream in = Csv.load();
        
        double[] avg = { 0.0 };
        
        CsvObservable<CharSequence> handler = new CsvObservable<CharSequence>()
        {
            double bid = Double.NaN;
            double ask = 0;
            double spreadTotal = 0;
            long spreadCount = 0;
            
            @Override
            public void onEvent(
                int row, int column, CharSequence value, boolean endOfLine)
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
                    if (!Double.isNaN(bid))
                    {                            
                        ask = TypeFormat.parseDouble(value);
                        spreadTotal = (ask - bid);
                        spreadCount++;
                    }
                    bid = Double.NaN;
                    break;
                }
            }
            
            @Override
            public void onComplete()
            {
                avg[0] = spreadTotal / spreadCount;
            }
        };
        CsvParser.parse(in, handler);        
        
        return avg[0];
    }
}
