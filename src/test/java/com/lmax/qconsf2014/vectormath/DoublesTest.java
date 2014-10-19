/*
 * Written by Michael Barker, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.lmax.qconsf2014.vectormath;

import static com.lmax.qconsf2014.vectormath.Verify.checkArgs;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Test;

public class DoublesTest
{
    Random r = new Random(1);
    int size = 8;
    
    double[] aArr = new double[size];
    double[] bArr = new double[size];
    double[] cArr = new double[size];
    double[] dArr = new double[size];
    
    DoubleArray aBuf = new DoubleArray(size);
    DoubleArray bBuf = new DoubleArray(size);
    DoubleArray cBuf = new DoubleArray(size);
    
    public void setUp()
    {        
        fillWithRandoms(r, aArr, aBuf);
        fillWithRandoms(r, bArr, bBuf);
        
        cBuf.fill(0.0);
    }
    
    private static void fillWithRandoms(Random r, double[] ds, DoubleArray dArr)
    {
        checkArgs(ds.length == dArr.length());
        
        for (int i = 0; i < ds.length; i++)
        {
            double d = r.nextDouble();
            
            ds[i] = d;
            dArr.set(i, d);
        }
    }
    
    @Test
    public void shouldSumAvx() throws Exception
    {

        Doubles.sum(cArr, bArr, aArr);
        VectorMath.sumAvx(cBuf, aBuf, bBuf);
        
        for (int i = 0; i < size; i++)
        {
            assertThat("" + i, cBuf.get(i), is(cArr[i]));
        }
    }

    @Test
    public void shouldSumSimple() throws Exception
    {

        Doubles.sum(cArr, bArr, aArr);
        VectorMath.sumSimple(cBuf, aBuf, bBuf);
        
        for (int i = 0; i < size; i++)
        {
            assertThat("" + i, cBuf.get(i), is(cArr[i]));
        }
    }

    @Test
    public void shouldSumArrayOfSize7() throws Exception
    {
        int size = 7;
        
        double[] aArr = new double[size];
        double[] bArr = new double[size];
        double[] cArr = new double[size];
        
        DoubleArray aBuf = new DoubleArray(size);
        DoubleArray bBuf = new DoubleArray(size);
        DoubleArray cBuf = new DoubleArray(size);
        
        fillWithRandoms(r, aArr, aBuf);
        fillWithRandoms(r, bArr, bBuf);
        cBuf.fill(0.0);
        
        Doubles.sum(cArr, bArr, aArr);
        VectorMath.sumAvx(cBuf, aBuf, bBuf);
        
        for (int i = 0; i < size; i++)
        {
            assertThat("" + i, cBuf.get(i), is(cArr[i]));
        }
    }
    
    @Test
    public void shouldSumUnsafeArray() throws Exception
    {
        Doubles.sum(cArr, bArr, aArr);
        Doubles.unsafeSum(dArr, bArr, aArr);
        
        for (int i = 0; i < size; i++)
        {
            assertThat("" + i, dArr[i], is(cArr[i]));
        }
    }    
}
