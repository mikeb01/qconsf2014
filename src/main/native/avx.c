#include <immintrin.h>

void sum(double* c, double* a, double* b, int len)
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
