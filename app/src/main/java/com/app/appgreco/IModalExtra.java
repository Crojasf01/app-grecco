package com.app.appgreco;

public interface IModalExtra {
    void modalIniciar(String nombre, String url, String uidUser);
    void modalIniciarDetail(String id);
    void modalAceptar(String id, Category category);
    void modalMensajito(String id);
}
