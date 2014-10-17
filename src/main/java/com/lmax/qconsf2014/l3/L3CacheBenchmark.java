package com.lmax.qconsf2014.l3;

import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

public class L3CacheBenchmark
{
    @State(Scope.Thread)
    public static class ThreadData
    {
        long[] values = new long[1 << 19];
        int[] indexes = new int[values.length];
        
        public ThreadData()
        {
            for (int i = 0; i < values.length; i++)
            {
                values[i] = i;
                indexes[i] = i;
            }
            
            shuffleArray(indexes);
        }
    }
    
    @Benchmark
    public void processLongs(Blackhole bh, ThreadData data)
    {
        for (int i : data.indexes)
        {
            bh.consume(data.values[i]);
        }
    }

    private static void shuffleArray(int[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
