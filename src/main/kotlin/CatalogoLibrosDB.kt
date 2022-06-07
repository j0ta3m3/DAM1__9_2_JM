import mu.KotlinLogging
import un6.eje6_5.AccederLibros
import un6.eje6_5.BooksDAO
import un6.eje6_5.ConnectionBuilder
import java.time.LocalDate

class CatalogoLibrosDB(conexion: ConnectionBuilder) : AccederLibros {

    var l = conexion

    companion object {
        val log = KotlinLogging.logger("LOG")
    }

    internal fun i(msg: String) {
        log.info { msg }
    }

    override fun infoLibro(idLibro: String): Map<String, Any> {

        var a = idLibro.toMutableList()

        repeat(2) {
            a.removeAt(0)
        }

        var digito1 = a[0].toString()
        var digito2 = a[1].toString()
        var digito3 = a[2].toString()

        var digito = "$digito1$digito2$digito3"
        var digitoReal = digito.toInt()

        var m = mutableMapOf<String, Any>()

        l.connection.use {
            val h2DAOO = BooksDAO(l.connection)

            var libroDevuelto = h2DAOO.selectById(digitoReal)

            if (libroDevuelto != null) {
                m.put("id", idLibro)
                m.put("author", libroDevuelto.author)
                m.put("genre", libroDevuelto.genre)
                m.put("price", libroDevuelto.price)
                m.put(
                    "publish_date",
                    LocalDate.parse(libroDevuelto.publish.toString())
                )
                m.put("description", libroDevuelto.description)

            }

        }
        return m
    }
    override fun existeLibro(idLibro: String): Boolean {
        var existe : Boolean


        var a = idLibro.toMutableList()

        repeat(2) {
            a.removeAt(0)
        }

        var digito1 = a[0].toString()
        var digito2 = a[1].toString()
        var digito3 = a[2].toString()

        var digito = "$digito1$digito2$digito3"
        var digitoReal = digito.toInt()

        if (idLibro.isNullOrBlank())
            existe = false
        else {
            l.connection.use {
                val h2DAOOO = BooksDAO(l.connection)

               var listaLibros = h2DAOOO.selectAll()

                for (i in listaLibros.indices){
                    println("$i")
                }

            }
        }
        return true
}

    }
