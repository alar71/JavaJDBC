package com.louanmar.programandojdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.louanmar.programandojdbc.model.Persona;

public class PersonaDAOMySQL implements PersonaDAO {

	//private final static String URL = "jdbc:mysql://localhost:3306/ejemplojdbc?user=root&password=Angel1971#";
	private final static String URL = "jdbc:mysql://localhost:3306/ejemplojdbc";
	private final static String USER = "root";
	private final static String PASSWORD = "Angel1971#";
	private final static String INSERTAR_PERSONA = "insert into persona(nombre, fechaNacimiento, salario, deBaja) values (?, ?, ?, ?)";
	private final static String BUSCAR_TODAS_PERSONAS = "select * from persona";
	private final static String BUSCAR_POR_ID = "select * from persona where idPersona = ?";
	private final static String MODIFICAR_PERSONA = "update persona set nombre = ?, fechaNacimiento = ?, salario = ?, deBaja = ? where idPersona = ?";
	private final static String BUSCAR_ENTRE_FECHAS = "select * from persona where fechaNacimiento between ? and ?";
	
	private final static String MODIFICAR_PERSONA_PRIMERA_PARTE = "update persona set nombre = ?, fechaNacimiento = ? where idPersona = ?";
	private final static String MODIFICAR_PERSONA_SEGUNDA_PARTE = "update persona set salario = ?, deBaja = ? where idPersona = ?";
	
	public PersonaDAOMySQL() throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
	}

	@Override
	public Persona insertarPersona(Persona persona) throws SQLException {
		//1º Obtener la conexión, pidiéndosela al DriverManager
		Connection conexion = DriverManager.getConnection(URL,USER,PASSWORD);
		//2º Obtener la sentencia a partir de la conexión
		//Como es una inserción y quiero conocer la clave AUTO_INCREMENT
		//generada, uso el método prepareStatement donde le paso de parámetro
		// Statement.RETURN_GENERATED_KEYS
		PreparedStatement sentencia = conexion.prepareStatement(INSERTAR_PERSONA, Statement.RETURN_GENERATED_KEYS);
		//3º Darle valor a los parámetros
		sentencia.setString(1, persona.getNombre());
		sentencia.setDate(2, Date.valueOf(persona.getFechaNacimiento()));
		sentencia.setDouble(3, persona.getSalario());
		sentencia.setBoolean(4, persona.isDeBaja());
		//4º Ejecutar la sentencia
		int numFilas = sentencia.executeUpdate();
		//5ª Procesar el resultado
		System.out.println("Se han insertado " + numFilas + " filas");
		//Recuperar la clave generada (AUTO_INCREMENT)
		ResultSet claves = sentencia.getGeneratedKeys();
		if (claves.next()) {
			int clave = claves.getInt(1);
			persona.setIdPersona(clave);
		}
		claves.close();
		sentencia.close();
		conexion.close();
		return persona;
	}

	@Override
	public Persona buscarPersonaPorId(int idPersona) throws SQLException {
		Persona p = null;
		Connection conexion = DriverManager.getConnection(URL,USER,PASSWORD);
		PreparedStatement sentencia = conexion.prepareStatement(BUSCAR_POR_ID);
		sentencia.setInt(1, idPersona);
		ResultSet resultado = sentencia.executeQuery();
		if (resultado.next()) {
			String nombre = resultado.getString("nombre");
			LocalDate fechaNacimiento = resultado.getDate("fechaNacimiento").toLocalDate();
			double salario = resultado.getDouble("salario");
			boolean deBaja = resultado.getBoolean("deBaja");
			p = new Persona(idPersona, nombre, fechaNacimiento, salario, deBaja);
		}
		resultado.close();
		sentencia.close();
		conexion.close();
		return p;
	}

	@Override
	public List<Persona> buscarTodasPersonas() throws SQLException {
		List<Persona> resultado = new ArrayList<>();
		Connection conexion = DriverManager.getConnection(URL,USER,PASSWORD);
		Statement sentencia = conexion.createStatement();
		ResultSet personas = sentencia.executeQuery(BUSCAR_TODAS_PERSONAS);
		while (personas.next()) {
			int idPersona = personas.getInt("idPersona");
			String nombre = personas.getString("nombre");
			LocalDate fechaNacimiento = personas.getDate("fechaNacimiento").toLocalDate();
			double salario = personas.getDouble("salario");
			boolean deBaja = personas.getBoolean("deBaja");
			Persona p = new Persona(idPersona, nombre, fechaNacimiento, salario, deBaja);
			resultado.add(p);
		}
		personas.close();
		sentencia.close();
		conexion.close();
		return resultado;
	}

	public void modificarPersona(Persona persona) throws SQLException {
		Connection conexion = DriverManager.getConnection(URL,USER,PASSWORD);
		//Deshabilito el modo automático
		conexion.setAutoCommit(false);
		
		PreparedStatement sentencia1 = conexion.prepareStatement(MODIFICAR_PERSONA_PRIMERA_PARTE);
		sentencia1.setString(1, persona.getNombre());
		sentencia1.setDate(2, Date.valueOf(persona.getFechaNacimiento()));
		sentencia1.setInt(3, persona.getIdPersona());
		
		PreparedStatement sentencia2 = conexion.prepareStatement(MODIFICAR_PERSONA_SEGUNDA_PARTE);
		sentencia2.setDouble(1, persona.getSalario());
		sentencia2.setBoolean(2, persona.isDeBaja());
		sentencia2.setInt(3, persona.getIdPersona());
		
		try {
			int numFilas1 = sentencia1.executeUpdate();
			int numFilas2 = sentencia2.executeUpdate();
		
			if (numFilas1 != 1) {
				System.out.println("Error al modificar la primera parte");
				conexion.rollback();
			} 
			if (numFilas2 != 1) {
				System.out.println("Error al modificar la segunda parte");
				conexion.rollback();
			}
			if (numFilas1 == 1 && numFilas2 == 1) {
				System.out.println("Modificación correcta");
				//Cuando la operación acaba de forma correcta, confirmo los cambios
				conexion.commit();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			conexion.rollback();
		}
		sentencia1.close();
		sentencia2.close();
		conexion.close();
	}
	
	@Override
	public List<Persona> buscarPersonasEntreFechas(LocalDate fechaInicial, LocalDate fechaFinal) throws SQLException {
		List<Persona> personas = new ArrayList<Persona>();
		
		Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
		PreparedStatement sentencia = conexion.prepareStatement(BUSCAR_ENTRE_FECHAS);
		sentencia.setDate(1, Date.valueOf(fechaInicial));
		sentencia.setDate(2, Date.valueOf(fechaFinal));
		
		ResultSet resultado = sentencia.executeQuery();
		while (resultado.next()) {
			int idPersona = resultado.getInt("idPersona");
			String nombre = resultado.getString("nombre");
			LocalDate fechaNacimiento = resultado.getDate("fechaNacimiento").toLocalDate();
			double salario = resultado.getDouble("salario");
			boolean deBaja = resultado.getBoolean("deBaja");
			Persona p = new Persona(idPersona, nombre, fechaNacimiento, salario, deBaja);
			personas.add(p);
		}
		resultado.close();
		sentencia.close();
		conexion.close();
		
		return personas;
	}
	
}
