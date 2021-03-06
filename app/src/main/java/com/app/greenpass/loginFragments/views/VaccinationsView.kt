package com.app.greenpass.loginFragments.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.app.greenpass.adapters.GenerateQrCodeAdapter
import com.app.greenpass.databinding.FragmentVaccinationsBinding
import com.app.greenpass.loginFragments.viewmodels.VaccinationsViewModel
import kotlinx.coroutines.runBlocking

//MVVM architecture for fragments
//a vaccinations view, consists of layout bindings and
//listeners for user interaction
open class VaccinationsView : Fragment() {
    private val vaccinationsViewModel: VaccinationsViewModel by activityViewModels()
    private lateinit var mBinding: FragmentVaccinationsBinding
    private val binding get() = mBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentVaccinationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vaccinationsViewModel.updateView()
        vaccinationsViewModel.viewsResult.observe(
            viewLifecycleOwner,
            { s ->
                when (s) {
                    is VaccinationsViewModel.ViewResult.Opened -> s.apply {
                        binding.galleryText.text = resources.getString(text)
                        binding.vaccHistory.adapter = GenerateQrCodeAdapter(
                            list,
                            vaccinationsViewModel,
                        )
                        binding.vaccHistory.addItemDecoration(
                            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                        )
                    }
                    is VaccinationsViewModel.ViewResult.Generated -> s.apply {
                        binding.desc.text = resources.getString(testText, s.date)
                        binding.imageView2.setImageBitmap(testMap)
                    }
                    else -> Toast.makeText(activity, "Wrong state", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}