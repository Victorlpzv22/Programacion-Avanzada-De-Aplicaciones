package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

import paa.parking.model.*;
import paa.parking.presentation.CrearParking.CancelarClick;
import paa.parking.presentation.CrearParking.CrearClick;
import paa.parking.business.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class CrearBooking extends JDialog {
	
	private JComboBox<Parking> listaParkings;
	private List<Parking> parkings;
	private BookingRemoteService n = new BookingRemoteService();
	
	private JPanel panel1;
	private JPanel panel2;
	
	private JButton crear;
	private JButton cancelar;
	
	private JLabel matricula;
	private JLabel fecha;
	private JLabel parking;
	
	private JTextField matriculaField;
	private JTextField fechaField;
	
	public  CrearBooking(JFrame padre) {
		super(padre, "Crear Booking", true);
		setResizable(false);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(0, 2, 0, 0));
		
		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		parking = new JLabel("Parking:");
		matricula = new JLabel("Matrícula:");
		fecha = new JLabel("Fecha:");

		parkings = n.findParkings();
		listaParkings = new JComboBox<Parking>();
		
		for(Parking p: parkings) {
			listaParkings.addItem(p);
		}
		
		matriculaField = new JTextField(20);
		fechaField = new JTextField(20);
		
		crear = new JButton("Crear");
		cancelar = new JButton("Cancelar");
		
		panel1.add(parking);
		panel1.add(listaParkings);
		
		panel1.add(matricula);
		panel1.add(matriculaField);
		
		panel1.add(fecha);
		panel1.add(fechaField);

		panel2.add(crear);
		panel2.add(cancelar);
		
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.SOUTH);
		
		crear.addActionListener(new CrearClick());
		cancelar.addActionListener(new CancelarClick());
	}

	class CrearClick implements ActionListener {
		public void actionPerformed(ActionEvent e){
			boolean correcto = true;
			try{
				Parking seleccionado = (Parking) listaParkings.getSelectedItem();
				Booking b = n.createBooking(matriculaField.getText(), LocalDate.parse(fechaField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), seleccionado.getCode());
			} catch(Exception ex) {
				JPanel excepcion = new JPanel();
				excepcion.setLayout(new GridLayout(0, 1, 0, 0));
				excepcion.add(new JLabel(ex.getMessage(), JLabel.CENTER));
				excepcion.add(new JLabel("Por favor, compruebe los datos introducidos.", JLabel.CENTER));
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
}