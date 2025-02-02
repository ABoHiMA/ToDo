package com.ae.todo.activities.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ae.todo.R
import com.ae.todo.activities.home.tabs.add.AddTaskFragment
import com.ae.todo.activities.home.tabs.settings.SettingsFragment
import com.ae.todo.activities.home.tabs.tasks.TaskFragment
import com.ae.todo.components.Utils.initApp
import com.ae.todo.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var bindingView: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        initApp(this)
        super.onCreate(savedInstanceState)

        bindingView = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindingView.root)
        initNavBar()
        restoreFragment(savedInstanceState)
        setOnFabClick()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment != null) {
            outState.putString("current_fragment", currentFragment.javaClass.name)
        }
    }

    private fun initNavBar() {
        bindingView.navBar.setOnItemSelectedListener { menu ->
            val fragment: Fragment = when (menu.itemId) {
                R.id.item_tasks -> TaskFragment()
                R.id.item_settings -> SettingsFragment()
                else -> TaskFragment()
            }
            replaceFragment(fragment)
            return@setOnItemSelectedListener true
        }
        bindingView.navBar.selectedItemId = R.id.item_tasks
    }

    private fun restoreFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val fragmentName = savedInstanceState.getString("current_fragment")
            if (fragmentName != null) {
                val fragment =
                    supportFragmentManager.fragmentFactory.instantiate(classLoader, fragmentName)
                replaceFragment(fragment)
            }
        } else replaceFragment(TaskFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setOnFabClick() {
        bindingView.fabAddTask.setOnClickListener {
            val bottomSheet = AddTaskFragment()
            bottomSheet.show(supportFragmentManager, "")
            bottomSheet.onTaskAdded =
                AddTaskFragment.OnTaskAdded { replaceFragment(TaskFragment()) }
        }
    }

}