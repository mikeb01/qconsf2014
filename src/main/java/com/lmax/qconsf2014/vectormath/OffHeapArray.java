package com.lmax.qconsf2014.vectormath;


import static jnr.ffi.Runtime.getSystemRuntime;

import java.nio.ByteBuffer;

import jnr.ffi.Pointer;

public class OffHeapArray
{
    protected static final long AVX_ALIGN = 256;
    protected final Pointer pointer;
    protected int length;
    protected ByteBuffer buffer;

    public OffHeapArray(int length, int numBytes)
    {
        this.buffer = Buffers.allocateAlignedByteBuffer(length * numBytes, AVX_ALIGN);
        this.length = length;
        this.pointer = Pointer.wrap(getSystemRuntime(), buffer);
    }

    public final int length()
    {
        return length;
    }

    public final Pointer getPointer()
    {
        return pointer;
    }
}