package org.apps.storyapp.ui.story

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.apps.storyapp.R
import org.apps.storyapp.ViewModelFactory
import org.apps.storyapp.databinding.ActivityStoryBinding
import org.apps.storyapp.model.User
import org.apps.storyapp.model.UserPreference
import org.apps.storyapp.ui.profile.ProfileActivity
import org.apps.storyapp.ui.maps.MapsActivity
import org.apps.storyapp.ui.upload_story.UploadStoryActivity

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var factory: ViewModelFactory
    private val storyViewModel by viewModels<StoryViewModel>() { factory }
    private lateinit var userPreference: UserPreference
    private lateinit var user: User
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.halaman_story)

        userPreference = UserPreference(this)
        user = userPreference.getUser()

        setupViewModel()
        showListStories(this)
        navigate()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun navigate(){
        binding.fab.setOnClickListener {
            startActivity(Intent(this,UploadStoryActivity::class.java))
        }
    }

    private fun showListStories(context: Context) {
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(context, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(context)
        }

        adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        storyViewModel.getListStory.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.addLoadStateListener(loadStateListener)
    }

    private val loadStateListener = { loadState: CombinedLoadStates ->
        val isLoading = loadState.source.refresh is LoadState.Loading
        showLoading(isLoading)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            }
            R.id.maps ->{
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
