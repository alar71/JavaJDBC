package com.louanmar.programandojdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.louanmar.programandojdbc.dao.PersonaDAO;
import com.louanmar.programandojdbc.dao.PersonaDAOMySQL;
import com.louanmar.programandojdbc.model.Persona;

public class Main {

	private static BufferedReader entrada;
	
	public static void menu() {
		entrada = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("1. Alta persona");
		System.out.println("2. Buscar todas las personas");
		System.out.println("3. Buscar persona por ID");
		System.out.println("4. SALIR");
		System.out.print("Introduce una opción:");
		try {
			String opcion = entrada.readLine();
			while (!opcion.equals("4")) {
				switch(opcion) {
					case "1": alta();
						break;
					case "2": buscarTodas();
						break;
					case "3": buscarPorId();
						break;
					default:
						System.out.println("Opción incorrecta");
				}
				System.out.print("Introduce una opción:");
				opcion = entrada.readLine();
			}
			System.out.println("Bye");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Dato incorrecto");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Error al cargar el driver");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void buscarPorId() throws NumberFormatException, IOException, ClassNotFoundException, SQLException {
		System.out.print("\tId de la persona: ");
		int id = Integer.parseInt(entrada.readLine());
		PersonaDAO dao = new PersonaDAOMySQL();
		Persona p = dao.buscarPersonaPorId(id);
		if (p != null) {
			System.out.println(p);
		} else {
			System.out.println("Id no encontrado");
		}
	}

	private static void buscarTodas() throws ClassNotFoundException, SQLException {
		PersonaDAO dao = new PersonaDAOMySQL();
		List<Persona> personas = dao.buscarTodasPersonas();
		System.out.println(personas);
	}

	private static void alta() throws IOException, ClassNotFoundException, SQLException {
		System.out.print("\tNombre: ");
		String nombre = entrada.readLine();
		System.out.print("\tFecha de nacimiento YYYY-MM-DD: ");
		String fechaStr = entrada.readLine();
		LocalDate fechaNacimiento = LocalDate.parse(fechaStr);
		System.out.print("\tSalario: ");
		double salario = Double.parseDouble(entrada.readLine());
		PersonaDAO dao = new PersonaDAOMySQL();
		Persona nueva = dao.insertarPersona(new Persona(nombre, fechaNacimiento, salario, false));
		System.out.println(nueva + " dada de alta");
	}

	public static void main(String[] args) {
/*		Persona p = new Persona(1, "MIGUEL", LocalDate.of(2002, 2, 2), 19999.99, true);
		try {
			PersonaDAO dao = new PersonaDAOMySQL();
			dao.modificarPersona(p);
			//System.out.println(dao.buscarPersonaPorId(1));
			//List<Persona> lista = dao.buscarPersonasEntreFechas(LocalDate.of(1990, 1, 1), LocalDate.of(1999,1,1));
			//System.out.println(lista);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		
		menu();
		
		/*Persona p1 = new Persona("Juan Perez", LocalDate.of(1992, 4, 12), 1500.21, false);
		try {
			PersonaDAO dao = new PersonaDAOMySQL();
			Persona nueva = dao.insertarPersona(p1);
			System.out.println("Persona insertada correctamente con id: " + nueva);
			List<Persona> personas = dao.buscarTodasPersonas();
			System.out.println("PERSONAS: " + personas);
		} catch (ClassNotFoundException e) {
			System.out.println("No se encuentra la clase Driver");
		} catch (SQLException e) {
			System.out.println("Error al operar con la base de datos: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("ErrorCode: " + e.getErrorCode());
			//e.printStackTrace();
		}*/ 
		
	}

}
