package com.louanmar.programandojdbc.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.louanmar.programandojdbc.model.Persona;

public interface PersonaDAO {

	public Persona insertarPersona(Persona persona) throws SQLException;
	
	public Persona buscarPersonaPorId(int idPersona) throws SQLException;
	
	public List<Persona> buscarTodasPersonas() throws SQLException;
	
	public void modificarPersona(Persona persona) throws SQLException;
	
	public List<Persona> buscarPersonasEntreFechas(LocalDate fechaInicial, LocalDate fechaFinal) throws SQLException;
}
