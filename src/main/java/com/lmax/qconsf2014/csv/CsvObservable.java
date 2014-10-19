/*
 * Written by Michael Barker, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 */

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
