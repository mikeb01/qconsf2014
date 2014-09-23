package com.lmax.qconsf2014.vectormath;


import java.nio.DoubleBuffer;

public class DoubleArray extends OffHeapArray
{
    private static final int BYTES_PER_DOUBLE = 8;
    private final DoubleBuffer data;
    
    public DoubleArray(int length)
    {
        super(length, BYTES_PER_DOUBLE);
        this.data = buffer.asDoubleBuffer();
    }
    
    public double get(int index)
    {
        return data.get(index);
    }
    
    public DoubleArray set(int index, double value)
    {
        data.put(index, value);
        return this;
    }
    
    public static DoubleArray of(double[] values)
    {
        DoubleArray array = new DoubleArray(values.length);
        array.data.put(values);
        return array;
    }

    public void fill(double d)
    {
        for (int i = 0; i < length; i++)
        {
            set(i, d);
        }
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("[");
        for (int i = 0; i < length; i++)
        {
            sb.append(get(i));
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append("]");
        
        return sb.toString();
    }
}
