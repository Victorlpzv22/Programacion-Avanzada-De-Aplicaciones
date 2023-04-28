package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import paa.parking.business.BookingRemoteService;
import paa.parking.model.Booking;
import paa.parking.model.Parking;

public class BorrarBooking extends JDialog {
	
	private JComboBox<Parking> lista;
	private BookingRemoteService n = new BookingRemoteService();

	private JPanel panel1;
	private JPanel panel2;
	
	private JButton ok;
	private JButton cancelar;
	
	private JLabel parking;
	private JLabel booking;
	
	private List<Parking> parkings;
	
	private JComboBox<Parking> listaParkings;
	private JComboBox<Booking> listaBookings;
	
	public BorrarBooking(JFrame padre, Booking preseleccionado) {
		super(padre, "Borrar Booking", true);
		setResizable(false);
		
		setLayout(new BorderLayout(5, 0));
		
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(0, 2, 0, 0));
		
		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		parking = new JLabel("Parking:");
		booking = new JLabel("Booking:");

		listaParkings = new JComboBox<Parking>();
		listaBookings = new JComboBox<Booking>();
		
		parkings = n.findParkings();
		
		for(Parking p: parkings)
			listaParkings.addItem(p);
		
		Parking parkingSeleccionado = (Parking) listaParkings.getSelectedItem();
		
		List<Booking>reservas1 = new ArrayList();
		
		List<Booking> bookings1 = n.findBookings();
		
		for(Booking b: bookings1)
			if(b.getParking().getCode() == parkingSeleccionado.getCode())
				reservas1.add(b);
		
		for(Booking b: reservas1)
			listaBookings.addItem(b);
		
		if(preseleccionado != null) {	
			listaParkings.setSelectedItem(preseleccionado.getParking());
			
			parkingSeleccionado = (Parking) listaParkings.getSelectedItem();
			
			List<Booking>reservas2 = new ArrayList();
			
			List<Booking> bookings2 = n.findBookings();
			
			for(Booking b: bookings2)
				if(b.getParking().getCode() == parkingSeleccionado.getCode())
					reservas2.add(b);
			
			for(Booking b: reservas2)
				listaBookings.addItem(b);
			
			listaBookings.setSelectedItem(preseleccionado);
		}
		
		ok = new JButton("OK");
		cancelar = new JButton("Cancelar");
		
		panel1.add(parking);
		panel1.add(listaParkings);
		
		panel1.add(booking);
		panel1.add(listaBookings);
		
		panel2.add(ok);
		panel2.add(cancelar);
		
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.SOUTH);
		
		ok.addActionListener(new CrearClick());
		cancelar.addActionListener(new CancelarClick());
		listaParkings.addActionListener(new ParkingClick());
		
	}

	class CrearClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			boolean correcto = true;
			try{
				Booking seleccionado = (Booking) listaBookings.getSelectedItem();
				n.cancelBooking(seleccionado.getCode());
			} catch(Exception ex) {
				JPanel excepcion = new JPanel();
				excepcion.setLayout(new GridLayout(0, 1, 0, 0));
				excepcion.add(new JLabel("Solo se puede cancelar una reserva con antelación.", JLabel.CENTER));
				JOptionPane.showMessageDialog(null, excepcion);
				
				correcto = false;
			}
			
			if(correcto)
				setVisible(false);
		}
	}

	class CancelarClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			setVisible(false);
		}
	}
	
	class ParkingClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			
			Parking parkingSeleccionado = (Parking) listaParkings.getSelectedItem();
			
			List<Booking> bookings = n.findBookings();
			
			List<Booking> reservas = new ArrayList();
			
			for(Booking b: bookings)
				if(b.getParking().getCode() == parkingSeleccionado.getCode())
					reservas.add(b);
			
			listaBookings.removeAllItems();
			
			for(Booking b: reservas)
				listaBookings.addItem(b);
		}
	}

}
