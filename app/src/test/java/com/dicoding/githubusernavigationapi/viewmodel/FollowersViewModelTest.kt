package com.dicoding.githubusernavigationapi.viewmodel


import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FollowersViewModelTest {

    private lateinit var viewModel: FollowersViewModel
    @Before
    fun setUp() {
        viewModel = FollowersViewModel()
    }

    @Test
    fun getLisFollowers() {
        val followersEntities = viewModel.getLisFollowers()
        Assert.assertNotNull(followersEntities)
    }
}