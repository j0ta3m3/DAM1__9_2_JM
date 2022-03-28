package un6.eje6_5

import java.sql.DriverManager
import java.util.logging.Level
import java.util.logging.LogManager
import javax.swing.JOptionPane

internal val l = LogManager.getLogManager().getLogger("").apply { level = Level.ALL }
internal fun i(tag: String, msg: String) {
    l.info("[$tag] - $msg")
}


fun main() {
    /*  var portatil = "/home/edu/IdeaProjects/IESRA-DAM-Prog/ejercicios/src/main/kotlin/un5/eje5_4/Catalog.xml"
      //var casa = "/home/usuario/Documentos/workspace/IdeaProjects/IESRA-DAM/ejercicios/src/main/kotlin/un5/eje5_4/Catalog.xml"
      var jm = "C:\\Users\\JMSJ\\Desktop\\b.xml"
      var miCatalogo = CatalogoLibrosXML(jm)

      var b = EnglishIUT(miCatalogo)
      b.preguntarPorUnLibro()
      b.mostrarInfoDeUnLibro()
  */


}

interface AccederLibros {

    fun infoLibro(idLibro: String): Map<String, Any>
    fun existeLibro(idLibro: String): Boolean
}

interface IU {

    fun preguntarPorUnLibro() {}
    fun mostrarInfoDeUnLibro() {}
}

class EnglishIUT(acceso: AccederLibros) : gestionLibros(acceso) {

    override fun preguntarPorUnLibro() {

        var idLibro = JOptionPane.showInputDialog("This is the interface in English :) ,Enter an ID")
        if (cat.existeLibro(idLibro))
            JOptionPane.showMessageDialog(null, "the book exists")
        else
            JOptionPane.showMessageDialog(null, "the book doesnt exist")
    }

    override fun mostrarInfoDeUnLibro() {

        var idLibro = JOptionPane.showInputDialog("This is the interface in English :) ,Enter an ID")
        var infoLibro = cat.infoLibro(idLibro)
        if (!infoLibro.isEmpty())
            JOptionPane.showMessageDialog(null, "The info of the book : \n$infoLibro")
        else
            JOptionPane.showMessageDialog(null, "info not found")
    }

}

class EspañolIUT(acceso: AccederLibros) : gestionLibros(acceso) {


    override fun preguntarPorUnLibro() {

        var idLibro = JOptionPane.showInputDialog("Esta es la interfaz en español :) ,Introduzca un ID")
        if (cat.existeLibro(idLibro))
            JOptionPane.showMessageDialog(null, "El libro existe")
        else
            JOptionPane.showMessageDialog(null, "El libro no existe")
    }

    override fun mostrarInfoDeUnLibro() {

        var idLibro = JOptionPane.showInputDialog("Esta es la interfaz en español :) ,Introduzca un ID")
        var infoLibro = cat.infoLibro(idLibro)
        if (!infoLibro.isEmpty())
            JOptionPane.showMessageDialog(null, "La información del libro es la siguiente : \n$infoLibro")
        else
            JOptionPane.showMessageDialog(null, "No se ha encontrado información")
    }

}

open class gestionLibros(acceso: AccederLibros) : IU {
    open var cat: AccederLibros = acceso

    override fun preguntarPorUnLibro() {
        super.preguntarPorUnLibro()
    }

    override fun mostrarInfoDeUnLibro() {
        super.mostrarInfoDeUnLibro()
    }


}