package oldWOrk; /**
 * Copyright Gabor Drescher <drescher@cs.fau.de>
 * Adapted for PFP by Demian Kellermann <demian.kellermann@fau.de>
 * 				  and Georg Dotzler <georg.dotzler@cs.fau.de>
 * 
 * Graphical display for Game of Life.
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class Painter extends JFrame {
	private static final long serialVersionUID = 1L;

	private int w;
	private int h;
	private int psize;
	private PaintCanvas pc;
	JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

	public Painter(int h, int w, int psize) {
		this.w = w * psize;
		this.h = h * psize;
		this.psize = psize;
		add(pane);
		pc = new PaintCanvas();
		pc.setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane.getViewport().add(pc);
		pane.setSize(w, h);
		pack();
		pc.init();
		setVisible(true);

	}

	public void paintScene(int[][] cells) {
		pc.paintComponent(cells);

	}

	private class PaintCanvas extends JPanel {
		private static final long serialVersionUID = 1L;

		private BufferStrategy buffer;
		private BufferedImage bi;
		private Graphics2D g2d;

		public PaintCanvas() {
			this.setPreferredSize(new Dimension((int) w, (int) h));
		}

		public void init() {
			createBufferStrategy(2);
			buffer = getBufferStrategy();
			bi = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice().getDefaultConfiguration()
					.createCompatibleImage((int) w, (int) h);
		}

		int count = 0;

		public void paintComponent(int[][] cells) {
			// clear back buffer
			Rectangle view = new Rectangle();
			if (getParent() instanceof JViewport) {
				JViewport vp = (JViewport) getParent();
				view = vp.getViewRect();
			} else {
				view = new Rectangle(0, 0, getWidth(), getHeight());
			}
			g2d = bi.createGraphics();
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, view.width, view.height);

			// paint HERE on g2d
			g2d.setColor(Color.GREEN);
			for (int y = 0; y < cells.length; ++y) {
				for (int x = 0; x < cells[0].length; ++x) {
					if (cells[y][x] == 1) {
						if (x * psize >= view.getX()
								&& (x + 1) * (psize) < view.getX()
										+ view.getWidth()
								&& y * psize >= view.getY()
								&& (y + 1) * (psize) < view.getY()
										+ view.getHeight()) {
							g2d.fillRect(x * psize - (int) view.getX(), y
									* psize - (int) view.getY(), psize, psize);
						}
					}
				}
			}
			g2d.setColor(Color.WHITE);
			for (int x = 0; x < view.width; x += psize) {
				g2d.drawLine((int) x, 0, (int) x, view.height);
			}
			for (float y = 0; y < view.height; y += psize) {
				g2d.drawLine(0, (int) y, view.width, (int) y);
			}

			// split image and flip
			g2d = (Graphics2D) buffer.getDrawGraphics();
			g2d.drawImage(bi, 0, 0, null);
			if (!buffer.contentsLost()) {
				if (count == 0) {
					buffer.show();
				}
			}
			pane.getVerticalScrollBar().repaint();
			pane.getHorizontalScrollBar().repaint();
		}
	}
}
