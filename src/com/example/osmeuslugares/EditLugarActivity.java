package com.example.osmeuslugares;

import com.example.osmeuslugares.modelo.Categoria;
import com.example.osmeuslugares.modelo.Lugar;
import com.example.osmeuslugares.modelo.basedatos.LugaresDb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditLugarActivity extends Activity {
	private Lugar lugarEdit;

	private Spinner spinnerCategoria;
	private TextView editTextNombre;
	private TextView editTextDireccion;
	private TextView editTextTelefono;
	private TextView editTextUrl;
	private TextView editTextComentario;
	private boolean add;
	private LugaresDb db = new LugaresDb(this);
	Bundle extras;

	SpinnerCategoriasAdapter spinnerCategoriasAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_lugar);

		// Nombre
		editTextNombre = (TextView) findViewById(R.id.editTextNombre);
		// Categoria
		spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
		spinnerCategoriasAdapter = new SpinnerCategoriasAdapter(this);
		spinnerCategoria.setAdapter(spinnerCategoriasAdapter);
		// Direccion
		editTextDireccion = (TextView) findViewById(R.id.editDireccion);
		// Telefono
		editTextTelefono = (TextView) findViewById(R.id.editTelefono);
		// Url
		editTextUrl = (TextView) findViewById(R.id.editUrl);
		// Comentario
		editTextComentario = (TextView) findViewById(R.id.editComentario);

		// //////
		lugarEdit = new Lugar();
		extras = new Bundle();
		extras = getIntent().getExtras();
		add = extras.getBoolean("add");
		if (add) {
			Toast.makeText(getBaseContext(), "ADD", Toast.LENGTH_LONG).show();
			// lugarEdit.setValoresIniciales();
		} else {
			Toast.makeText(getBaseContext(), extras.getString(Lugar.C_NOMBRE),
					Toast.LENGTH_LONG).show();
			lugarEdit.setBundle(extras);
		}

		// Establecer valores desde lugarEdit a widget edici—n
		establecerValoresEditar();

		// en vez de hacer una lista estática hacemos un xml en el directorio
		// values array_tipos_lugares

		// lista estática
		// String []listaTipos = {"Prairas", "Restaurantes", "Outros"};
		// crear un adaptador
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item,
		// listaTipos);
		// spinnerTipo.setAdapter(adapter);

	}

	public void onClickCancelar(View v) {
		confirmar("Salir?", "Comfirmar");
	}

	private void confirmar(String texto, String titulo) {
		AlertDialog.Builder dialogConfirmar = new AlertDialog.Builder(this);
		dialogConfirmar.setTitle(titulo);
		dialogConfirmar.setMessage(texto);
		dialogConfirmar.setCancelable(false);
		dialogConfirmar.setPositiveButton("Confirmar",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						confirmar();
					}

					private void confirmar() {
						// TODO Auto-generated method stub
						finish();
					}

				});
		dialogConfirmar.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						cancelar();
					}

					private void cancelar() {
						// TODO Auto-generated method stub
						// hacer toast para poner opración cancelada
						Toast.makeText(getBaseContext(), "Operación cancelada",
								Toast.LENGTH_LONG).show();
					}
				});
		dialogConfirmar.show();// mostrar ventana de alert
	}

	private void establecerValoresEditar() {
		// TODO Auto-generated method stub
		editTextNombre.setText(lugarEdit.getNombre());

		int position = 0;
		if (!add) {
			position = spinnerCategoriasAdapter.getPositionById(lugarEdit
					.getCategoria().getId());
			Log.i("INFO", "Position=" + position);
		}
		spinnerCategoria.setSelection(position);

		editTextDireccion.setText(lugarEdit.getDireccion());
		editTextTelefono.setText(lugarEdit.getTelefono());
		editTextUrl.setText(lugarEdit.getUrl());
		editTextComentario.setText(lugarEdit.getComentario());

	}

	public void onClickGuardar(View v) {

		// Intent i=new Intent();
		// i.putExtra("resultado", "RESULTADO..");
		// setResult(RESULT_OK, i);
		// finish();

		if (add) {
			Lugar lugar = new Lugar();
			LugaresDb lugaresDb = new LugaresDb(this);

			lugar.setNombre(editTextNombre.getText().toString());
			lugar.setCategoria((Categoria) spinnerCategoria.getSelectedItem());
			lugar.setDireccion(editTextDireccion.getText().toString());
			lugar.setTelefono(editTextTelefono.getText().toString());
			lugar.setUrl(editTextUrl.getText().toString());
			lugar.setComentario(editTextComentario.getText().toString());
			lugaresDb.createLugar(lugar);
			Toast.makeText(getBaseContext(), "Guardado", Toast.LENGTH_LONG)
					.show();
		} else {
			// poñer todo o de arriba pero con lugaredit

			Lugar editLugar = new Lugar();
			LugaresDb lugaresDb = new LugaresDb(this);

			editLugar.setId(lugarEdit.getId());
			editLugar.setNombre(editTextNombre.getText().toString());
			editLugar.setCategoria((Categoria) spinnerCategoria
					.getSelectedItem());
			editLugar.setDireccion(editTextDireccion.getText().toString());
			editLugar.setTelefono(editTextTelefono.getText().toString());
			editLugar.setUrl(editTextUrl.getText().toString());
			editLugar.setComentario(editTextComentario.getText().toString());
			// editLugar.setId();
			lugaresDb.editLugar(editLugar);
			Toast.makeText(getBaseContext(), "Guardado", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_lugar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {

		case R.id.action_settings:
			return true;

		case R.id.add_categorias:
			lanzarNuevasCategorias();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void lanzarNuevasCategorias() {
		// TODO Auto-generated method stub
		extras.clear();
		extras.putBoolean("add", true);
		Intent i = new Intent(this, EditCategoriasActivity.class);
		i.putExtras(extras);// pasar add
		startActivity(i);
	}
}
