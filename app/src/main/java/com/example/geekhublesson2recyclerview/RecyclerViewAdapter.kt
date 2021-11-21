package com.example.geekhublesson2recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geekhublesson2recyclerview.databinding.ItemLeftMessageBinding
import com.example.geekhublesson2recyclerview.databinding.ItemRightMessageBinding
import ua.motionman.recyclerviewlecture.model.Message

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messageList = emptyList<Message>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_MESSAGE_LEFT -> {
                val binding = ItemLeftMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderLeftMessage(binding)
            }
            VIEW_TYPE_MESSAGE_RIGHT -> {
                val binding = ItemRightMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderRightMessage(binding)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderLeftMessage -> holder.bind(messageList[position])
            is ViewHolderRightMessage -> holder.bind(messageList[position])
        }
    }

    class ViewHolderLeftMessage(private val binding: ItemLeftMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.firstUserMessageTextView.text = item.message
        }
    }

    class ViewHolderRightMessage(private val binding: ItemRightMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.secondUserMessageTextView.text = item.message
        }
    }

    companion object {
        const val VIEW_TYPE_MESSAGE_LEFT = 1
        const val VIEW_TYPE_MESSAGE_RIGHT = 2
    }
}

