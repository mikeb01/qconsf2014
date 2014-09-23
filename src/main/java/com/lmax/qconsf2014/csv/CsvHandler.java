package com.lmax.qconsf2014.csv;

public interface CsvHandler
{
    default void onStartDocument(String documentIdentifier)
    {
    }

    default void onEndDocument()
    {
    }

    default void onStartRow(int row)
    {
    }

    default void onEndRow(int row)
    {
    }

    default void onValue(int row, int column, CharSequence value)
    {
    }
}
