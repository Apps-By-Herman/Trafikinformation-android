package com.appsbyherman.trafikinformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.appsbyherman.trafikinformation.databinding.DialogDeviationBinding
import com.appsbyherman.trafikinformation.models.Deviation

class DeviationDialog : DialogFragment() {
    private lateinit var deviation: Deviation

    private var _binding: DialogDeviationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviation = arguments?.parcelable<BaseParcelable>(deviationTag)?.value as Deviation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = DialogDeviationBinding.inflate(inflater, container, false)

        binding.tvTest.text = deviation.header
        binding.tvTest2.text = deviation.message

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "ModalBottomSheet"
        const val deviationTag = "deviation"
    }
}
