package com.youravgjoe.joeycannonweatherapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textview.MaterialTextView
import com.youravgjoe.joeycannonweatherapp.models.WeatherData
import com.youravgjoe.joeycannonweatherapp.utils.TemperatureUnits
import com.youravgjoe.joeycannonweatherapp.utils.Utils
import com.youravgjoe.joeycannonweatherapp.utils.capitalizeWords

@ExperimentalStdlibApi
class MainActivity : AppCompatActivity(),
    InputFragment.InputFragmentInterface,
    ForecastFragment.ForecastInterface {
    private lateinit var backButton: ImageView
    private lateinit var icon: ImageView
    private lateinit var actionBarTitle: MaterialTextView
    private lateinit var options: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar()
        switchFragments(InputFragment.newInstance(), false, INPUT_TAG)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        updateActionBar(getVisibleFragment())
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar ?: return
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setCustomView(R.layout.action_bar)

        val view = actionBar.customView
        val toolbar: Toolbar = view .parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)

        backButton = view.findViewById(R.id.back)
        backButton.setOnClickListener {
            this.onBackPressed()
        }
        icon = view.findViewById(R.id.icon)

        actionBarTitle = view.findViewById(R.id.title)

        options = view.findViewById(R.id.options)


        val popupMenu = PopupMenu(this, options)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        if (Utils.getCurrentUnits(this) == TemperatureUnits.FAHRENHEIT) {
            popupMenu.menu.findItem(R.id.celsius).isChecked = true
            popupMenu.menu.findItem(R.id.fahrenheit).isChecked = false
        } else {
            popupMenu.menu.findItem(R.id.fahrenheit).isChecked = true
            popupMenu.menu.findItem(R.id.celsius).isChecked = false
        }
        popupMenu.setOnMenuItemClickListener { item ->
            popupMenu.menu.iterator().forEach {
                it.isChecked = false
            }
            when (item.itemId) {
                R.id.fahrenheit -> {
                    item.isChecked = true
                    Utils.setCurrentUnits(this, TemperatureUnits.FAHRENHEIT)
                    reloadFragment()
                    return@setOnMenuItemClickListener true
                }
                R.id.celsius -> {
                    item.isChecked = true
                    Utils.setCurrentUnits(this, TemperatureUnits.CELSIUS)
                    reloadFragment()
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }

        options.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun updateActionBar(fragment: Fragment?) {
        actionBarTitle.text = when (fragment) {
            is ForecastFragment -> fragment.city.capitalizeWords()
            is WeatherDetailsFragment -> fragment.weatherData.getDateTimeString()
            else -> resources.getString(R.string.app_name)
        }
        updateBackButton(fragment)
    }

    private fun updateBackButton(fragment: Fragment?) {
        if (fragment !is InputFragment) {
            backButton.visibility = View.VISIBLE
            icon.visibility = View.GONE
        } else {
            backButton.visibility = View.GONE
            icon.visibility = View.VISIBLE
        }
    }

    private fun switchFragments(
        fragment: Fragment,
        addToBackStack: Boolean = true,
        tag: String? = null
    ) {
        if (addToBackStack) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.container, fragment, tag)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.container, fragment, tag)
                .commit()
        }

        updateActionBar(fragment)
    }

    private fun reloadFragment() {
        getVisibleFragment()?.let {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.detach(it)
            transaction.attach(it)
            transaction.commit()
        }
    }

    private fun getVisibleFragment(): Fragment? {
        supportFragmentManager.fragments.forEach {
            if (it.isVisible) return it
        }
        return null
    }

    override fun onGetWeather(city: String, list: List<WeatherData>) {
        switchFragments(ForecastFragment.newInstance(city, list), tag = FORECAST_TAG)
    }

    override fun onRowClick(weatherData: WeatherData) {
        switchFragments(WeatherDetailsFragment.newInstance(weatherData), tag = WEATHER_DETAILS_TAG)
    }
}