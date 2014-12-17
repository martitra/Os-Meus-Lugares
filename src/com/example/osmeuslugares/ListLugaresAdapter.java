package com.example.osmeuslugares;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.example.osmeuslugares.modelo.Lugar;
import com.example.osmeuslugares.modelo.basedatos.LugaresDb;
import com.example.osmeuslugares.modelo.basedatos.RecursoIcono;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListLugaresAdapter extends BaseAdapter {

	private final Activity activity;
	private Vector<Lugar> lista;
	private LugaresDb lugaresDb;
	private boolean verInfoAmpliada;
	private RecursoIcono recursoIcono;

	/**
	 * 
	 * @param activity
	 * @param lista
	 */
	public ListLugaresAdapter(Activity activity) {
		super();
		this.activity = activity;
		this.verInfoAmpliada = true;
		this.lista = new Vector<Lugar>();
		actualizarDesdeBd();
		this.recursoIcono = new RecursoIcono(activity);

	}

	/**
	 * @return the verInfoAmpliada
	 */
	public boolean isVerInfoAmpliada() {
		return verInfoAmpliada;
	}

	/**
	 * @param verInfoAmpliada
	 *            the verInfoAmpliada to set
	 */
	public void setVerInfoAmpliada(boolean verInfoAmpliada) {
		this.verInfoAmpliada = verInfoAmpliada;
	}

	// idea: crear lugaresdb y cargar lista después los métodos hacen uso de
	// estos métodos
	public void actualizarDesdeBd() throws SQLException {
		lugaresDb = new LugaresDb(activity);
		this.lista = lugaresDb.cargarLugaresDesdeBD();
	}

	@Override
	public int getCount() {
		return lista.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lista.elementAt(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Lugar lugar = (Lugar) getItem(position);
		return lugar.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup) suministrar a lislugares
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// habrá im if o switch para informacióin ampliada o no
		// TODO Auto-generated method stub
		LayoutInflater inflater = activity.getLayoutInflater();
		View view;
		if (isVerInfoAmpliada()) {
			// modificar el if adaptarlo a lo que queremos, información ampliada
			view = inflater.inflate(R.layout.elemento_lista, null, true);
			TextView textViewTitulo = (TextView) view
					.findViewById(R.id.textViewTitulo);
			ImageView imgViewIcono = (ImageView) view.findViewById(R.id.icono);
			TextView textViewInfo = (TextView) view
					.findViewById(R.id.textViewInfo);
			Lugar lugar = (Lugar) lista.elementAt(position);
			textViewTitulo.setText(lugar.getNombre());
			textViewInfo.setText(lugar.toString());
			Drawable icon = recursoIcono.obtenDrawableIcon(lugar.getCategoria()
					.getIcon());
			imgViewIcono.setImageDrawable(icon);
		} else {
			view = inflater.inflate(R.layout.elemento_lista, null, true);
			TextView textViewTitulo = (TextView) view
					.findViewById(R.id.textViewTitulo);
			ImageView imgViewIcono = (ImageView) view.findViewById(R.id.icono);
			TextView textViewInfo = (TextView) view
					.findViewById(R.id.textViewInfo);
			Lugar lugar = (Lugar) lista.elementAt(position);
			textViewTitulo.setText(lugar.getNombre());
			textViewInfo.setText(lugar.getUrl());
			// textViewTitulo.setText(lista.elementAt(position));
			// pendiente textViewrUrl

			Drawable icon = recursoIcono.obtenDrawableIcon(lugar.getCategoria()
					.getIcon());
			imgViewIcono.setImageDrawable(icon);
		}
		return view;
	}

}
