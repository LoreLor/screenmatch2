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
}
