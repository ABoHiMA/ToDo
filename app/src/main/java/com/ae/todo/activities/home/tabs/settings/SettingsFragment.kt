package com.ae.todo.activities.home.tabs.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.ae.todo.R
import com.ae.todo.components.Utils.SAVED_LANG
import com.ae.todo.components.Utils.SAVED_LANG_POS
import com.ae.todo.components.Utils.SAVED_MODE
import com.ae.todo.components.Utils.SAVED_MODE_POS
import com.ae.todo.components.Utils.setLanguage
import com.ae.todo.components.Utils.setMode
import com.ae.todo.components.Utils.sharedPreferences
import com.ae.todo.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var viewBinding: FragmentSettingsBinding
    private lateinit var textView: TextView
    private var savedLanguage: Int? = null
    private var savedMode: Int? = null
    private var firstTime = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView = requireActivity().findViewById(R.id.appbar_title)
        initSpinners()
    }

    private fun initSpinners() {
        ArrayAdapter.createFromResource(requireContext(), R.array.languages, R.layout.item_spinner)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                viewBinding.spinnerLang.adapter = adapter
            }

        ArrayAdapter.createFromResource(requireContext(), R.array.modes, R.layout.item_spinner)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                viewBinding.spinnerMode.adapter = adapter
            }


        savedLanguage = sharedPreferences?.getInt(SAVED_LANG_POS, 0)
        savedMode = sharedPreferences?.getInt(SAVED_MODE_POS, 0)

        viewBinding.spinnerLang.setSelection(savedLanguage!!)
        viewBinding.spinnerMode.setSelection(savedMode!!)

        viewBinding.spinnerLang.onItemSelectedListener = this
        viewBinding.spinnerMode.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (firstTime) {
            firstTime = false
            return
        }

        when (parent?.id) {
            viewBinding.spinnerLang.id -> {
                changLanguage(position)
                sharedPreferences?.edit()?.putInt(SAVED_LANG_POS, position)?.apply()
                refreshFragment()
                textView.setText(R.string.settings)
            }

            viewBinding.spinnerMode.id -> {
                changeTheme(position)
                sharedPreferences?.edit()?.putInt(SAVED_MODE_POS, position)?.apply()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun changLanguage(position: Int) {
        val selectedLanguage = if (position == 0) "en" else "ar"
        setLanguage(requireActivity(), selectedLanguage)
        sharedPreferences?.edit()?.putString(SAVED_LANG, selectedLanguage)?.apply()
    }

    private fun changeTheme(position: Int) {
        val selectedTheme = when (position) {
            0 -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            1 -> AppCompatDelegate.MODE_NIGHT_NO
            2 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        setMode(selectedTheme)
        sharedPreferences?.edit()?.putInt(SAVED_MODE, selectedTheme)?.apply()
    }

    private fun refreshFragment() {
        parentFragmentManager.beginTransaction().apply {
            replace(
                R.id.fragment_container, this@SettingsFragment::class.java, null
            )
            commit()
        }
    }

}
