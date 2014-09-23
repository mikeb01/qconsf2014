package com.lmax.qconsf2014.vectormath;


import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class ArraySumBenchmark
{
    @Param({"2048"}) int size;
    
    private double[] a;
    private double[] b;
    private double[] c;
    
    private DoubleArray aBuf;
    private DoubleArray bBuf;
    private DoubleArray cBuf;
    
    private Random r = new Random(89787234);
    
    @Setup
    public void setUp() throws Exception
    {
        a = new double[size];
        b = new double[size];
        c = new double[size];
        
        aBuf = new DoubleArray(size);
        bBuf = new DoubleArray(size);
        cBuf = new DoubleArray(size);
        
        for (int j = 0; j < size; j++)
        {                
            double aVal = r.nextDouble();
        	double bVal = r.nextDouble();
        	
            a[j] = aVal;
        	b[j] = bVal;
        	
        	aBuf.set(j, aVal);
        	aBuf.set(j, bVal);
        }
    }
    
    @Benchmark
    public void timeSumJava(Blackhole blackhole)
    {
		Doubles.sum(c, a, b);
    	blackhole.consume(c);
    }
    
    @Benchmark
    public void timeUnsafeSumJava(Blackhole blackhole)
    {
        Doubles.unsafeSum(c, a, b);
        blackhole.consume(c);
    }
    
    @Benchmark
    public void timeSumAvx(Blackhole blackhole)
    {
        VectorMath.sum(cBuf, aBuf, bBuf);
        blackhole.consume(c);
    }
    
    
}
