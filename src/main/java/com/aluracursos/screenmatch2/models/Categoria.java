package com.aluracursos.screenmatch2.models;

public enum Categoria {
    ACCION("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIMEN("Crime");

    //me creo una avariable
    private String categoriaOmdb;
    //me creo un constructor
    Categoria(String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
    }

    //parseo de enum a string
    public static Categoria fromString(String text){
        for (Categoria categoria : Categoria.values()) {
            if(categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoría encontrada: " + text);
    }
}
