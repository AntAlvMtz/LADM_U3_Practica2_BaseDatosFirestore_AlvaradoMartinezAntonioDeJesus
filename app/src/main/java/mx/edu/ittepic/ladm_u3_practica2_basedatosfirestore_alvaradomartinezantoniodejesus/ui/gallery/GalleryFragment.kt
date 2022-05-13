package mx.edu.ittepic.ladm_u3_practica2_basedatosfirestore_alvaradomartinezantoniodejesus.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        asig.llenarSpinner(binding.codigobarras)

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}