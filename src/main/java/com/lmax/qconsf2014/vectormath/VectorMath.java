package com.lmax.qconsf2014.vectormath;


import static com.lmax.qconsf2014.vectormath.Verify.checkArgs;
import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;

public class VectorMath
{    
    public interface Avx
    {
        void sum_avx(Pointer c, Pointer a, Pointer b, int len);
        void sum_simple(Pointer c, Pointer a, Pointer b, int len);
    }
    
    private static final Avx AVX;
    
    static
    {
        AVX = LibraryLoader.create(Avx.class).load("target/native/libavx.so");
    }

    public static void sumAvx(OffHeapArray c, OffHeapArray a, OffHeapArray b)
    {
        checkArgs(c.length() == a.length() && c.length() == b.length());
        
        AVX.sum_avx(c.getPointer(), a.getPointer(), b.getPointer(), a.length());
    }

    public static void sumSimple(OffHeapArray c, OffHeapArray a, OffHeapArray b)
    {
        checkArgs(c.length() == a.length() && c.length() == b.length());
        
        AVX.sum_simple(c.getPointer(), a.getPointer(), b.getPointer(), a.length());
    }
}
