package com.example.osmeuslugares.modelo;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class CoordenadasGPS {

	private LocationManager manejador;
	private Activity activity;
	Location localizacion;
	String proveedor;

	/**
	 * @throws Exception
	 * 
	 */
	public CoordenadasGPS(Activity activity) throws Exception {
		super();
		this.activity = activity;
		// TODO Auto-generated constructor stub
		// inicializar manejador
		manejador = (LocationManager) activity.getSystemService(proveedor);
		if (manejador == null)
			throw new Exception("GPS no disponible");
		localizacion = manejador
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Criteria criteria = new Criteria();
		// ejor proveedor red o gps
		// proveedor = manejador.getBestProvider(criteria, true);

		// ultima loxalization conocida que es la actual
		localizacion = manejador.getLastKnownLocation(proveedor);

	}

	/**
	 * @return the localizacion
	 */
	public Location getLocalizacion() {
		return localizacion;
	}

}
