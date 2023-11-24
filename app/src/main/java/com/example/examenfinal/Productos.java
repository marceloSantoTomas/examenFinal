package com.example.examenfinal;

import android.os.Parcel;
import android.os.Parcelable;

public class Productos implements Parcelable {
    private int id;
    private String nombre;
    private int cantidad; // Nuevo campo

    public Productos() {
    }

    public Productos(int id, String nombre, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }
    // Métodos getter y setter para el campo "cantidad"
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "ID de producto: " + id + "\n" +
                "Nombre del Producto: " + nombre + "\n" +
                "Cantidad: " + cantidad; // Incluye la cantidad en la representación de cadena
    }



//Proximo a ocupar
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nombre);
        dest.writeInt(this.cantidad);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.nombre = source.readString();

    }

    protected Productos(Parcel in) {
        this.id = in.readInt();
        this.nombre = in.readString();
        this.cantidad = in.readInt();

    }

    public static final Parcelable.Creator<Productos> CREATOR = new Parcelable.Creator<Productos>() {
        @Override
        public Productos createFromParcel(Parcel source) {
            return new Productos(source);
        }

        @Override
        public Productos[] newArray(int size) {
            return new Productos[size];
        }
    };
}
