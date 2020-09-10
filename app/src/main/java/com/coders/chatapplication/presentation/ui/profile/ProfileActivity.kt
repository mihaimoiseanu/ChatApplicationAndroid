package com.coders.chatapplication.presentation.ui.profile

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.coders.chatapplication.R
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import com.coders.chatapplication.presentation.commons.bindView
import com.coders.chatapplication.presentation.commons.goneUnless
import com.coders.chatapplication.presentation.commons.toastIt
import com.coders.chatapplication.presentation.ui.views.AvatarImageView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val viewModel by viewModels<ProfileViewModel>()

    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val avatar by bindView<AvatarImageView>(R.id.avatar)
    private val name by bindView<TextView>(R.id.name)
    private val email by bindView<TextView>(R.id.email)
    private val inputButton by bindView<AppCompatButton>(R.id.input_button)
    private val blockButton by bindView<AppCompatButton>(R.id.block_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getLongExtra("other_user_id", -1)
        if (id < 0) {
            finish()
        }
        viewModel.failure.observe(this, {
            toastIt(it.exception.message ?: "Error")
        })
        viewModel.friendship.observe(this, {
            handleFriendship(it)
        })
        inputButton.setOnClickListener {
            viewModel.sendAction()
        }
        blockButton.setOnClickListener {
            viewModel.blockUser()
        }
        viewModel.getFriendship(id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleFriendship(friendshipModel: FriendshipModel) {
        val userModel = friendshipModel.userModel
        avatar.setText(
            "${userModel.firstName?.get(0)}${userModel.lastName?.get(0)}",
            ((friendshipModel.userModel.id ?: 0) % 255).toInt()
        )
        name.text = "${userModel.firstName} ${userModel.lastName}"
        email.text = "${userModel.email}"
        inputButton.text = when (friendshipModel.friendshipStatus) {
            FriendshipStatus.PENDING -> {
                blockButton.goneUnless(true)
                if (viewModel.thisUserId == friendshipModel.lastUserActioned) {
                    getString(R.string.cancel)
                } else {
                    getString(R.string.accept)
                }
            }
            FriendshipStatus.ACCEPTED -> {
                blockButton.goneUnless(true)
                getString(R.string.delete)
            }
            FriendshipStatus.BLOCKED -> if (viewModel.thisUserId != friendshipModel.lastUserActioned) {
                finish();""
            } else {
                blockButton.goneUnless()
                getString(R.string.unblock)
            }
            FriendshipStatus.NONE -> getString(R.string.request)
        }
    }
}