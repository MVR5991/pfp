package oldWOrk;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import static javax.swing.SwingUtilities.*;

/**
 * oldWOrk.A thread-safe pixel-based screen.
 *
 * @author Marius Kamp <marius.kamp@fau.de>
 */
public class PainterScreen implements Screen {

	private Double[][] array;
	private int counter;
	private Runnable repaint;
	private double minValue = -10;
	private double maxValue = 10;
	


	/**
	 * Creates a new instance of oldWOrk.PainterScreen.
	 *
	 * @param width
	 * 		The number of colored elements in x-direction
	 * @param height
	 * 		The number of colored elements in y-direction
	 */
	public PainterScreen(final int width, final int height) {
		this(width, height, -10, 10);
	}



	/**
	 * Creates a new instance of oldWOrk.PainterScreen.
	 *
	 * @param width
	 * 		The number of colored elements in x-direction
	 * @param height
	 * 		The number of colored elements in y-direction
	 * @param minValue
	 * 		The minimum value. Smaller values will be clamped.
	 * @param maxValue
	 * 		The maximum value. Larger values will be clamped.
	 */
	public PainterScreen(final int width, final int height, final double minValue,
			final double maxValue) {
		assert width > 0;
		assert height > 0;

		this.minValue = minValue;
		this.maxValue = maxValue;
		this.counter = width * height;
		this.array = new Double[height][width];

		try {
			invokeAndWait(new Runnable() {
				public void run() {
					final JFrame mainFrame = new JFrame();

					final JPanel colorPanel = new JPanel() {
						private static final long serialVersionUID = 1L;

						@Override
						protected void paintComponent(Graphics g) {
							super.paintComponent(g);

							final double xFac = (double)getSize().width / width;
							final double yFac = (double)getSize().height / height;
							for (int y = 0; y < height; ++y) {
								for (int x = 0; x < width; ++x) {
									boolean unset = true;
									synchronized (PainterScreen.this) {
										unset = array[y][x] == null;
										if (!unset) {
											g.setColor(mapValue(array[y][x]));
										}
									}

									if (!unset) {
										g.fillRect(
											(int)(x * xFac),
											(int)(y * yFac),
											(int)Math.ceil(xFac),
											(int)Math.ceil(yFac)
										);
									} else {
										g.setColor(Color.GRAY);
										g.fillRect(
											(int)(x * xFac),
											(int)(y * yFac),
											(int)Math.ceil(xFac),
											(int)Math.ceil(yFac)
										);
										g.setColor(Color.WHITE);
										g.drawLine(
											(int)(x * xFac),
											(int)(y * yFac),
											(int)((x + 1) * xFac) - 1,
											(int)((y + 1) * yFac) - 1
										);
										g.drawLine(
											(int)(x * xFac),
											(int)((y + 1) * yFac) - 1,
											(int)((x + 1) * xFac) - 1,
											(int)(y * yFac)
										);
									}
								}
							}
						}
					};

					colorPanel.setPreferredSize(new Dimension(
						10 * width, 10 * height
					));

					mainFrame.setContentPane(colorPanel);
					mainFrame.setTitle("oldWOrk.FuncPainter");
					mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					mainFrame.pack();
					mainFrame.setLocationRelativeTo(null);
					mainFrame.setVisible(true);

					repaint = new Runnable() {
						public void run() {
							mainFrame.repaint();
						}
					};
				}
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}



	private final Color mapValue(final Double value) {
		return Color.getHSBColor(
			(float)Math.min(
				Math.max(
					(value.doubleValue() - minValue) / (maxValue - minValue) - 0.07, 0
				),
				1
			),
			1,
			1
		);
	}



	/**
	 * Sets the point (x, y) to the specified value.
	 *
	 * @param x
	 * @param y
	 * @param vlaue
	 */
	public void setValue(final int x, final int y, final double value) {
		Thread.yield(); // for frequent thread changes
		synchronized (this) {
			array[y][x] = Double.valueOf(value);
			counter--;
		}
		invokeLater(repaint);
	}



	/**
	 * Checks whether a value has been assigned to the specified point (x, y).
	 *
	 * @param x
	 * @param y
	 *
	 * @return <code>true</code>, if the point (x, y) already has a value.
	 */
	public synchronized boolean hasValue(final int x, final int y) {
		return array[y][x] != null;
	}



	/**
	 * @return <code>true</code>, if the number of calls to setValue is equal or
	 * 		greater than the number of elements of this screen.
	 */
	public synchronized boolean finished() {
		return counter <= 0;
	}



	/**
	 * @return the width of this screen.
	 */
	public int getWidth() {
		return array[0].length;
	}



	/**
	 * @return the height of this screen.
	 */
	public int getHeight() {
		return array.length;
	}
}

