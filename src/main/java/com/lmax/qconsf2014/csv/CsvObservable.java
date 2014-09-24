package com.lmax.qconsf2014.csv;

public interface CsvObservable<T>
{
    default void onError(Exception e)
    {
    }

    default void onComplete()
    {
    }

    default void onEvent(int row, int column, T value, boolean endOfLine)
    {
    }
}
