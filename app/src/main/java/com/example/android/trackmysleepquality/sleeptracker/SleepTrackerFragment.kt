/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

         // Create an instance of the ViewModel Factory.
        // Вам нужна ссылка на приложение, к которому прикреплен этот фрагмент, чтобы перейти в поставщик view-model factory.
        // Функция requireNotNullKotlin выдает IllegalArgumentException значение if null.
        val application = requireNotNull(this.activity).application

        // Вам нужна ссылка на ваш источник данных через ссылку на DAO.
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val sleepTrackerViewModel = ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        binding.sleepTrackerViewModel = sleepTrackerViewModel  // привязкa (dataBinding)
        // Установите текущее activity в качестве владельца жизненного цикла привязки (dataBinding).
        binding.lifecycleOwner = this

        sleepTrackerViewModel.navigateToSleepQuality.observe(this.viewLifecycleOwner) { night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId)
                )
                sleepTrackerViewModel.doneNavigating()
            }
        }

        sleepTrackerViewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        }

        val  adapter = SleepNightAdapter()
        binding.sleepList.adapter = adapter

        sleepTrackerViewModel.nights.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)   // Метод обновления списка ListAdapter
            }
        }

        val manager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false )
        binding.sleepList.layoutManager = manager

        return binding.root
    }
}
