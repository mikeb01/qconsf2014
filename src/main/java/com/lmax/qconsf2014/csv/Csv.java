package com.lmax.qconsf2014.csv;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Csv
{
    static InputStream load()
    {
        String filename = "data.csv";
        InputStream in;
        try
        {
            in = new BufferedInputStream(new FileInputStream(filename));
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("Run 'gunzip *.gz' before running this test");
        }
        return in;
    }
}
