interface AccederLibros {

    fun infoLibro(idLibro: String): Map<String, Any>
    fun existeLibro(idLibro: String): Boolean
}