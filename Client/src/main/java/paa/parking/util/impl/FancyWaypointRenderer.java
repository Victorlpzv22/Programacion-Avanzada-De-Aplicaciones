package paa.parking.util.impl;

/*
 * WaypointRenderer.java
 *
 * Created on March 30, 2006, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

/**
 * A fancy waypoint painter
 * 
 * @author Martin Steiger
 */
public class FancyWaypointRenderer implements WaypointRenderer<MyWaypoint> {

	private final Map<Color, BufferedImage> map = new HashMap<Color, BufferedImage>();

	// private final Font font = new Font("Lucida Sans", Font.BOLD, 10);

	private BufferedImage origImage;

	/**
	 * Uses a default waypoint image
	 */
	public FancyWaypointRenderer() {
		//URL resource = getClass().getClassLoader().getResource("waypoint_white.png");

		try {
			//origImage = ImageIO.read(resource);
			origImage = ImageIO.read(this.getClass().getResource("/waypoint_white.png"));
		} catch (Exception ex) {
			throw new RuntimeException("Could not load the waypoint icon.");
		}
	}

	private BufferedImage convert(BufferedImage loadImg, Color newColor) {
		int w = loadImg.getWidth();
		int h = loadImg.getHeight();
		BufferedImage imgOut = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		BufferedImage imgColor = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = imgColor.createGraphics();
		g.setColor(newColor);
		g.fillRect(0, 0, w + 1, h + 1);
		g.dispose();

		Graphics2D graphics = imgOut.createGraphics();
		graphics.drawImage(loadImg, 0, 0, null);
		graphics.setComposite(MultiplyComposite.Default);
		graphics.drawImage(imgColor, 0, 0, null);
		graphics.dispose();

		return imgOut;
	}

	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer viewer, MyWaypoint w) {
		g = (Graphics2D) g.create();

		if (origImage == null)
			return;

		BufferedImage myImg = map.get(w.getColor());

		if (myImg == null) {
			myImg = convert(origImage, w.getColor());
			map.put(w.getColor(), myImg);
		}

		Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());

		int x = (int) point.getX();
		int y = (int) point.getY();

		g.drawImage(myImg, x - myImg.getWidth() / 2, y - myImg.getHeight(), null);

		String label = w.getLabel();

		// g.setFont(font);

		FontMetrics metrics = g.getFontMetrics();
		int tw = metrics.stringWidth(label);
		int th = 1 + metrics.getAscent();

		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawString(label, x - tw / 2, y + th - myImg.getHeight() + 1); // dbd: Queda algo mejor el numerito con el +1

		g.dispose();
	}
}
