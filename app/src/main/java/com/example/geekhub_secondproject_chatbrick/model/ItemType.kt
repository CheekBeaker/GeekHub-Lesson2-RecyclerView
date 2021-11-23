package com.example.geekhub_secondproject_chatbrick.model

sealed class ItemType {

    data class FirstUser(val message: Message) : ItemType()

    data class SecondUser(val message: Message) : ItemType()

    data class HeaderList(var counterFirstUser: Int, var counterSecondUser: Int) : ItemType()
}

