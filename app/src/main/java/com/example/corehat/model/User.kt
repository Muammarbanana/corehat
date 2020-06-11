package com.example.corehat.model

class User(val id: String, val username: String, val name: String) {
    constructor() : this("", "", "")
}