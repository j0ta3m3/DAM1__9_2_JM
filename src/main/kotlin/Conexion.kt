package un6.eje6_5

import mu.KotlinLogging
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*

data class Libro(

    var author: String,
    var title: String,
    var genre: String,
    var price: Double,
    var publish: Date,
    var description: String,
    var id: Int
)

class ConnectionBuilder {
    // TODO Auto-generated catch block
    lateinit var connection: Connection
    private val jdbcURL = "jdbc:h2:mem:default"
    private val jdbcUsername = ""
    private val jdbcPassword = ""

    init {
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)
        } catch (e: SQLException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

}

class BooksDAO(private val c: Connection) {

    companion object {
        private const val SCHEMA = "default"
        private const val BOOKS = "BOOKS"
        private const val TRUNCATE_TABLE_BOOKS_SQL = "TRUNCATE TABLE BOOKS"
        private const val CREATE_TABLE_BOOKS_SQL =
            "CREATE TABLE BOOKS ( " +
                    " id number (7) NOT NULL AUTO_INCREMENT ,\n" +
                    "title VARCHAR(50) ,\n" +
                    "author VARCHAR(20) ,\n" +
                    "genre VARCHAR(25) ,\n" +
                    "price DOUBLE ,\n" +
                    "publish_date DATE ,\n" +
                    "description VARCHAR(250)," +
                    "PRIMARY KEY (id))"
        private const val INSERT_BOOKS_SQL = "INSERT INTO BOOKS" + "  (id ,author, title, genre, price, publish_date,description ) VALUES " + " (? ,?, ?, ?, ?, ? ,?);"
        private const val SELECT_BOOK_BY_ID = "SELECT id,author, title, genre, price, publish_date,description from BOOKS where id =?"
        private const val SELECT_ALL_BOOKS = "select * from BOOKS"
        private const val DELETE_BOOKS_SQL = "delete from BOOKS where id = ?;"
        private const val UPDATE_BOOKS_SQL = "update BOOKS set author = ?,title= ?, genre =? , price = ? , publish_date = ? , description = ? where id = ?;"
    }

    fun update(book: Libro ): Boolean {
        var rowUpdated = false

        val sqlDate = java.sql.Date(book.publish.time) // Notice the ".sql." (not "util") in package name.

        try {
            c.prepareStatement(UPDATE_BOOKS_SQL).use { st ->
                st.setString(1, book.author)
                st.setString(2, book.title)
                st.setString(3, book.genre)
                st.setDouble(4, book.price)
                st.setDate(5, sqlDate)
                st.setString(6, book.description)
                println(st)
                st.executeUpdate()
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowUpdated
    }

    fun prepareTable() {
        val metaData = c.metaData
        val rs = metaData.getTables(null, SCHEMA, BOOKS, null)

        if (!rs.next()) createTable() else truncateTable()
    }

    private fun truncateTable() {
        println(TRUNCATE_TABLE_BOOKS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.createStatement().use { st ->
                st.execute(TRUNCATE_TABLE_BOOKS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun createTable() {
        println(CREATE_TABLE_BOOKS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            //Get and instance of statement from the connection and use
            //the execute() method to execute the sql
            c.createStatement().use { st ->
                //SQL statement to create a table
                st.execute(CREATE_TABLE_BOOKS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun printSQLException(ex: SQLException) {
        for (e in ex) {
            if (e is SQLException) {
                e.printStackTrace(System.err)
                System.err.println("SQLState: " + e.sqlState)
                System.err.println("Error Code: " + e.errorCode)
                System.err.println("Message: " + e.message)
                var t = ex.cause
                while (t != null) {
                    println("Cause: $t")
                    t = t.cause
                }
            }
        }

    }

    fun insertBook(book: Libro) {
        println(INSERT_BOOKS_SQL)
        // try-with-resource statement will auto close the connection.

        val sqlDate = java.sql.Date(book.publish.time) // Notice the ".sql." (not "util") in package name.


        try {
            c.prepareStatement(INSERT_BOOKS_SQL).use { st ->
                st.setInt(1, book.id)
                st.setString(2, book.author)
                st.setString(3, book.title)
                st.setString(4, book.genre)
                st.setDouble(5, book.price)
                st.setDate(6, sqlDate)
                st.setString(7, book.description)
                println(st)
                st.executeUpdate()
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun selectById(id: Int): Libro? {
        var miLibro: Libro? = null
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_BOOK_BY_ID).use { st ->
                st.setInt(1, id)
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()



                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val author = rs.getString("author")
                    val title = rs.getString("title")
                    val genre = rs.getString("genre")
                    val price = rs.getDouble("price")
                    val publish = rs.getDate("publish_date")
                    val description = rs.getString("description")
                    miLibro = Libro(author,title, genre, price, publish, description, id)
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return miLibro
    }

    fun deleteById(id: Int): Boolean {
        var rowDeleted = false

        try {
            c.prepareStatement(DELETE_BOOKS_SQL).use { st ->
                st.setInt(1, id)
                rowDeleted = st.executeUpdate() > 0
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowDeleted
    }

    fun selectAll(): List<Libro> {

        // using try-with-resources to avoid closing resources (boiler plate code)
        val libros: MutableList<Libro> = ArrayList()
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_ALL_BOOKS).use { st ->
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val author = rs.getString("author")
                    val title = rs.getString("title")
                    val genre = rs.getString("genre")
                    val price = rs.getDouble("price")
                    val publish = rs.getDate("publish_date")
                    val description = rs.getString("description")
                    val id = rs.getInt("id")
                    libros.add( Libro(author,title, genre, price, publish, description, id))
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return libros
    }
}


fun main() {
/*
    val c = ConnectionBuilder()
    println("conectando.....")

    if (c.connection.isValid(10)) {
        println("Conexión válida")

        c.connection.use {
            val h2DAO = BooksDAO(c.connection)

            // Creamos la tabla o la vaciamos si ya existe
            h2DAO.prepareTable()

            // Insertamos 4 libros
            repeat(4)
            {

                val format = SimpleDateFormat("MM/dd/yyyy");  // United States style of format.
                val myDate = format.parse("10/10/2009")

                h2DAO.insertBook(Libro("rafael", "erre", "terror", 4.4, myDate, "hghjv", id = 100))
            }

           var catDb= CatalogoLibrosDB(c)

           catDb.infoLibro("bk105").toString()

 */
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