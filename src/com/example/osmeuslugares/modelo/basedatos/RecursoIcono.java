package com.example.osmeuslugares.modelo.basedatos;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.example.osmeuslugares.R;
import com.example.osmeuslugares.R.array;

public class RecursoIcono {

	private Activity activity;
	private Resources res;
	private TypedArray drawableIconosLugares;
	private List<String> valoresIconosLugares;

	public RecursoIcono(Activity activity) {
		super();
		this.activity = activity;
		// cargar iconos lugares
		res = activity.getResources();
		drawableIconosLugares = res
				.obtainTypedArray(R.array.drawable_iconos_lugares);

		valoresIconosLugares = (List<String>) Arrays.asList(res
				.getStringArray(R.array.valores_iconos_lugares));

	}

	/**
	 * metodo para coger el icono en drawable getResources es para coger lo que
	 * está en la carpeta res referencia a un recurso:
	 * res.getDrawable(R.drawable.hotel_star) - coger el recurso uno a uno --
	 * 
	 * @param icon
	 * @return
	 */
	public Drawable obtenDrawableIcon(String icon) {

		// Buscamos la posici—n de icon
		int posicion = valoresIconosLugares.indexOf(icon);
		// -1 si no existe lo ponemos a 0 (icono ND: No Definido)
		if (posicion == -1)
			posicion = 0;
		return drawableIconosLugares.getDrawable(posicion);
		// return iconosLugares.getDrawable(0);//cero para probar/ esto devuelve
		// un drabwsble
	}
}
