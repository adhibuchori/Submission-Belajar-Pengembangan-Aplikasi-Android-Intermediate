package com.adhibuchori.storyapp.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhibuchori.storyapp.R
import com.adhibuchori.storyapp.adapter.ItemUserStoryAdapter
import com.adhibuchori.storyapp.adapter.LoadingStateAdapter
import com.adhibuchori.storyapp.databinding.ActivityMainBinding
import com.adhibuchori.storyapp.ui.auth.welcome.WelcomeActivity
import com.adhibuchori.storyapp.data.remote.utils.story.Result
import com.adhibuchori.storyapp.ui.maps.MapsActivity
import com.adhibuchori.storyapp.ui.story.addstory.AddStoryActivity
import com.adhibuchori.storyapp.ui.story.detail.DetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory.getInstance(application, dataStore)
    }

    private val launcherAddStoryActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            mainViewModel.getAllStories()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyAdapter = ItemUserStoryAdapter { story, optionsCompat ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, story.id)
            startActivity(intent, optionsCompat.toBundle())
        }

        binding.rvUserStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter.withLoadStateFooter(footer = LoadingStateAdapter { storyAdapter.retry()})
        }

        mainViewModel.listStory.observe(this) {
            storyAdapter.submitData(lifecycle, it)
            if (it == null) Toast.makeText(
                this@MainActivity,
                getString(R.string.empty_text),
                Toast.LENGTH_SHORT
            ).show()
        }

        mainViewModel.isLogin().observe(this) {
            if (it.isNullOrEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.logout),
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        binding.fabAddStory.setOnClickListener {
            launcherAddStoryActivity.launch(Intent(this, AddStoryActivity::class.java))
        }

        binding.fabLocation.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        setupAction()
    }

    private fun setupAction() {
        binding.toolbar.actionLogout.setOnClickListener {
            mainViewModel.logout().observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, getString(R.string.logout_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.toolbar.actionChangeLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    companion object{
        const val RESULT_OK = 110
    }
}