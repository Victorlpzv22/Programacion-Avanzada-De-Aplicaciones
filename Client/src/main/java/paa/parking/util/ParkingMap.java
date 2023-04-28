package paa.parking.util;

import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import paa.parking.business.BookingRemoteService;
import paa.parking.business.IBookingService;
import paa.parking.model.Parking;
import paa.parking.util.impl.FancyWaypointRenderer;
import paa.parking.util.impl.MyWaypoint;

/**
 * ParkingMap es una subclase de JXMapKit, a su vez una subclase de JPanel que permite
 * visualizar un mapa. Esta subclase añade un método que muestra sobreimpresos al mapa 
 * todos los parkings conocidos por la capa de negocio en la fecha que el usuario elija,
 * así como su número actual de plazas disponibles.
 * 
 * La documentación de la clase JXMapKit original se puede consultar en <a href=
 * "https://github.com/msteiger/jxmapviewer2">https://github.com/msteiger/jxmapviewer2</a>
 * 
 * @author PAA (PRL)
 */
public class ParkingMap extends JXMapKit {
	private static final long serialVersionUID = 1L;
	WaypointPainter<MyWaypoint> waypointPainter;

	/**
	 * Construye un nuevo mapa con la vista centrada en Madrid, con el tamaño 
	 * preferido indicado por el usuario.
	 * 
	 * @param preferredWidth Ancho preferido
	 * @param preferredHeight Alto preferido
	 */
	public ParkingMap(int preferredWidth, int preferredHeight) {
		super();
		this.setDefaultProvider(DefaultProviders.OpenStreetMaps);

		TileFactoryInfo info = new OSMTileFactoryInfo();
		TileFactory tf = new DefaultTileFactory(info);
		this.setTileFactory(tf);
		this.setZoom(7);
		this.setAddressLocation(new GeoPosition(40.438889, -3.691944)); // Madrid
		this.getMainMap().setRestrictOutsidePanning(true);
		this.getMainMap().setHorizontalWrapped(false);

		this.waypointPainter = new WaypointPainter<MyWaypoint>();
		waypointPainter.setRenderer(new FancyWaypointRenderer());
		this.getMainMap().setOverlayPainter(this.waypointPainter);

		((DefaultTileFactory) this.getMainMap().getTileFactory()).setThreadPoolSize(8);
		this.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
	}
	
	/**
	 * Pinta en el mapa la situación de ocupación de los parkings del sistema
	 * en la fecha solicitada. Para ello hace uso de la interfaz IBookingService
	 * del paquete paa.parking.business.
	 * 
	 * @param date Fecha para la que se desea pintar la disponibilidad.
	 */
	public void showAvailability(LocalDate date) {
		IBookingService s = new BookingRemoteService();
		Set<MyWaypoint> waypoints = new HashSet<MyWaypoint>();
		
		for (Parking p: s.findParkings()) {
			final int availableSpaces = s.availableSpaces(p.getCode(), date);
			final float occupancyRatio = (float) availableSpaces / (float) p.getBookableSpaces();
			Color c;
			if (0.5 < occupancyRatio) {
				c = Color.GREEN;
			} else if (0.2 < occupancyRatio) {
				c = Color.ORANGE;
			} else {
				c = Color.RED;
			}
			waypoints.add(new MyWaypoint("" + availableSpaces, c, new GeoPosition(p.getLatitude(), p.getLongitude())));
		}
		this.waypointPainter.setWaypoints(waypoints);
		this.repaint();
	}
}
