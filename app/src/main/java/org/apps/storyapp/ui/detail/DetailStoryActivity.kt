package org.apps.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import org.apps.storyapp.R
import org.apps.storyapp.databinding.ActivityDetailStoryBinding
import org.apps.storyapp.network.response.ListStoryItem
import org.apps.storyapp.withDateFormat

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    companion object{
        const val KEY_STORY = "key_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.detail_cerita)
        dataDetail()
    }

    private fun dataDetail(){
        val dataStory = intent.getParcelableExtra<ListStoryItem>("key_story")
        if (dataStory != null){
            binding.apply {
                Glide.with(this@DetailStoryActivity)
                    .load(dataStory.photoUrl)
                    .into(ivPhoto)
                tvItemNama.text = dataStory.name
                tvDeskripsi.text = dataStory.description
                tvItemDate.text = dataStory.createdAt.withDateFormat()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return true
    }
}