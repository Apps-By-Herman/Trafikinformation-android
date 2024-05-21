package com.appsbyherman.trafikinformation

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.appsbyherman.trafikinformation.databinding.DialogDeviationBinding
import com.appsbyherman.trafikinformation.models.Deviation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.util.Locale

class DeviationDialog : DialogFragment(R.layout.dialog_deviation) {
    private lateinit var deviation: Deviation

    private var _binding: DialogDeviationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviation = arguments?.parcelable<BaseParcelable>(DEVIATION)?.value as Deviation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = DialogDeviationBinding.inflate(inflater, container, false)

        Gson().toJson(deviation).also { Log.d(tag, it) }

        if (deviation.image.isNotEmpty()) {
            // TODO: if we have more than one image, we should show them in a gallery. Is there ever a image???
            Glide.with(this).load(deviation.image.first()).into(binding.ivImage)
        }

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ROOT)

        if (deviation.endTime.isEmpty()) {
            binding.tvEndTime.visibility = View.GONE
        }
        else {
            val date = format.parse(deviation.endTime)
            val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT)

            binding.tvEndTime.text = "Slutar: ${sdf2.format(date)}"
        }

        if (deviation.startTime.isEmpty()) {
            binding.tvStartTime.visibility = View.GONE
        }
        else {
            val date = format.parse(deviation.startTime)
            val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT)

            binding.tvStartTime.text = "Börjar: ${sdf2.format(date)}"
        }

        deviation.affectedDirection.ifEmpty { binding.tvAffectedDirection.visibility = View.GONE}
        binding.tvAffectedDirection.text = deviation.affectedDirection.trim()

        deviation.locationDescriptor.ifEmpty { binding.tvLocationDescriptor.visibility = View.GONE }
        binding.tvLocationDescriptor.text = deviation.locationDescriptor.trim()

        deviation.message.ifEmpty { binding.tvMessage.visibility = View.GONE }
        binding.tvMessage.text = deviation.message.trim()

        deviation.trafficRestrictionType.ifEmpty { binding.tvTrafficRestrictionType.visibility = View.GONE }
        binding.tvTrafficRestrictionType.text = deviation.trafficRestrictionType.trim()

        deviation.severityText.ifEmpty { binding.tvSeverityText.visibility = View.GONE }
        binding.tvSeverityText.text = deviation.severityText.trim()

        deviation.messageCode.ifEmpty { binding.tvMessageCode.visibility = View.GONE }
        binding.tvMessageCode.text = deviation.messageCode.trim()

        deviation.roadName.ifEmpty { binding.tvRoadName.visibility = View.GONE }
        binding.tvRoadName.text = deviation.roadName.trim()

        deviation.roadNumber.ifEmpty { binding.tvRoadNumber.visibility = View.GONE }
        binding.tvRoadNumber.text = deviation.roadNumber.trim()

        binding.tvUrl.text = if (deviation.webLink.isEmpty() && deviation.url.isEmpty()) {
            binding.tvUrl.visibility = View.GONE
            ""
        }
        else {
            deviation.webLink.ifEmpty { deviation.url.ifEmpty { "" } }
        }

        binding.btnClose.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "DeviationDialog"
        const val DEVIATION = "deviation"
    }
}
