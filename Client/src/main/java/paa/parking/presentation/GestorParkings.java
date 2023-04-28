package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import paa.parking.business.BookingRemoteService;
import paa.parking.model.Booking;
import paa.parking.model.Parking;
import paa.parking.util.ParkingMap;
import paa.parking.util.PublicParkings;

public class GestorParkings extends JFrame {
	
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JPanel subPanel1;
	private JPanel subPanel2;
	
	private JButton button1;
	private JButton button2;
	
	private JComboBox<Parking> listaParkings;
	private List<Parking> parkings;
	
	private JComboBox<LocalDate> listaFechas;
	private List<LocalDate> fechas;
	
	private List<Booking> bookings;

	private JList<Booking> lista;
	private List<Booking> reservas;

	private Icon icon1 = new ImageIcon("src/main/resources/nuevo.png");
	private Icon icon2 = new ImageIcon("src/main/resources/borrar.png");
	
	private ParkingMap parking;
	
	private JMenuBar menu;
	private JMenu archivo;
	private JMenu ayuda;
	
	private JMenuItem nuevoParking;
	private JMenuItem nuevaReserva;
	private JMenuItem anularReserva;
	private JMenuItem acercaDe;
	private JMenuItem salir;
	
	private Border borde1;
	private Border borde2;
	private Border borde3;
	
	private BookingRemoteService n = new BookingRemoteService();
	
	public  GestorParkings(String titulo) {
		super (titulo);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout(5,10));
		
		parking = new ParkingMap(800, 600);
		
		menu = new JMenuBar();
		archivo = new JMenu("Archivo");
		ayuda = new JMenu("Ayuda");
		
		nuevoParking = new JMenuItem("Nuevo Parking");
		nuevaReserva = new JMenuItem("Nueva Reserva");
		anularReserva = new JMenuItem("Anular Reserva");
		acercaDe = new JMenuItem("Acerca De");
		salir = new JMenuItem("Salir");
		
		borde1 = new TitledBorder("Mapa de Bookings");
		borde2 = new TitledBorder("Parkings");
		borde3 = new TitledBorder("Bookings");
		
		setJMenuBar(menu);
		menu.add(archivo);
		menu.add(ayuda);
		
		archivo.add(nuevoParking);
		archivo.add(nuevaReserva);
		archivo.add(anularReserva);
		archivo.add(salir);
		ayuda.add(acercaDe);
		
		panel1 = new JPanel();
		panel1.setLayout(new BorderLayout(5,10));
		
		BorderFactory.createTitledBorder(borde1);
		panel1.setBorder(borde1);

		panel1.add(parking, BorderLayout.CENTER);
		
		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		button1 = new JButton(icon1);
		button2 = new JButton(icon2);
		
		panel2.add(button1);
		panel2.add(button2);
		
		panel3= new JPanel();
		panel3.setLayout(new BorderLayout(5,10));
		
		subPanel1 = new JPanel();
		subPanel1.setLayout(new FlowLayout());
		
		BorderFactory.createTitledBorder(borde2);
		subPanel1.setBorder(borde2);
		
		listaParkings = new JComboBox<Parking>();
		
		parkings = n.findParkings();

		for(Parking p: parkings)
			listaParkings.addItem(p);

		subPanel1.add(listaParkings);
		
		if(parkings.size() != 0) 
			listaParkings.setSelectedIndex(0);

		listaFechas = new JComboBox<LocalDate>();

		bookings= n.findBookings();
		
		fechas = new ArrayList<LocalDate>();
		
		for(Booking b : bookings) 
			if(!fechas.contains(b.getDate()))
				fechas.add(b.getDate());

		for(LocalDate f : fechas) 
			listaFechas.addItem(f);
		
		if(fechas.size() != 0)
			parking.showAvailability((LocalDate) listaFechas.getSelectedItem());
		
		panel1.add(listaFechas, BorderLayout.NORTH);
		
		subPanel2 = new JPanel();
		subPanel2.setLayout(new FlowLayout());
		
		BorderFactory.createTitledBorder(borde3);
		subPanel2.setBorder(borde3);
		
		lista = new JList<Booking>();

		lista.setVisibleRowCount(35);
		
		JScrollPane scroller = new JScrollPane(lista);
		
		if(parkings.size() != 0) {
			reservas = new ArrayList();

			bookings = n.findBookings();

			Parking parkingSeleccionado = (Parking) listaParkings.getSelectedItem();

			for(Booking b: bookings)
				if(b.getParking().getCode() == parkingSeleccionado.getCode())
					reservas.add(b);

			lista.setListData(new Vector(reservas));
		}
		
		subPanel2.add(scroller);
		
		panel3.add(subPanel1, BorderLayout.NORTH);
		panel3.add(subPanel2, BorderLayout.CENTER);

		add(panel1, BorderLayout.CENTER);
		add(panel2, BorderLayout.NORTH);
		add(panel3, BorderLayout.WEST);

		CrearBookingClick crearBooking = new CrearBookingClick();
		BorrarBookingClick borrarBooking = new BorrarBookingClick();
		
		acercaDe.addActionListener(new AyudaClick());
		salir.addActionListener(new SalirClick());
		button1.addActionListener(crearBooking);
		nuevaReserva.addActionListener(crearBooking);
		nuevoParking.addActionListener(new CrearParkingClick());
		listaParkings.addActionListener(new ParkingClick());
		button2.addActionListener(borrarBooking);
		anularReserva.addActionListener(borrarBooking);
		listaFechas.addActionListener(new MapaClick());
		lista.addListSelectionListener(new ListaClick());
	}

	class AyudaClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			JOptionPane.showMessageDialog(GestorParkings.this, "Gestor de parkings y bookings, por Víctor López Valero. Versión 1.0");
		}
	}
	
	class SalirClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}
	}

	class CrearParkingClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			JDialog cp = new CrearParking(GestorParkings.this);
			listaParkings.removeAllItems();
			
			cp.pack();
			cp.setVisible(true);
			
			parkings = n.findParkings();
			
			for(Parking p: parkings)
				listaParkings.addItem(p);
			
			subPanel1.add(listaParkings);
			
			if(fechas.size() != 0)
				parking.showAvailability((LocalDate) listaFechas.getSelectedItem());
		}
	}
	
	class ParkingClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			Parking parkingSeleccionado = (Parking) listaParkings.getSelectedItem();
			
			reservas = new ArrayList();

			List<Booking> bookings = n.findBookings();

			if(parkingSeleccionado != null) {
				for(Booking b: bookings)
					if(b.getParking().getCode() == parkingSeleccionado.getCode())
						reservas.add(b);
			}

			lista.setListData(new Vector(reservas));
		}
	}

	class CrearBookingClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			JDialog cb = new CrearBooking(GestorParkings.this);
			
			cb.pack();
			cb.setVisible(true);
			
			Parking parkingSeleccionado = (Parking) listaParkings.getSelectedItem();
			
			reservas = new ArrayList();
			
			List<Booking> bookings = n.findBookings();
			
			for(Booking b: bookings)
				if(b.getParking().getCode() == parkingSeleccionado.getCode())
					reservas.add(b);
			
			lista.setListData(new Vector(reservas));

			bookings = n.findBookings();
			
			for(Booking b : bookings)
				if(!fechas.contains(b.getDate())) {
					fechas.add(b.getDate());
					listaFechas.addItem(b.getDate());
				}
			
			if(fechas.size() != 0)
				parking.showAvailability((LocalDate) listaFechas.getSelectedItem());
		}
	}

	class BorrarBookingClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			JDialog bb = new BorrarBooking(GestorParkings.this, lista.getSelectedValue());

			bb.pack();
			bb.setVisible(true);
			
			Parking parkingSeleccionado = (Parking) listaParkings.getSelectedItem();
			
			reservas = new ArrayList();
			
			List<Booking> bookings = n.findBookings();
			
			for(Booking b: bookings)
				if(b.getParking().getCode() == parkingSeleccionado.getCode())
					reservas.add(b);
			
			lista.setListData(new Vector(reservas));

			if(fechas.size() != 0)
				parking.showAvailability((LocalDate) listaFechas.getSelectedItem());
		}
	}
	
	class MapaClick implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(fechas.size() != 0)
				parking.showAvailability((LocalDate) listaFechas.getSelectedItem());
		}
	}

	class ListaClick implements ListSelectionListener {
		private JLabel parking1;
		private JLabel parking2;
		private JLabel matricula1;
		private JLabel matricula2;
		private JLabel fecha1;
		private JLabel fecha2;

		public void valueChanged(ListSelectionEvent e) {
			Booking seleccionado = lista.getSelectedValue();
			if(seleccionado != null) {
				JPanel info = new JPanel();
				info.setLayout(new GridLayout(0, 2, 0, 0));
				parking1 = new JLabel("Parking:");
				parking2 = new JLabel(seleccionado.getParking().getName());
				matricula1 = new JLabel("Matrícula:");
				matricula2 = new JLabel(seleccionado.getLicencePlate());
				fecha1 = new JLabel("Fecha:");
				fecha2 = new JLabel(seleccionado.getDate().format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));

				info.add(parking1);
				info.add(parking2);			
				info.add(matricula1);
				info.add(matricula2);			
				info.add(fecha1);
				info.add(fecha2);

				JOptionPane.showMessageDialog(GestorParkings.this, info);
			}
		}
	}

	public static void main (String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BookingRemoteService b = new BookingRemoteService();

				JFrame gestor = new GestorParkings("Gestor de Parkings y Reservas");
				gestor.pack();
				gestor.setVisible(true);
			}
		});
		
	}
}