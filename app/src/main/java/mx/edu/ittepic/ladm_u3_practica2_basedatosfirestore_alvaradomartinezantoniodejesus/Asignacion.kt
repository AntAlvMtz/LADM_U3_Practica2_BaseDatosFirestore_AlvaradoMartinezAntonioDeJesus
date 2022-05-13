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
                listaID.clear()
                //var cadena = "Selecciona un código de barras\n"
                //listaCB.add(cadena)
                for(documento in query!!){
                    var cadena = "${documento.getString("CodigoBarras")}\n"
                    listaCB.add(cadena)
                    listaID.add(documento.id)

                }
                cod.adapter = ArrayAdapter(este.requireContext(),android.R.layout.simple_spinner_item,listaCB)

                cod.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        id = listaID.get(position)
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




}