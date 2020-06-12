package com.example.corehat.model

class Notifikasi(val namakonselor: String, val photo: String, val iduser: String, val message: String, val timestamp: Long, val statusbaca: Int)  {
    constructor(): this("", "", "", "", -1, 0)
}