package com.lmax.qconsf2014.vectormath;

public class Verify
{
    static void checkArgs(boolean b)
    {
        if (!b)
        {
            throw new IllegalArgumentException();
        }
    }
}
