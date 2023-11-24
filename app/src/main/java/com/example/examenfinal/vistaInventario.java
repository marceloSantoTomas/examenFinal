package com.example.examenfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class vistaInventario extends AppCompatActivity {

    private EditText txtid, txtnom, txtcantidad;
    private Button btnbus, btnmod, btnreg, btneli;
    private ListView lvDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_inventario);


        txtid = (EditText) findViewById(R.id.txtid);
        txtnom = (EditText) findViewById(R.id.txtnom);
        txtcantidad = (EditText) findViewById(R.id.txtcantidad);
        btnbus = (Button) findViewById(R.id.btnbus);
        btnmod = (Button) findViewById(R.id.btnmod);
        btnreg = (Button) findViewById(R.id.btnreg);
        btneli = (Button) findViewById(R.id.btneli);
        lvDatos = (ListView) findViewById(R.id.lvDatos);



        botonBuscar();
        botonModificar();
        botonRegistrar();
        botonEliminar();
        listaProductos();


    }

    private void botonBuscar() {
        btnbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtid.getText().toString().trim().isEmpty() && txtnom.getText().toString().trim().isEmpty()) {
                    ocultarTeclado();
                    Toast.makeText(vistaInventario.this, "Ingresa el ID o el nombre del producto que quieres buscar", Toast.LENGTH_SHORT).show();
                } else {
                    int id = !txtid.getText().toString().trim().isEmpty() ? Integer.parseInt(txtid.getText().toString()) : -1;
                    String nombre = txtnom.getText().toString().trim();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Productos.class.getSimpleName());

                    dbref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            boolean found = false;

                            // Buscar por ID
                            if (id != -1 && id == Integer.parseInt(snapshot.child("id").getValue().toString())) {
                                found = true;
                            }

                            // Buscar por nombre
                            if (!nombre.isEmpty() && nombre.equalsIgnoreCase(snapshot.child("nombre").getValue().toString())) {
                                found = true;
                            }

                            if (found) {
                                ocultarTeclado();
                                txtnom.setText(snapshot.child("nombre").getValue().toString());

                                if (snapshot.hasChild("cantidad")) {
                                    String cantidad = snapshot.child("cantidad").getValue().toString();
                                    txtcantidad.setText(cantidad);
                                } else {
                                    txtcantidad.setText("");
                                }
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            // Manejar cambios si es necesario
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            // Manejar eliminaciones si es necesario
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            // Manejar movimientos si es necesario
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Manejar errores de base de datos si es necesario
                        }
                    });
                }
            }
        });
    }


    //cierra la funcion de boton buscar

    private void botonModificar() {
        btnmod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtid.getText().toString().trim().isEmpty()
                        || txtnom.getText().toString().trim().isEmpty()
                        || txtcantidad.getText().toString().trim().isEmpty()) {

                    ocultarTeclado();
                    Toast.makeText(vistaInventario.this, "Completa los datos faltantes para actualizar", Toast.LENGTH_SHORT).show();
                } else {
                    int id = Integer.parseInt(txtid.getText().toString());
                    String nom = txtnom.getText().toString();
                    int cantidad = Integer.parseInt(txtcantidad.getText().toString());

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Productos.class.getSimpleName());

                    dbref.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean res = false;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                res = true;
                                Productos producto = dataSnapshot.getValue(Productos.class);
                                dataSnapshot.getRef().child("nombre").setValue(nom);
                                dataSnapshot.getRef().child("cantidad").setValue(cantidad);
                                ocultarTeclado();
                                txtid.setText("");
                                txtnom.setText("");
                                txtcantidad.setText("");
                                listaProductos();
                                Toast.makeText(vistaInventario.this, "Producto modificado correctamente", Toast.LENGTH_SHORT).show();
                                break;
                            }

                            if (!res) {
                                ocultarTeclado();
                                Toast.makeText(vistaInventario.this, "ID (" + id + ") No encontrado.\n Imposible modificar", Toast.LENGTH_SHORT).show();
                                txtid.setText("");
                                txtnom.setText("");
                                txtcantidad.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Manejar errores de Firebase
                        }
                    });
                }
            }
        });
    }



    private void botonRegistrar() {
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtid.getText().toString().trim().isEmpty()
                        || txtnom.getText().toString().trim().isEmpty()
                        || txtcantidad.getText().toString().trim().isEmpty()) {

                    ocultarTeclado();
                    Toast.makeText(vistaInventario.this, "Completa los datos faltantes", Toast.LENGTH_SHORT).show();
                } else {
                    int id = Integer.parseInt(txtid.getText().toString());
                    String nom = txtnom.getText().toString();
                    int cantidad = Integer.parseInt(txtcantidad.getText().toString());

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Productos.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux = Integer.toString(id);
                            boolean res = false;
                            for (DataSnapshot x : snapshot.getChildren()) {
                                // si el id que quiero guardar coincide con uno de los id que ya está guardado
                                if (x.child("id").getValue().toString().equals(aux)) {
                                    res = true;
                                    ocultarTeclado();
                                    Toast.makeText(vistaInventario.this, "Error, el ID (" + aux + ") ya EXISTE!!", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            boolean res2 = false;
                            for (DataSnapshot x : snapshot.getChildren()) {
                                // si el id que quiero guardar coincide con uno de los id que ya está guardado
                                if (x.child("nombre").getValue().toString().equals(nom)) {
                                    res2 = true;
                                    ocultarTeclado();
                                    Toast.makeText(vistaInventario.this, "Error, el Nombre (" + nom + ") ya EXISTE!!", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if (res == false && res2 == false) {
                                Productos prod = new Productos(id, nom, cantidad);
                                dbref.push().setValue(prod);
                                ocultarTeclado();

                                Toast.makeText(vistaInventario.this, "Producto registrado CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                txtid.setText("");
                                txtnom.setText("");
                                txtcantidad.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }



    private void listaProductos() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Productos.class.getSimpleName());

        ArrayList<Productos> lisluc = new ArrayList<Productos>();
        ArrayAdapter<Productos> ada = new ArrayAdapter<Productos>(vistaInventario.this, android.R.layout.simple_list_item_1, lisluc);
        lvDatos.setAdapter(ada);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Productos prod = snapshot.getValue(Productos.class);
                lisluc.add(prod);
                ada.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ada.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Productos prod = lisluc.get(position);
                AlertDialog.Builder a = new AlertDialog.Builder(vistaInventario.this);
                a.setCancelable(true);
                a.setTitle("Producto Selecionado");
                String msg = "ID : " + prod.getId() + "\n\n";
                msg += "NOMBRE :" + prod.getNombre();
                a.setMessage(msg);
                a.show();

            }
        });


    } //Cierra el metodo lista productos

    private void botonEliminar() {
        btneli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtid.getText().toString().trim().isEmpty()) {
                    ocultarTeclado();
                    Toast.makeText(vistaInventario.this, "Ingresa el ID del producto que quieres eliminar", Toast.LENGTH_SHORT).show();
                } else {
                    int id = Integer.parseInt(txtid.getText().toString());

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Productos.class.getSimpleName());

                    dbref.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(id);
                            boolean res = false;
                            for (DataSnapshot x : snapshot.getChildren()) {
                                res = true;

                                AlertDialog.Builder builder = new AlertDialog.Builder(vistaInventario.this);
                                builder.setCancelable(false);
                                builder.setTitle("Pregunta");

                                int cantidadActual = x.child("cantidad").getValue(Integer.class);
                                if (cantidadActual > 1) {
                                    builder.setMessage("Hay " + cantidadActual + " productos con este ID. ¿Cuántos deseas eliminar?");
                                    final EditText input = new EditText(vistaInventario.this);
                                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    builder.setView(input);

                                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                int cantidadEliminar = Integer.parseInt(input.getText().toString());
                                                if (cantidadEliminar > 0 && cantidadEliminar <= cantidadActual) {
                                                    ocultarTeclado();
                                                    x.getRef().child("cantidad").setValue(cantidadActual - cantidadEliminar);
                                                    Toast.makeText(vistaInventario.this, cantidadEliminar + " productos eliminados correctamente", Toast.LENGTH_SHORT).show();
                                                    listaProductos();
                                                    txtid.setText("");
                                                    txtcantidad.setText("");
                                                    txtnom.setText("");
                                                } else {
                                                    Toast.makeText(vistaInventario.this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (NumberFormatException e) {
                                                Toast.makeText(vistaInventario.this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    builder.setMessage("¿Estás seguro que quieres eliminar este producto?");
                                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ocultarTeclado();
                                            x.getRef().removeValue();
                                            listaProductos();
                                            Toast.makeText(vistaInventario.this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // No hacer nada si se cancela
                                    }
                                });

                                builder.show();
                                break;
                            }

                            if (!res) {
                                ocultarTeclado();
                                Toast.makeText(vistaInventario.this, "ID (" + aux + ") No encontrado. ¡IMPOSIBLE ELIMINAR!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Manejar errores de Firebase
                        }
                    });
                }
            }
        });
    }
    //cierra el metodo eliminar


    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    } // Cierra el método ocultarTeclado.

}