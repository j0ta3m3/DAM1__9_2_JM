import un6.eje6_5.BooksDAO
import un6.eje6_5.ConnectionBuilder
import un6.eje6_5.Libro
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.ArrayList



fun main(args: Array<String>) {


    var portatil = "/home/edu/IdeaProjects/IESRA-DAM-Prog/ejercicios/src/main/kotlin/un5/eje5_4/Catalog.xml"
    var casa =
        "/home/usuario/Documentos/workspace/IdeaProjects/IESRA-DAM/ejercicios/src/main/kotlin/un5/eje5_4/Catalog.xml"
    var jm = "C:\\Users\\JMSJ\\IntelliJIDEAProjects\\6_5\\src\\main\\kotlin\\un6\\eje6_5\\Catalog.xml"
    var cat = CatalogoLibrosXML(jm)
    var id = "bk105"
    cat.i(cat.existeLibro(id).toString())
    cat.i(cat.infoLibro(id).toString())


    val c = ConnectionBuilder()
    println("conectando.....")

    if (c.connection.isValid(10)) {
        println("Conexión válida")

        c.connection.use {
            val h2DAO = BooksDAO(c.connection)

            // Creamos la tabla o la vaciamos si ya existe
            h2DAO.prepareTable()

            // Insertamos 1 libro

            val format = SimpleDateFormat("MM/dd/yyyy");  // United States style of format.
            val myDate = format.parse("10/10/2009")

            h2DAO.insertBook(Libro("rafael", "erre", "terror", 4.4, myDate, "hghjv", 100))
            h2DAO.insertBook(Libro("manuel de falla","wai","miedo",4.5,myDate,"alomejor",101))

            //AQUI USO LA CLASE CATALOGOLIBROSDB

            var catDb = CatalogoLibrosDB(c)

            //USAMOS EL METODO PARA MOSTRAR LA INFORMACION DEL LIBRO QUE HEMOS INTRODUCIDO ANTES METIENDO SU ID
            catDb.i(catDb.infoLibro("bk100").toString())

            catDb.existeLibro("bk101")
/*
            // Buscar un libro
            var u = h2DAO.selectById(100)

            // Actualizar un libro
            if (u != null) {
                u.title = "Los pilares de la tierra"
                h2DAO.update(u)
            }

            // Borrar un libro
            h2DAO.deleteById(100)

            // Seleccionar todos los libros
            println(h2DAO.selectAll())


        }
    } else
        println("Conexión ERROR")

*/
        }
    }


}

