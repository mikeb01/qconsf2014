package com.lmax.qconsf2014.vectormath;


import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import sun.misc.Unsafe;

public class Buffers
{
    private static final Unsafe THE_UNSAFE;
    static
    {
        try
        {
            final PrivilegedExceptionAction<Unsafe> action = new PrivilegedExceptionAction<Unsafe>()
            {
                public Unsafe run() throws Exception
                {
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                }
            };

            THE_UNSAFE = AccessController.doPrivileged(action);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }
    
    public static Unsafe getUnsafe()
    {
        return THE_UNSAFE;
    }
    
    public static long getAddressForBuffer(ByteBuffer bb)
    {
        if (bb.isDirect())
        {
            return ((sun.nio.ch.DirectBuffer) bb).address();
        }
        
        throw new IllegalArgumentException("Only direct byte buffers supported");
    }
    
    /**
     * Allocate an aligned buffer
     * 
     * @param capacity Size in bytes
     * @param align Alignment in bits
     */
    public static ByteBuffer allocateAlignedByteBuffer(int capacity, long align)
    {
        if (Long.bitCount(align) != 1)
        {
            throw new IllegalArgumentException("Alignment must be a power of 2");
        }
        ByteBuffer buffy = ByteBuffer.allocateDirect((int)(capacity + align));
        long address = Buffers.getAddressForBuffer(buffy);
        if ((address & (align - 1)) == 0)
        {
            buffy.limit(capacity);
            return buffy.slice().order(ByteOrder.nativeOrder());
        }
        else
        {
            int newPosition = (int)(align - (address & (align - 1)));
            buffy.position(newPosition);
            int newLimit = newPosition + capacity;
            buffy.limit(newLimit);
            return buffy.slice().order(ByteOrder.nativeOrder());
        }
    }
}
