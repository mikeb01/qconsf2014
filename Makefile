all:
	mkdir -p target/native
	gcc -o target/native/avx.os -c -std=gnu99 -march=native -O3 -fPIC src/main/native/avx.c
	gcc -o target/native/libavx.so -shared target/native/avx.os

clean:
	rm -rf target/native