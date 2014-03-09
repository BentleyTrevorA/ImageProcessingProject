/*************************************************************************
 *  Compilation:  javac MyComplex.java
 *  Execution:    java MyComplex
 *
 *  Data type for complex numbers.
 *
 *  The data type is "immutable" so once you create and initialize
 *  a MyComplex object, you cannot change it. The "final" keyword
 *  when declaring re and im enforces this rule, making it a
 *  compile-time error to change the .re or .im fields after
 *  they've been initialized.
 *
 *  % java MyComplex
 *  a            = 5.0 + 6.0i
 *  b            = -3.0 + 4.0i
 *  Re(a)        = 5.0
 *  Im(a)        = 6.0
 *  b + a        = 2.0 + 10.0i
 *  a - b        = 8.0 + 2.0i
 *  a * b        = -39.0 + 2.0i
 *  b * a        = -39.0 + 2.0i
 *  a / b        = 0.36 - 1.52i
 *  (a / b) * b  = 5.0 + 6.0i
 *  conj(a)      = 5.0 - 6.0i
 *  |a|          = 7.810249675906654
 *  tan(a)       = -6.685231390246571E-6 + 1.0000103108981198i
 *
 *************************************************************************/

public class MyComplex {
    private double re;   // the real part
    private double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    public MyComplex(double real, double imag) {
        re = real;
        im = imag;
    }

    // return a string representation of the invoking MyComplex object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    // return abs/modulus/magnitude and angle/phase/argument
    public double magnitude()   { return Math.hypot(re, im); }  // Math.sqrt(re*re + im*im)
    public double phase() { return Math.atan2(im, re); }  // between -pi and pi

    // return a new MyComplex object whose value is (this + b)
    public void plus(MyComplex b) {
        re += b.re;
        im += b.im;
    }

    // return a new MyComplex object whose value is (this - b)
    public void minus(MyComplex b) {
        re -= b.re;
        im -= b.im;
    }

    // return a new MyComplex object whose value is (this * b)
    public void times(MyComplex b) {
        double real = re * b.re - im * b.im;
        double imag = re * b.im + im * b.re;
        re = real;
        im = imag;
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    public void times(double alpha) {
        re *= alpha;
        im *= alpha;
    }

    // return a new MyComplex object whose value is the conjugate of this
    public MyComplex conjugate() {  return new MyComplex(re, -im); }

    // return a new MyComplex object whose value is the reciprocal of this
    public MyComplex reciprocal() {
        double scale = re*re + im*im;
        return new MyComplex(re / scale, -im / scale);
    }

    // return the real or imaginary part
    public double re() { return re; }
    public double im() { return im; }

    // return a / b
    public MyComplex divides(MyComplex b) {
        MyComplex a = this;
        return times(a, b.reciprocal());
    }

    // return a new MyComplex object whose value is the complex exponential of this
    public MyComplex exp() {
        return new MyComplex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // return a new MyComplex object whose value is the complex sine of this
    public MyComplex sin() {
        return new MyComplex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // return a new MyComplex object whose value is the complex cosine of this
    public MyComplex cos() {
        return new MyComplex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // return a new MyComplex object whose value is the complex tangent of this
    public MyComplex tan() {
        return sin().divides(cos());
    }

    // a static version of plus
    public static MyComplex plus(MyComplex a, MyComplex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        MyComplex sum = new MyComplex(real, imag);
        return sum;
    }

    // return a new MyComplex object whose value is (this - b)
    public static MyComplex minus(MyComplex a, MyComplex b) {
        return new MyComplex(a.re - b.re, a.im - b.im);
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    public static MyComplex times(MyComplex a, MyComplex b) {
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new MyComplex(real, imag);
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    public static MyComplex times(MyComplex a, double alpha) {
        return new MyComplex(a.re * alpha, a.im * alpha);
    }
}