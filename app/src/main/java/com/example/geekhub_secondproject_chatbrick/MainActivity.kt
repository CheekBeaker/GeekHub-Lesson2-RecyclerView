package com.example.geekhub_secondproject_chatbrick

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geekhub_secondproject_chatbrick.adapters.ChatAdapter
import com.example.geekhub_secondproject_chatbrick.adapters.UserChangeClickListener
import com.example.geekhub_secondproject_chatbrick.databinding.ActivityMainBinding
import com.example.geekhub_secondproject_chatbrick.databinding.HeaderItemBinding
import com.example.geekhub_secondproject_chatbrick.decorator.ItemDecorator
import com.example.geekhub_secondproject_chatbrick.model.ItemType
import com.example.geekhub_secondproject_chatbrick.model.Message

class MainActivity : AppCompatActivity(), UserChangeClickListener {
    private lateinit var binding: ActivityMainBinding
    private val baseAdapter = ChatAdapter(this as UserChangeClickListener)
    private var firstUserMessageCount = 0
    private var secondUserMessageCount = 0
    private var currentUser = 0
    private var messages = mutableListOf<ItemType>(ItemType.HeaderList(0, 0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        initAdapter()
        setContentView(binding.root)
        binding.sendButton.setOnClickListener {
            onSendClick()
        }
    }

    private fun onSendClick() {
        if (binding.messageEditText.text.isEmpty()) {
            return
        }

        val userMessage = when (currentUser) {
            ChatAdapter.VIEW_TYPE_MESSAGE_LEFT ->
                ItemType.FirstUser(Message(firstUserMessageCount++,
                    binding.messageEditText.text.toString()))
            else -> ItemType.SecondUser(Message(secondUserMessageCount++,
                binding.messageEditText.text.toString()))
        }

        messageCounters()
        messages.add(userMessage)
        baseAdapter.submitList(messages.toList())
    }

    private fun initAdapter() {
        binding.recyclerViewContainer.apply {
            adapter = baseAdapter
            addItemDecoration(ItemDecorator(20))
        }

        baseAdapter.submitList(messages.toList())
    }

    override fun userClick(user: Int, binding: HeaderItemBinding) {
        currentUser = user

        when (user) {
            ChatAdapter.VIEW_TYPE_MESSAGE_LEFT ->
                with(binding) {
                    firstUserHeaderButton.setBackgroundColor(Color.BLUE)
                    secondUserHeaderButton.setBackgroundColor(Color.RED)
                }
            ChatAdapter.VIEW_TYPE_MESSAGE_RIGHT ->
                with(binding) {
                    firstUserHeaderButton.setBackgroundColor(Color.RED)
                    secondUserHeaderButton.setBackgroundColor(Color.BLUE)
                }
        }
    }

    override fun longClick(item: ItemType, user: Int) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Delete this message?")
            .setPositiveButton("Yep"
            ) { dialog, _ ->
                dialogYesClick(item, user, dialog)
            }
            .setNegativeButton("Nah"
            ) { dialog, _ ->
                dialog.dismiss()
            }

        builder.show()
    }

    private fun dialogYesClick(item: ItemType, user: Int, dialog: DialogInterface) {
        when (user) {
            ChatAdapter.VIEW_TYPE_MESSAGE_LEFT ->
                messages.remove(item as ItemType.FirstUser).also { firstUserMessageCount-- }
            ChatAdapter.VIEW_TYPE_MESSAGE_RIGHT ->
                messages.remove(item as ItemType.SecondUser).also { secondUserMessageCount-- }
        }

        messageCounters()
        baseAdapter.submitList(messages.toList())
        dialog.dismiss()
    }

    private fun messageCounters() {
        (messages[0] as ItemType.HeaderList).apply {
            counterFirstUser = firstUserMessageCount
            counterSecondUser = secondUserMessageCount
        }
    }
}