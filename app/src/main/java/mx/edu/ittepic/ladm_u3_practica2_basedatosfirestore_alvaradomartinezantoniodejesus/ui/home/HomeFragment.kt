package mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus.ui.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus.Inventario
import mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val inventario = Inventario(this)

    val opciones = listOf("Tipo de Consulta","Por Tipo de Equipo", "Por Características")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.actualizar.isEnabled = false

        inventario.mostrar(binding.lista)

        binding.op.adapter = ArrayAdapter(this.requireContext(),android.R.layout.simple_spinner_item,opciones)

        binding.op.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    1 -> if (binding.tipoequipo.text.isNotEmpty()) {
                        inventario.consultarPorTipo(binding.tipoequipo.text.toString(),binding.lista)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Necesita ingresar un tipo",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    2 -> if (binding.caracteristicas.text.isNotEmpty()) {
                        inventario.consultarPorCaracteristicas(binding.caracteristicas.text.toString(),binding.lista)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Necesita ingresar la caracteristica a buscar",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.insertar.setOnClickListener {
            inventario.codigoBarras = binding.codigobarras.text.toString()
            inventario.tipoEquipo = binding.tipoequipo.text.toString()
            inventario.caracteristicas = binding.caracteristicas.text.toString()
            inventario.fechaCompra = binding.fecha.text.toString()
            inventario.insertar()
            binding.codigobarras.text.clear()
            binding.tipoequipo.text.clear()
            binding.caracteristicas.text.clear()
            binding.fecha.text.clear()

        }

        binding.lista.setOnItemClickListener { parent, view, position, l ->
            inventario.dialogoEliminarActualizar(position,binding.codigobarras,binding.tipoequipo,binding.caracteristicas,binding.fecha,binding.actualizar)
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}