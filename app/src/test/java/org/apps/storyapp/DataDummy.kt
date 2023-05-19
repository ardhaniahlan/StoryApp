package org.apps.storyapp

import org.apps.storyapp.network.response.ListStoryItem
import org.apps.storyapp.network.response.StoryResponse
import kotlin.text.Typography.quote

object DataDummy {

    fun generateDummyStory(): StoryResponse {
        return StoryResponse(
            error = false,
            message = "success",
            listStory = arrayListOf(
                ListStoryItem(
                    id = "id",
                    name = "name",
                    description = "description",
                    photoUrl = "photoUrl",
                    createdAt = "createdAt",
                    lat = 0.01F,
                    lon = 0.01F
                )
            )
        )
    }
}