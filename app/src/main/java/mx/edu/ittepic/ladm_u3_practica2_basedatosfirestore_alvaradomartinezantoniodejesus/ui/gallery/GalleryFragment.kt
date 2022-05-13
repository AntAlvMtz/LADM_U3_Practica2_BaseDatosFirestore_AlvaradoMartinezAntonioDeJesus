package mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus.ui.gallery

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus.Asignacion
import mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus.Inventario
import mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val asig = Asignacion(this)

    val opciones = listOf("Tipo de Consulta","Por Empleado", "Por Area de Trabajo", "Por Fecha")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        asig.llenarSpinner(binding.codigobarras)

        asig.mostrar(binding.lista)

        binding.actualizar.isEnabled = false

        binding.op.adapter = ArrayAdapter(this.requireContext(),
            R.layout.simple_spinner_item,opciones)

        binding.op.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when(position){
                    1 -> if(binding.nomempleado.text.isNotEmpty())
                        asig.consultarPorEmp(binding.nomempleado.text.toString(),binding.lista)
                    else Toast.makeText(requireContext(),"Necesita ingresar un empleado", Toast.LENGTH_LONG).show()
                    2 -> if(binding.areatrab.text.isNotEmpty())
                        asig.consultarPorArea(binding.areatrab.text.toString(),binding.lista)
                    else Toast.makeText(requireContext(),"Necesita ingresar un área de trabajo",
                        Toast.LENGTH_LONG).show()
                    3 -> if(binding.fecha.text.isNotEmpty())
                        asig.consultarPorFecha(binding.fecha.text.toString(),binding.lista)
                    else Toast.makeText(requireContext(),"Necesita ingresar una fecha", Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.insertar.setOnClickListener {
            asig.nomEmp = binding.nomempleado.text.toString()
            asig.areaTrabajo = binding.areatrab.text.toString()
            asig.fecha = binding.fecha.text.toString()
            asig.insertar()
            binding.nomempleado.text.clear()
            binding.areatrab.text.clear()
            binding.fecha.text.clear()
            asig.llenarSpinner(binding.codigobarras)
        }

        binding.lista.setOnItemClickListener { adapterView, view, position, l ->
            asig.dialogoEliminarActualizar(position,binding.nomempleado,binding.areatrab,binding.fecha,binding.actualizar, binding.lista)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}