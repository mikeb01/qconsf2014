/*
 * Written by Michael Barker, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 */

#include <immintrin.h>

void sum_avx(double* c, double* a, double* b, int len)
{
    __m256d rA_AVX, rB_AVX, rC_AVX;   // variables for AVX

    for (int i = 0; i < len; i += 4)
    {
        rA_AVX = _mm256_load_pd(&a[i]);
        rB_AVX = _mm256_load_pd(&b[i]);
        rC_AVX = _mm256_add_pd(rA_AVX, rB_AVX);
        _mm256_store_pd(&c[i], rC_AVX);
    }
}

void sum_simple(double* c, double* a, double* b, int len)
{
    for (int i = 0; i < len; i++)
    {
    	c[i] = a[i] + b[i];
    }
}
