package com.example.osmeuslugares.modelo.basedatos;

import java.util.Vector;

import com.example.osmeuslugares.modelo.Categoria;
import com.example.osmeuslugares.modelo.Lugar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LugaresDb extends SQLiteOpenHelper {

	// variables estáticas
	private static String LOGTAG = "LugaresDb";// mandar a registro de log

	private SQLiteDatabase db;// creamos esto para no llamarlo en insertar
								// lugares prueba

	private static String nombre = "lugares.db";// nombre que le damos a la base
												// de datos
	private static CursorFactory factory = null;
	private static int version = 4;

	public LugaresDb(Context context) {
		super(context, nombre, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	// on create solo se ejecuta cuando la base de datos no existe si se mete
	// más inserciones
	// tenemos que borrar y ejecutar otra vez. Hacer método a parte para poner
	// más datos en bd
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		this.db = db;

		try {
			// lanzar creación de la tabla
			Log.i(LugaresDb.LOGTAG, "Creando base de datos....");
			// creamos tabla lugar
			String sql = "CREATE TABLE Lugar("
					+ "lug_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "lug_nombre TEXT NOT NULL, "
					+ "lug_categoria_id INTEGER NOT NULL,"
					+ "lug_direccion TEXT," + "lug_ciudad TEXT,"
					+ "lug_telefono TEXT, " + "lug_url TEXT,"
					+ "lug_comentario TEXT);";

			// Log.i(LugaresDb.LOGTAG,sql);

			db.execSQL(sql);// ejecutamos el sql
			sql = "CREATE UNIQUE INDEX idx_lug_nombre ON lugar(lug_nombre  ASC)";// crear
																					// indice
																					// por
																					// nombre
			db.execSQL(sql);

			sql = "CREATE TABLE Categoria("
					+ "cat_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "cat_nombre TEXT NOT NULL," + "cat_icon TEXT NOT NULL);";

			db.execSQL(sql);

			sql = "CREATE UNIQUE INDEX idx_cat_nombre ON Categoria(cat_nombre ASC)";
			db.execSQL(sql);

			// Log.i(LugaresDb.LOGTAG, "Base de datos creada");

			// datos de prueba
			insertarLugaresPrueba();
		} catch (SQLException e) {
			Log.e(getClass().toString(), e.getMessage());
		}

	}

	private void insertarLugaresPrueba() {

		db.execSQL("INSERT INTO Categoria(cat_nombre,cat_icon) "
				+ "VALUES('Playas','icono_playa')");
		db.execSQL("INSERT INTO Categoria(cat_nombre,cat_icon) "
				+ "VALUES('Restaurantes','icono_restaurante')");
		db.execSQL("INSERT INTO Categoria(cat_nombre,cat_icon) "
				+ "VALUES('Hoteles', 'icono_hotel')");
		db.execSQL("INSERT INTO Categoria(cat_nombre,cat_icon) "
				+ "VALUES('Otros', 'icono_otros')");

		db.execSQL("INSERT INTO Lugar(lug_nombre, lug_categoria_id, lug_direccion, lug_ciudad, lug_telefono, lug_url, lug_comentario) "
				+ "VALUES('Playa Riazor',1, 'Riazor','A Coru–a','981000000','','')");
		db.execSQL("INSERT INTO Lugar(lug_nombre, lug_categoria_id, lug_direccion, lug_ciudad, lug_telefono, lug_url, lug_comentario) "
				+ "VALUES('Playa Orzan',1, 'Orzan','A Coru–a','981000000','','')");
		Log.i("INFO", "Registros de prueba insertados");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i("INFO", "Base de datos: onUpgrade" + oldVersion + "->"
				+ newVersion);
		if (newVersion > oldVersion) {
			try {
				db.execSQL("DROP TABLE IF EXISTS Lugar");
				db.execSQL("DROP INDEX IF EXISTS idx_lug_nombre");
				db.execSQL("DROP TABLE IF EXISTS Categoria");
				db.execSQL("DROP INDEX IF EXISTS idx_cat_nombre");
			} catch (Exception e) {
				Log.e(this.getClass().toString(), e.getMessage());
			}
			onCreate(db);

			Log.i(this.getClass().toString(),
					"Base de datos actualizada. versi—n 3");
		}
	}

	public Vector<Lugar> cargarLugaresDesdeBD() {
		Vector<Lugar> resultado = new Vector<Lugar>();// crear vector vacío
		SQLiteDatabase db = this.getReadableDatabase();// abrir acceso a base
														// de datos en modo
														// lectura
		Cursor cursor = db.rawQuery("SELECT Lugar.*, cat_nombre, cat_icon "
				+ "FROM Lugar join Categoria on lug_categoria_id = cat_id",
				null);// crear query a base de datos
		// Se podrá usar query() en vez de rawQuery
		// join para recoger nombre de categoria, hacer tabla de categoria antes
		while (cursor.moveToNext()) {
			Lugar lugar = new Lugar();
			lugar.setId(cursor.getLong(cursor.getColumnIndex(Lugar.C_ID)));
			lugar.setNombre(cursor.getString(cursor
					.getColumnIndex(Lugar.C_NOMBRE)));
			Long idCategoria = cursor.getLong(cursor
					.getColumnIndex(Lugar.C_CATEGORIA_ID));
			String nombreCategoria = cursor.getString(cursor
					.getColumnIndex(Categoria.C_NOMBRE));
			String icon = cursor.getString(cursor
					.getColumnIndex(Categoria.C_ICON));

			lugar.setCategoria(new Categoria(idCategoria, nombreCategoria, icon));

			lugar.setDireccion(cursor.getString(cursor
					.getColumnIndex(Lugar.C_DIRECCION)));
			lugar.setCiudad(cursor.getString(cursor
					.getColumnIndex(Lugar.C_CIUDAD)));
			lugar.setTelefono(cursor.getString(cursor
					.getColumnIndex(Lugar.C_TELEFONO)));
			lugar.setUrl(cursor.getString(cursor.getColumnIndex(Lugar.C_URL)));
			lugar.setComentario(cursor.getString(cursor
					.getColumnIndex(Lugar.C_COMENTARIO)));
			resultado.add(lugar);

		}
		return resultado;
	}

	public Vector<Categoria> cargarCategoriasDesdeBD(boolean opcSeleccionar) {
		Vector<Categoria> resultado = new Vector<Categoria>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Categoria ORDER BY cat_id",
				null);
		if (opcSeleccionar) {
			resultado.add(new Categoria(0L, "Seleccionar...", "icono_nd"));
		}
		while (cursor.moveToNext()) {
			Categoria categoria = new Categoria();
			categoria.setId(cursor.getLong(cursor
					.getColumnIndex(Categoria.C_ID)));
			categoria.setNombre(cursor.getString(cursor
					.getColumnIndex(Categoria.C_NOMBRE)));
			categoria.setIcon(cursor.getString(cursor
					.getColumnIndex(Categoria.C_ICON)));

			resultado.add(categoria);
		}
		return resultado;
	}

	public void createLugar(Lugar newLugar) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues reg = new ContentValues();

		reg.put("lug_nombre", newLugar.getNombre());
		reg.put("lug_categoria_id", newLugar.getCategoria().getId());
		reg.put("lug_direccion", newLugar.getDireccion());
		reg.put("lug_telefono", newLugar.getTelefono());
		reg.put("lug_url", newLugar.getUrl());
		reg.put("lug_comentario", newLugar.getComentario());

		db.insertOrThrow("Lugar", null, reg);
	}

	public void editLugar(Lugar editLugar) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues reg = new ContentValues();

		reg.put("lug_nombre", editLugar.getNombre());
		reg.put("lug_categoria_id", editLugar.getCategoria().getId());
		reg.put("lug_direccion", editLugar.getDireccion());
		reg.put("lug_telefono", editLugar.getTelefono());
		reg.put("lug_url", editLugar.getUrl());
		reg.put("lug_comentario", editLugar.getComentario());

		db.update("Lugar", reg, "lug_id=" + editLugar.getId(), null);

	}

	public void eliminarLugar(Lugar eliminarLugar) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM Lugar WHERE lug_id=" + eliminarLugar.getId());
	}

	public void createCategoria(Categoria newCategoria) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues reg = new ContentValues();

		reg.put("cat_nombre", newCategoria.getNombre());
		reg.put("cat_icon", newCategoria.getIcon());

		db.insertOrThrow("Categoria", null, reg);

	}

	public void editCategoria(Categoria editCategoria) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues reg = new ContentValues();

		reg.put("cat_nombre", editCategoria.getNombre());
		reg.put("cat_icon", editCategoria.getIcon());

		db.update("Categoria", reg, "cat_id=" + editCategoria.getId(), null);

	}

	public void eliminarCategoria(Categoria eliminarCategoria) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM Categoria WHERE cat_id="
				+ eliminarCategoria.getId());
	}

}
