import java.util.ArrayList;
import java.util.List;

public class FourierTransformResult
{
    private List<Double> real, imaginary;

    public FourierTransformResult()
    {
        real = new ArrayList<Double>();
        imaginary = new ArrayList<Double>();
    }

    public void addReal(double value)
    {
        real.add(value);
    }

    public void addImaginary(double value)
    {
        imaginary.add(value);
    }

    public double getReal(int position)
    {
        return real.get(position);
    }

    public double getImaginary(int position)
    {
        return imaginary.get(position);
    }

    public double getMagnitude(int position)
    {
        double both = Math.pow(real.get(position), 2) + Math.pow(imaginary.get(position), 2);
        return Math.sqrt(both);
    }

    public double getPhase(int position) {
        double realPart = real.get(position);
        double imaginaryPart = imaginary.get(position);
        double closeToZero = Math.pow(10, -10);

        if(getMagnitude(position) < closeToZero)
            return 0;

        return Math.atan(imaginaryPart / realPart);
    }
}
