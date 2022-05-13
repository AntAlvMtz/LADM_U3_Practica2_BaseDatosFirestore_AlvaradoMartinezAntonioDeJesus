package mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus

import android.R
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore


class Asignacion(este: Fragment) {
    private val este = este
    var id = ""
    var nomEmp = ""
    var areaTrabajo = ""
    var fecha = ""
    var codigoBarras = ""
    var arreglo = ArrayList<String>()
    var listaID = ArrayList<String>()
    var listaIDInv = ArrayList<String>()
    var listaCB = ArrayList<String>()

    fun llenarSpinner(cod:Spinner){
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

                listaCB.clear()
                listaIDInv.clear()
                //var cadena = "Selecciona un código de barras\n"
                //listaCB.add(cadena)
                for(documento in query!!){
                    var cadena = "${documento.getString("CodigoBarras")}\n"
                    listaCB.add(cadena)
                    listaIDInv.add(documento.id)

                }
                cod.adapter = ArrayAdapter(este.requireContext(),android.R.layout.simple_spinner_item,listaCB)

                cod.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        id = listaIDInv.get(position)
                        codigoBarras = listaCB.get(position)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }

    }

    fun insertar(){
        val baseRemota = FirebaseFirestore.getInstance()
        val datos = hashMapOf(
            "Id" to id,
            "NombreEmp" to nomEmp,
            "AreaTrabajo" to areaTrabajo,
            "Fecha" to fecha,
            "CodigoBarras" to codigoBarras
        )
        baseRemota.collection("asignacion")
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
            .collection("asignacion")
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
                    var cadena = "ID: ${documento.getString("Id")}\n" +
                            "Nombre Empleado: ${documento.getString("NombreEmp")}\n" +
                            "Fecha: ${documento.getString("Fecha")}\n" +
                            "Codigo de Barras : ${documento.getString("CodigoBarras")}"

                    arreglo.add(cadena)
                    listaID.add(documento.id)

                }

                lista.adapter = ArrayAdapter<String>(este.requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)

            }
    }

    fun dialogoEliminarActualizar(position: Int, nom:EditText, area:EditText, fecha:EditText, act:Button) {
        var idElegido = listaID.get(position)
        AlertDialog.Builder(este.requireContext())
            .setTitle("ATENCIÓN!!")
            .setMessage("¿Qué desea hacer con \n ${arreglo.get(position)}")
            .setPositiveButton("ELIMINAR"){d, i->
                eliminar(idElegido)
            }
            .setNeutralButton("ACTUALIZAR"){d,i->
                actualizar(idElegido,nom,area,fecha,act)
            }
            .setNegativeButton("CANCELAR"){d,i->}
            .show()
    }

    fun actualizar(idElegido: String, nom: EditText, area: EditText, fecha: EditText, act: Button) {
        act.isEnabled = true
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("asignacion")
            .document(idElegido)
            .get() //OBTIENE 1 DOCUMENTO
            .addOnSuccessListener {
                nom.setText(it.getString("NombreEmp"))
                area.setText(it.getString("AreaTrabajo"))
                fecha.setText(it.getString("Fecha"))
            }
            .addOnFailureListener {
                AlertDialog.Builder(este.requireContext())
                    .setMessage(it.message)
                    .show()
            }

        act.setOnClickListener {
            baseRemota.collection("asignacion")
                .document(idElegido)
                .update("Id", id,"NombreEmp" , nom.text.toString(), "AreaTrabajo",area.text.toString(),
                    "Fecha" , fecha.text.toString(),"CodigoBarras" , codigoBarras)
                .addOnSuccessListener {
                    Toast.makeText(este.requireContext(),"ÉXITO! SE ACTUALIZÓ CORRECTAMENTE", Toast.LENGTH_LONG).show()
                    nom.text.clear()
                    area.text.clear()
                    fecha.text.clear()
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
        baseRemota.collection("asignacion")
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


}