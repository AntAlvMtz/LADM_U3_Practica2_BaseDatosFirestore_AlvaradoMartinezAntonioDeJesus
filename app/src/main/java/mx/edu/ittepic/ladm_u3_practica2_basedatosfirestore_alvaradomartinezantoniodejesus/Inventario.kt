package mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus

import android.R
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus.ui.home.HomeFragment

class Inventario(este: Fragment) {
    private val este = este
    var codigoBarras = ""
    var tipoEquipo = ""
    var caracteristicas = ""
    var fechaCompra = ""
    var arreglo = ArrayList<String>()
    var listaID = ArrayList<String>()

    fun insertar(){
        val baseRemota = FirebaseFirestore.getInstance()
        val datos = hashMapOf(
            "CodigoBarras" to codigoBarras,
            "TipoEquipo" to tipoEquipo,
            "Caracteristicas" to caracteristicas,
            "FechaCompra" to fechaCompra
        )
        baseRemota.collection("inventario")
            .add( datos )
            .addOnSuccessListener {
                //Sí se pudo
                Toast.makeText(este.requireContext(),"ÉXITO! SE INSERTÓ CORRECTAMENTE", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                //No se pudo!!
                AlertDialog.Builder(este.requireContext())
                    .setMessage(it.message)
                    .show()
            }
    }

    fun mostrar(lista:ListView){

        FirebaseFirestore.getInstance()
            .collection("inventario")
            .addSnapshotListener { query, error ->
                //Snapshoot = foto de datos
                if (error!=null){
                    //Si entra, hay un error
                    AlertDialog.Builder(este.requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                arreglo.clear()
                listaID.clear()

                for(documento in query!!){
                    var cadena = "Codigo de Barras: ${documento.getString("CodigoBarras")}\n" +
                            "Tipo de Equipo: ${documento.getString("TipoEquipo")}\n" +
                            "Características: ${documento.getString("Caracteristicas")}\n" +
                            "Fecha de Compra: ${documento.getString("FechaCompra")}"
                    arreglo.add(cadena)
                    listaID.add(documento.id)

                }

                lista.adapter = ArrayAdapter<String>(este.requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)

            }
    }

    fun dialogoEliminarActualizar(position: Int, cod:EditText, tip:EditText, carac:EditText, fec:EditText, act:Button) {
        var idElegido = listaID.get(position)
        AlertDialog.Builder(este.requireContext())
            .setTitle("ATENCIÓN!!")
            .setMessage("¿Qué desea hacer con \n ${arreglo.get(position)}")
            .setPositiveButton("ELIMINAR"){d, i->
                eliminar(idElegido)
            }
            .setNeutralButton("ACTUALIZAR"){d,i->
                actualizar(idElegido,cod,tip,carac,fec,act)
            }
            .setNegativeButton("CANCELAR"){d,i->}
            .show()
    }

    private fun actualizar(idElegido: String, cod: EditText, tip: EditText, carac: EditText, fec: EditText, act:Button) {
        act.isEnabled = true
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("inventario")
            .document(idElegido)
            .get() //OBTIENE 1 DOCUMENTO
            .addOnSuccessListener {
                cod.setText(it.getString("CodigoBarras"))
                tip.setText(it.getString("TipoEquipo"))
                carac.setText(it.getString("Caracteristicas"))
                fec.setText(it.getString("FechaCompra"))
            }
            .addOnFailureListener {
                AlertDialog.Builder(este.requireContext())
                    .setMessage(it.message)
                    .show()
            }

        act.setOnClickListener {
            baseRemota.collection("inventario")
                .document(idElegido)
                .update("CodigoBarras" , cod.text.toString(), "TipoEquipo",tip.text.toString(),
                    "Caracteristicas" , carac.text.toString(),"FechaCompra" , fec.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(este.requireContext(),"ÉXITO! SE ACTUALIZÓ CORRECTAMENTE", Toast.LENGTH_LONG).show()
                    cod.text.clear()
                    tip.text.clear()
                    carac.text.clear()
                    fec.text.clear()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(este.requireContext())
                        .setMessage(it.message)
                        .show()
                }
            act.isEnabled = false

        }
    }


    fun eliminar(idElegido: String) {
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("inventario")
            .document(idElegido)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(este.requireContext(),"SE ELIMINÓ CORRECTAMENTE", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                AlertDialog.Builder(este.requireContext())
                    .setMessage(it.message)
                    .show()
            }

    }

    fun consultarPorTipo(tipo:String,lista: ListView){
        FirebaseFirestore.getInstance()
            .collection("inventario")
            .addSnapshotListener { query, error ->
                //Snapshoot = foto de datos
                if (error!=null){
                    //Si entra, hay un error
                    AlertDialog.Builder(este.requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                arreglo.clear()
                listaID.clear()

                for(documento in query!!){
                    if(documento.getString("TipoEquipo").equals(tipo)){
                        var cadena = "Codigo de Barras: ${documento.getString("CodigoBarras")}\n" +
                                "Tipo de Equipo: ${documento.getString("TipoEquipo")}\n" +
                                "Características: ${documento.getString("Caracteristicas")}\n" +
                                "Fecha de Compra: ${documento.getString("FechaCompra")}"
                        arreglo.add(cadena)
                        listaID.add(documento.id)
                    }

                }

                lista.adapter = ArrayAdapter<String>(este.requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)

            }
    }

    fun consultarPorCaracteristicas(carac:String,lista: ListView){
        FirebaseFirestore.getInstance()
            .collection("inventario")
            .addSnapshotListener { query, error ->
                //Snapshoot = foto de datos
                if (error!=null){
                    //Si entra, hay un error
                    AlertDialog.Builder(este.requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                arreglo.clear()
                listaID.clear()

                for(documento in query!!){
                    if(documento.getString("Caracteristicas").equals(carac)){
                        var cadena = "Codigo de Barras: ${documento.getString("CodigoBarras")}\n" +
                                "Tipo de Equipo: ${documento.getString("TipoEquipo")}\n" +
                                "Características: ${documento.getString("Caracteristicas")}\n" +
                                "Fecha de Compra: ${documento.getString("FechaCompra")}"
                        arreglo.add(cadena)
                        listaID.add(documento.id)
                    }

                }

                lista.adapter = ArrayAdapter<String>(este.requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)

            }
    }
}