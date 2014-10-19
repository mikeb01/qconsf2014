/*
 * Written by Michael Barker, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.lmax.qconsf2014.csv;

enum State
{
    START_ROW, START_FIELD, TEXT, QUOTED_TEXT, POSSIBLE_DQUOTE
}