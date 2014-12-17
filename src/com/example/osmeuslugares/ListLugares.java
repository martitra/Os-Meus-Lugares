package com.example.osmeuslugares;

import com.example.osmeuslugares.modelo.CoordenadasGPS;
import com.example.osmeuslugares.modelo.Lugar;
import com.example.osmeuslugares.modelo.basedatos.LugaresDb;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class ListLugares extends ListActivity {

	private ListLugaresAdapter listLugaresAdapter;
	Bundle extras = new Bundle();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_lugares);
		registerForContextMenu(super.getListView());
		// String[] lista = { "Sitio1", "Sitio2", "Sitio3" };
		// Vector<String> vector = new Vector<String>(3);
		// vector.add(lista[0]);
		// vector.add(lista[1]);
		// vector.add(lista[3]);
		// setListAdapter(new ListLugaresAdapter(this, vector));
		// creado objeto adaptador
		listLugaresAdapter = new ListLugaresAdapter(this);
		// lee el fichero de preferencias y lo devuelve
		listLugaresAdapter.setVerInfoAmpliada(getPreferenciaVerInfoAmpliada());
		setListAdapter(listLugaresAdapter);

		// Leer preferencia de info
		boolean infoAmpliada = getPreferenciaVerInfoAmpliada();
		if (infoAmpliada) {
			Toast.makeText(getBaseContext(), "Info Ampliada ON",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getBaseContext(), "Info Ampliada OFF",
					Toast.LENGTH_LONG).show();
		}

	}

	public void imageButtonAddLugarOnClick(View v) {
		Bundle extras = new Bundle();
		extras.putBoolean("add", true);
		lanzarEditLugar(extras);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Lugar itemLugar = (Lugar) getListAdapter().getItem(position);
		// bundle para pasar datos
		Bundle extras = itemLugar.getBundle();
		System.out.println("Desde listLugares" + itemLugar.getId());
		extras.putBoolean("add", false);
		lanzarEditLugar(extras);
	}

	private void lanzarEditLugar(Bundle extras) {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, EditLugarActivity.class);
		i.putExtras(extras);
		startActivityForResult(i, 1234);
	}

	public boolean getPreferenciaVerInfoAmpliada() {
		SharedPreferences preferencias = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		return preferencias.getBoolean("ver_info_ampliada", false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1234 && resultCode == RESULT_OK) {
			String resultado = data.getExtras().getString("resultado");
			Toast.makeText(getBaseContext(), resultado, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_lugares, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.add_lugar) {
			extras.clear();
			extras.putBoolean("add", true);
			lanzarEditLugar(extras);
			return true;
		}
		if (id == R.id.cerrar) {
			finish();
		}
		if (id == R.id.acerca_de) {
			Intent i = new Intent(this, AcercaDeActivity.class);
			startActivity(i);
		}

		if (id == R.id.mi_localizacion) {
			CoordenadasGPS coordenadasGPS = null;
			try {
				coordenadasGPS = new CoordenadasGPS(this);
				Location localizacion = coordenadasGPS.getLocalizacion();
				localizacion.toString();
				Toast.makeText(getBaseContext(), "GPS habilitado",
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(getBaseContext(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 * android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_lugares_contextual, menu);
	}

	@Override
	// esto es para aguantar la pantalla
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Lugar lugar = (Lugar) listLugaresAdapter.getItem(info.position);
		LugaresDb lugaresDb = new LugaresDb(this);

		switch (item.getItemId()) {
		case R.id.visitar_pagina:
			if (!lugar.getUrl().isEmpty()) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(lugar.getUrl()));
				startActivity(i);
			}
			return true;
		case R.id.marcar_telefono_lugar:
			lanzarMarcarTelefono(lugar.getTelefono());
			return true;
		case R.id.envia_email:
			lanzarEmail(lugar);
			return true;

		case R.id.edit_lugar:
			Toast.makeText(getBaseContext(), "Editar: " + lugar.getNombre(),
					Toast.LENGTH_SHORT).show();
			// llamar a editar
			Bundle extras = lugar.getBundle();
			extras.putBoolean("add", false);
			lanzarEditLugar(extras);
			return true;

		case R.id.delete_lugar:
			Toast.makeText(getBaseContext(), "Eliminar: " + lugar.getNombre(),
					Toast.LENGTH_SHORT).show();
			lugaresDb.eliminarLugar(lugar);
			// llamar a eliminar
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void lanzarEmail(Lugar lugar) {
		// TODO Auto-generated method stub

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		String[] to = { "lag@fenandowirtz.com" };
		String subject = "Lugar" + lugar.getNombre();
		String body = lugar.toString();
		i.putExtra(Intent.EXTRA_EMAIL, to);
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT, body);
		startActivity(i);

	}

	private void lanzarMarcarTelefono(String telefono) {
		// TODO Auto-generated method stub
		if (!telefono.isEmpty()) {
			Intent i = new Intent(Intent.ACTION_CALL);
			i.setData(Uri.parse("tel:" + telefono));
			startActivity(i);
		}
	}

	public void onRestart() {
		super.onRestart();
		listLugaresAdapter.actualizarDesdeBd();
		listLugaresAdapter.notifyDataSetChanged();

	}

}
