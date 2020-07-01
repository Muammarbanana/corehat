package com.example.corehat.model

class NotifikasiMateri(val judul: String, val jenis: Int, val iduser: String, val message: String, val timestamp: Long, val statusbaca: Int, val submateri: SubMateriKomplit)  {
    constructor(): this("", 0, "", "", -1, 0, SubMateriKomplit("", "", 0, "", 0, "", 0))
}