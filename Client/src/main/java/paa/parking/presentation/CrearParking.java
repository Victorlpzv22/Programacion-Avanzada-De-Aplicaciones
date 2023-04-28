package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import paa.parking.model.*;
import paa.parking.business.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CrearParking extends JDialog {
	
	private BookingRemoteService n = new BookingRemoteService();

	private JPanel panel1;
	private JPanel panel2;
	
	private JButton crear;
	private JButton cancelar;
	
	private JLabel nombre;
	private JLabel direccion;
	private JLabel plazas;
	private JLabel plazasDisponibles;
	private JLabel longitud;
	private JLabel latitud;
	
	private JTextField nombreField;
	private JTextField direccionField;
	private JTextField plazasField;
	private JTextField plazasDisponiblesField;
	private JTextField longitudField;
	private JTextField latitudField;
	
	public CrearParking(JFrame padre) {
		super(padre, "Crear Parking", true);
		setResizable(false);
		
		setLayout(new BorderLayout(5, 0));
		
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(0, 2, 0, 0));
		
		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		nombre = new JLabel("Nombre:");
		direccion = new JLabel("Dirección:");
		plazas = new JLabel("Plazas:");
		plazasDisponibles = new JLabel("Plazas Disponibles:");
		longitud = new JLabel("Longitud:");
		latitud = new JLabel("Latitud:");

		nombreField = new JTextField(20);
		direccionField = new JTextField(20);
		plazasField = new JTextField(20);
		plazasDisponiblesField = new JTextField(20);
		longitudField = new JTextField(20);
		latitudField = new JTextField(20);
		
		crear = new JButton("Crear");
		cancelar = new JButton("Cancelar");
		
		panel1.add(nombre);
		panel1.add(nombreField);
		
		panel1.add(direccion);
		panel1.add(direccionField);
		
		panel1.add(plazas);
		panel1.add(plazasField);
		
		panel1.add(plazasDisponibles);
		panel1.add(plazasDisponiblesField);
		
		panel1.add(longitud);
		panel1.add(longitudField);
		
		panel1.add(latitud);
		panel1.add(latitudField);
		
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
				Parking p = n.createParking(nombreField.getText(), direccionField.getText(), Integer.parseInt(plazasField.getText()), Integer.parseInt(plazasDisponiblesField.getText()), Double.parseDouble(longitudField.getText()), Double.parseDouble(latitudField.getText()));
			} catch(Exception ex) {
				JPanel excepcion = new JPanel();
				excepcion.setLayout(new GridLayout(0, 1, 0, 0));
				excepcion.add(new JLabel("Los datos son invalidos.", JLabel.CENTER));
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
