package com.lmax.qconsf2014.csv;

enum State
{
    START_ROW, START_FIELD, TEXT, QUOTED_TEXT, POSSIBLE_DQUOTE
}