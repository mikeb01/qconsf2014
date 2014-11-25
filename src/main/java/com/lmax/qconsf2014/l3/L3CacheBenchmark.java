/*
 * Written by Michael Barker, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.lmax.qconsf2014.l3;

import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class L3CacheBenchmark
{
    @State(Scope.Benchmark)
    public static class P
    {
        @Param({
             "1",  "2",  "3",  "4",  "5",  "6",  "7",  "8",  "9", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
        })
        public int size;
    }

    @State(Scope.Thread)
    public static class ThreadData
    {
        int[] values;
        int[] indexes;

        @Setup
        public void setup(P p)
        {
            values = new int[1 << p.size];
            indexes = new int[values.length];

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
        for (final int i : data.indexes)
        {
            bh.consume(data.values[i]);
        }
    }

    private static void shuffleArray(int[] ar)
    {
        final Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            final int index = rnd.nextInt(i + 1);
            // Simple swap
            final int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static void main(String[] args) throws RunnerException
    {
        final Options opt = new OptionsBuilder().include(L3CacheBenchmark.class.getSimpleName()).build();
        new Runner(opt).run();
    }
}
