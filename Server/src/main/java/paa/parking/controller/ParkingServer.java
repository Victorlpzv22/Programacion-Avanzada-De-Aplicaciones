package paa.parking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import paa.parking.business.BookingService;
import paa.parking.business.BookingServiceException;
import paa.parking.model.Parking;
import paa.parking.util.PublicParkings;
import paa.parking.model.Booking;

@WebServlet("/")
public class ParkingServer extends HttpServlet {

	BookingService bs;
	private static final long serialVersionUID = 1L;

	public ParkingServer() {
		super();
	}

	public void init() {
		String absoluteDiskPath = getServletContext().getRealPath("./WEB-INF/classes/bdatos/PAA_ParkingsNew.mdb");

		bs = new BookingService(absoluteDiskPath);
		
		PublicParkings parkingsPublicos = new PublicParkings();
		List<Parking> parkings = (List) parkingsPublicos.listMadridParkings();
		
		for(Parking p: parkings)
			bs.createParking(p.getName(), p.getAddress(), p.getSpaces(), p.getBookableSpaces(), p.getLongitude(), p.getLatitude());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch(request.getParameter("action")) {
		case "findParkings":
			findParkings(request, response);
			break;
		case "createParking":
			createParking(request, response);
			break;
		case "findParking":
			findParking(request, response);
			break;
		case "availableSpaces":
			availableSpaces(request, response);
			break;
		case "createBooking":
			createBooking(request, response);
			break;
		case "removeBooking":
			removeBooking(request, response);
			break;
		case "findBookings":
			findBookings(request, response);
			break;
		default:
			response.sendError(400, "Malformed URL, please check the API.");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void findParking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
			
			JSONArray arrayDatos = new JSONArray();
			
			JSONObject datoP = new JSONObject();
			JSONObject datoB = new JSONObject();
			
			Long codigo = Long.parseLong(request.getParameter("parkingCode"));

			Parking p = bs.findParking(codigo);

			datoP.put("Code", p.getCode());
			datoP.put("Name", p.getName());
			datoP.put("Address", p.getAddress());
			datoP.put("BookableSpaces", p.getBookableSpaces());
			datoP.put("TotalSpaces", p.getSpaces());
			datoP.put("Longitude", p.getLongitude());
			datoP.put("Latitude", p.getLatitude());
			
			for (Booking b : p.getBookings()) {
				datoB = new JSONObject();
				datoB.put("Code", b.getCode());
				datoB.put("ParkingCode", b.getParking().getCode());
				datoB.put("LicencePlate", b.getLicencePlate());
				datoB.put("Date", b.getDate().toString());
				arrayDatos.add(datoB);
			}
			
			datoP.put("Bookings", arrayDatos);

			String jsonString = datoP.toJSONString();
			out.println(jsonString);
		} catch (IOException e) {
			response.sendError(500);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void findParkings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
			
			JSONArray arrayDatosP = new JSONArray();
			JSONArray arrayDatosB = new JSONArray();
			
			JSONObject datoP = new JSONObject();
			JSONObject datoB = new JSONObject();
			
			List<Parking> parkings = bs.findParkings();

			for (Parking p : parkings) {
				datoP = new JSONObject();
				datoP.put("Code", p.getCode());
				datoP.put("Name", p.getName());
				datoP.put("Address", p.getAddress());
				datoP.put("BookableSpaces", p.getBookableSpaces());
				datoP.put("TotalSpaces", p.getSpaces());
				datoP.put("Longitude", p.getLongitude());
				datoP.put("Latitude", p.getLatitude());

				arrayDatosB = new JSONArray();
				for (Booking b : p.getBookings()) {
					datoB = new JSONObject();
					datoB.put("Code", b.getCode());
					datoB.put("ParkingCode", b.getParking().getCode());
					datoB.put("LicencePlate", b.getLicencePlate());
					datoB.put("Date", b.getDate().toString());
		
					arrayDatosB.add(datoB);
				}
				datoP.put("Bookings", arrayDatosB);
				arrayDatosP.add(datoP);
			}
			
			String jsonString = arrayDatosP.toJSONString();
			out.println(jsonString);
		} catch (IOException e) {
			response.sendError(500);
		}
	}

	@SuppressWarnings("unchecked")
	protected void createParking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();

			JSONObject datoP = new JSONObject();

			String n = request.getParameter("name");
			String n1 = n.replace("-", " ");
			String n2 = n1.replace("_a", "á");
			String n3 = n2.replace("_e", "é");
			String n4 = n3.replace("_i", "í");
			String n5 = n4.replace("_o", "ó");
			String n6 = n5.replace("_u", "ú");
			String n7 = n6.replace("_v", "ü");
			String n8 = n7.replace("_1", "(");
			String n9 = n8.replace("_2", ")");
			String nr = n9.replace("_3", "/");
			
			String a = request.getParameter("address");
			String a1 = a.replace("-", " ");
			String a2 = a1.replace("_a", "á");
			String a3 = a2.replace("_e", "é");
			String a4 = a3.replace("_i", "í");
			String a5 = a4.replace("_o", "ó");
			String a6 = a5.replace("_u", "ú");
			String a7 = a6.replace("_v", "ü");
			String a8 = a7.replace("_1", "(");
			String a9 = a8.replace("_2", ")");
			String ar = a9.replace("_3", "/");
			
			Parking p = bs.createParking(nr, ar, Integer.parseInt(request.getParameter("totalSpaces")),
					Integer.parseInt(request.getParameter("bookableSpaces")), Double.parseDouble(request.getParameter("longitude")), Double.parseDouble(request.getParameter("latitude")));

			datoP.put("Code", p.getCode());
			datoP.put("Name", p.getName());
			datoP.put("Address", p.getAddress());
			datoP.put("BookableSpaces", p.getBookableSpaces());
			datoP.put("TotalSpaces", p.getSpaces());
			datoP.put("Longitude", p.getLongitude());
			datoP.put("Latitude", p.getLatitude());

			String jsonString = datoP.toJSONString();
			out.println(jsonString);
		} catch (IOException e) {
			response.sendError(500);
		}
	}

	@SuppressWarnings("unchecked")
	protected void availableSpaces(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
			JSONObject dato = new JSONObject();
			
			Long codigo = Long.parseLong(request.getParameter("parkingCode"));
			LocalDate fecha = LocalDate.parse(request.getParameter("date"));

			dato.put("AvailableSpaces", bs.availableSpaces(codigo, fecha));

			String jsonString = dato.toJSONString();
			out.println(jsonString);
		} catch (IOException e) {
			response.sendError(500);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void createBooking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();

			JSONObject datoB = new JSONObject();

			Booking b = bs.createBooking(request.getParameter("licencePlate"), LocalDate.parse(request.getParameter("date")), Long.parseLong(request.getParameter("parkingCode")));
			
			datoB.put("Code", b.getCode());
			datoB.put("ParkingCode", b.getParking().getCode());
			datoB.put("LicencePlate", b.getLicencePlate());
			datoB.put("Date", b.getDate().toString());

			String jsonString = datoB.toJSONString();
			out.println(jsonString);
		} catch (IOException e) {
			response.sendError(500);
		}
	}
	
	protected void removeBooking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding("UTF-8");

			bs.cancelBooking(Long.parseLong(request.getParameter("bookingCode")));

		} catch (IOException e) {
			response.sendError(500);
		}
	}

	@SuppressWarnings("unchecked")
	protected void findBookings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
			JSONArray arrayDatosBooking = new JSONArray();
			JSONObject datoBooking = new JSONObject();
			
			List<Booking> bookings = bs.findBookings();
			
			for (Booking b : bookings) {
				datoBooking = new JSONObject();
				datoBooking.put("Code", b.getCode());
				datoBooking.put("ParkingCode", b.getParking().getCode());
				datoBooking.put("LicencePlate", b.getLicencePlate());
				datoBooking.put("Date", b.getDate().toString());
				arrayDatosBooking.add(datoBooking);
			}
			
			String jsonString = arrayDatosBooking.toJSONString();
			out.println(jsonString);
		} catch (IOException e) {
			response.sendError(500);
		}
	}
}
