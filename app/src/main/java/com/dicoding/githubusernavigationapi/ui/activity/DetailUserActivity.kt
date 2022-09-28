package com.dicoding.githubusernavigationapi.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubusernavigationapi.viewmodel.DetailUserViewModel
import com.dicoding.githubusernavigationapi.R
import com.dicoding.githubusernavigationapi.adapter.SectionPagerAdapter
import com.dicoding.githubusernavigationapi.data.SettingPreferences
import com.dicoding.githubusernavigationapi.databinding.ActivityDetailUserBinding
import com.dicoding.githubusernavigationapi.viewmodel.SettingViewModel
import com.dicoding.githubusernavigationapi.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailUserViewModel>()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        val actionbar = supportActionBar
        actionbar!!.title = username.toString()
        actionbar.setDisplayHomeAsUpEnabled(true)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        detailViewModel.setUserDetail(username!!)
        detailViewModel.getUserDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvCompany.text = it.company
                    tvLocation.text = it.location
                    tvRepository.text = it.publicRepository.toString()
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .apply(RequestOptions().override(1000, 1000))
                        .into(imgView)
                }
            }
        }
        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.buttonFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.buttonFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.buttonFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                detailViewModel.addToFavorite(id, username, avatarUrl.toString())
            } else {
                detailViewModel.removeFromFavorite(id)
            }
            binding.buttonFavorite.isChecked = _isChecked
        }
        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        binding.apply {
            viewPager2.adapter = sectionPagerAdapter
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}
