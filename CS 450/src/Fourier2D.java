/**
 this clsas provides the methods for the 2D-Fourier transformation
 http://www.cs.virginia.edu/~wm2a/applets/Transformation/
 */

public class Fourier2D {

    /** size of matrix in M-direction */
    private int M_2P;

    /** size of matrix in N-direction  */
    private int N_2P;

    /** largest exponent, so that 2^maxPotM <= (M_2P-1) */
    private int maxPotM;

    /** largest exponent, so that 2^maxPotN <= (N_2P-1) */
    private int maxPotN;


    /** constructor for the use of the 2D transformation routines */
    public Fourier2D(int M, int N) {
        int M_tmp, N_tmp;

        // check for power of 2
        M_tmp = M;
        N_tmp = N;
        while( ((M_tmp >>= 1) != 0) && ((N_tmp >>= 1) != 0) ) {
            if( ((M_tmp & 0x1) == 0x1) && (M_tmp>>1 != 0) &&
                    ((N_tmp & 0x1) == 0x1) && (N_tmp>>1 != 0) ) {
                System.out.println("Fourier: both dimensions must be powers of 2");
                System.exit(1);
            }
        }
        M_2P = M;
        N_2P = N;
        maxPotM=0;
        while ( (1<<maxPotM) <= (M_2P-1) ) {
            maxPotM++;
        }
        maxPotM--;
        maxPotN=0;
        while ( (1<<maxPotN) <= (N_2P-1) ) {
            maxPotN++;
        }
        maxPotN--;
    }


    /** genearl 2D-DFT */
    public Complex2[][] dft (Complex2[][] G) {

        Complex2[][] GF;
        Complex2 gfz;

        GF = new Complex2[M_2P][N_2P];
        gfz = new Complex2();
        for (int u = 0; u < M_2P; u++) {
            for (int v = 0; v < N_2P; v++) {
                GF[u][v] = new Complex2();
                for (int m = 0; m < M_2P; m++) {
                    gfz.real = 0.0; gfz.imag = 0.0;
                    for (int n = 0; n < N_2P; n++) {
                        gfz.add(Complex2.mult(G[m][n],
                                Complex2.expi(-2.0*Math.PI*n*v/N_2P)));
                    }
                    (GF[u][v]).add(gfz.mult(Complex2.expi(-2.0*Math.PI*m*u/M_2P)));
                }
                (GF[u][v]).mult(1.0/(M_2P*N_2P));
            }
        }
        return GF;
    }


    /** general 2D-IDFT */
    public Complex2[][] idft (Complex2[][] GF) {

        Complex2[][] G;
        Complex2 gz;

        G = new Complex2[M_2P][N_2P];
        gz = new Complex2();
        for (int m = 0; m < M_2P; m++) {
            for (int n = 0; n < N_2P; n++) {
                G[m][n] = new Complex2();
                for (int u = 0; u < M_2P; u++) {
                    gz.real = 0.0; gz.imag = 0.0;
                    for (int v = 0; v < N_2P; v++) {
                        gz.add(Complex2.mult(GF[u][v], Complex2.expi(2.0*Math.PI*n*v/N_2P)));
                    }
                    (G[m][n]).add(gz.mult(Complex2.expi(2.0*Math.PI*m*u/M_2P)));
                }
            }
        }
        return G;
    }


    /** 2D-FFT, based on 1D FFT */
    public Complex2[][] fft (Complex2[][] G) {

        Complex2[][] GF;
        Complex2[] v,vf;
        Fourier1D fft1D;

        GF = new Complex2[M_2P][N_2P];

        // transform first dimension
        fft1D = new Fourier1D(N_2P);
        v = new Complex2[N_2P];
        for (int m = 0; m < M_2P; m++) {
            for (int n = 0; n < N_2P; n++)
                v[n] = new Complex2(G[m][n]);
            vf = fft1D.fft(v);
            for (int n = 0; n < N_2P; n++)
                GF[m][n] = new Complex2(vf[n]);
        }

        // transform second dimension
        fft1D = new Fourier1D(M_2P);
        v = new Complex2[M_2P];
        for (int n = 0; n < N_2P; n++) {
            for (int m = 0; m < M_2P; m++)
                v[m] = new Complex2(GF[m][n]);
            vf = fft1D.fft(v);
            for (int m = 0; m < M_2P; m++)
                GF[m][n] = new Complex2(vf[m]);
        }

        return GF;
    }


    /** 2D-IFFT, based on 1D IFFT */
    public Complex2[][] ifft (Complex2[][] G) {

        Complex2[][] GF;
        Complex2[] v,vf;
        Fourier1D ifft1D;

        GF = new Complex2[M_2P][N_2P];

        // transform first dimension
        ifft1D = new Fourier1D(N_2P);
        v = new Complex2[N_2P];
        for (int m = 0; m < M_2P; m++) {
            for (int n = 0; n < N_2P; n++)
                v[n] = new Complex2(G[m][n]);
            vf = ifft1D.ifft(v);
            for (int n = 0; n < N_2P; n++)
                GF[m][n] = new Complex2(vf[n]);
        }

        // transform second dimension
        ifft1D = new Fourier1D(M_2P);
        v = new Complex2[M_2P];
        for (int n = 0; n < N_2P; n++) {
            for (int m = 0; m < M_2P; m++)
                v[m] = new Complex2(GF[m][n]);
            vf = ifft1D.ifft(v);
            for (int m = 0; m < M_2P; m++)
                GF[m][n] = new Complex2(vf[m]);
        }

        return GF;
    }
}