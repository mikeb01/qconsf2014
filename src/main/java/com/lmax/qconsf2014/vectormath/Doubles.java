package com.lmax.qconsf2014.vectormath;

import static com.lmax.qconsf2014.vectormath.Verify.checkArgs;
import sun.misc.Unsafe;


public class Doubles 
{
    private static final Unsafe UNSAFE = Buffers.getUnsafe();
    private static final long BASE_OFFSET = UNSAFE.arrayBaseOffset(double[].class);
    private static final long INDEX_SCALE = UNSAFE.arrayIndexScale(double[].class);
    
    public static void sum(
        double[] sum, 
        double[] addendA, 
        double[] addendB)
    {
        checkArgs(sum.length == addendA.length && 
            sum.length == addendB.length);
        
        for (int i = 0; i < 0 + sum.length; i++)
        {
            sum[i] = addendA[i] + addendB[i];
        }
    }
    
    public static void unsafeSum(double[] sum, double[] addendA, double[] addendB)
    {
        checkArgs(sum.length == addendA.length && sum.length == addendB.length);
        
        for (int i = 0; i < 0 + sum.length; i++)
        {
            long offset = i * INDEX_SCALE;
            UNSAFE.putDouble(sum, BASE_OFFSET + offset,
                UNSAFE.getDouble(addendA, BASE_OFFSET + offset) + 
                UNSAFE.getDouble(addendB, BASE_OFFSET + offset));
        }
    }
}
