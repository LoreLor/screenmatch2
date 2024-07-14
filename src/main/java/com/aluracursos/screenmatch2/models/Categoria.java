package com.aluracursos.screenmatch2.models;

public enum Categoria {
    ACCION("Action", "Acción"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    DRAMA("Drama", "Drama"),
    CRIMEN("Crime", "Crimen");

    //me creo una avariable
    private String categoriaOmdb;
    private String categoriaEsp;
    //me creo un constructor
    Categoria(String categoriaOmdb, String categoriaEsp) {

        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEsp = categoriaEsp;
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

    public static Categoria fromEsp(String text){
        for (Categoria categoria : Categoria.values()) {
            if(categoria.categoriaEsp.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoría encontrada: " + text);
    }
}
