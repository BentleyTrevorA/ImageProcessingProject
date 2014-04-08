import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class Plotter extends ApplicationFrame {

    public Plotter(String title)
    {
        super(title);
    }

    public void makeChart(byte[] bytes)
    {
        XYSeries series = new XYSeries("F(u)");
        for(int i=0; i<bytes.length; i++)
        {
            series.add(i, bytes[i]);
        }

        finishChart(series);
    }

    public void makeChart(double[] bytes)
    {
        XYSeries series = new XYSeries("F(u)");
        for(int i=0; i<bytes.length; i++)
        {
            series.add(i, bytes[i]);
        }

        finishChart(series);
    }

    private void finishChart(XYSeries series)
    {
        XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(getTitle(), "x", "y", data);
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(600, 400));
        setContentPane(panel);
    }
}
