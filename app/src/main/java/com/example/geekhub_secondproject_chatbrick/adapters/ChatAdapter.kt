package com.example.geekhub_secondproject_chatbrick.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geekhub_secondproject_chatbrick.databinding.HeaderItemBinding
import com.example.geekhub_secondproject_chatbrick.databinding.LeftItemBinding
import com.example.geekhub_secondproject_chatbrick.databinding.RightItemBinding
import com.example.geekhub_secondproject_chatbrick.model.ItemType

interface UserChangeClickListener {
    fun userClick(user: Int, binding: HeaderItemBinding)

    fun longClick(item: ItemType, user: Int)
}

class ChatAdapter(
    val userListener: UserChangeClickListener,
) : androidx.recyclerview.widget.ListAdapter<ItemType, RecyclerView.ViewHolder>(diff) {
    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is ItemType.FirstUser -> VIEW_TYPE_MESSAGE_LEFT
            is ItemType.SecondUser -> VIEW_TYPE_MESSAGE_RIGHT
            is ItemType.HeaderList -> VIEW_TYPE_HEADERS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MESSAGE_LEFT ->
                LeftMessageViewHolder(
                    LeftItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            VIEW_TYPE_MESSAGE_RIGHT ->
                RightMessageViewHolder(
                    RightItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            else ->
                UsersHolder(
                    HeaderItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LeftMessageViewHolder -> holder.bind((currentList[position]) as ItemType.FirstUser)
            is RightMessageViewHolder -> holder.bind((currentList[position]) as ItemType.SecondUser)
            is UsersHolder -> holder.bind(currentList[position] as ItemType.HeaderList)
        }
    }

    inner class LeftMessageViewHolder(private val binding: LeftItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: ItemType.FirstUser) {
            val text = "User 1: " + userData.message.message
            binding.leftMessageTextView.text = text
            binding.leftMessageTextView.setOnLongClickListener {
                userListener.longClick(userData, VIEW_TYPE_MESSAGE_LEFT)
                return@setOnLongClickListener true
            }
        }
    }

    inner class RightMessageViewHolder(private val binding: RightItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: ItemType.SecondUser) {
            val text = "User 2: " + userData.message.message
            binding.rightMessageTextView.text = text
            binding.rightMessageTextView.setOnLongClickListener {
                userListener.longClick(userData, VIEW_TYPE_MESSAGE_RIGHT)
                return@setOnLongClickListener true
            }
        }
    }

    inner class UsersHolder(private val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: ItemType.HeaderList) {
            with(binding) {
                firstUserHeaderButton.setOnClickListener {
                    userListener.userClick(VIEW_TYPE_MESSAGE_LEFT, binding)
                }

                firstUserHeaderButton.text = "User 1: ${userData.counterFirstUser}"

                secondUserHeaderButton.setOnClickListener {
                    userListener.userClick(VIEW_TYPE_MESSAGE_RIGHT, binding)
                }

                secondUserHeaderButton.text = "User 2: ${userData.counterSecondUser}"
            }
        }
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<ItemType>() {
            override fun areItemsTheSame(oldItem: ItemType, newItem: ItemType): Boolean {
                return when (oldItem) {
                    is ItemType.FirstUser -> oldItem == (newItem as? ItemType.FirstUser)
                    is ItemType.SecondUser -> oldItem == (newItem as? ItemType.SecondUser)
                    is ItemType.HeaderList -> false
                }
            }

            override fun areContentsTheSame(oldItem: ItemType, newItem: ItemType): Boolean {
                return when (oldItem) {
                    is ItemType.FirstUser ->
                        oldItem.message == (newItem as ItemType.FirstUser).message
                    is ItemType.SecondUser ->
                        oldItem.message == (newItem as ItemType.SecondUser).message
                    is ItemType.HeaderList ->
                        (oldItem.counterFirstUser == (newItem as ItemType.HeaderList).counterFirstUser
                                && oldItem.counterSecondUser == newItem.counterSecondUser)
                }
            }
        }

        const val VIEW_TYPE_MESSAGE_LEFT = 0
        const val VIEW_TYPE_MESSAGE_RIGHT = 1
        const val VIEW_TYPE_HEADERS = 2
    }
}