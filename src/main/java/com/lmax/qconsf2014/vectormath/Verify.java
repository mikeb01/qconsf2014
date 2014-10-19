/*
 * Written by Michael Barker, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 */

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
