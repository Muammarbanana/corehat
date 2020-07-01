package com.example.corehat.model

class SubMateriKomplit(val img_desc: String, val isi: String, val jenis: Int, val judul: String, val materi_id: Int, val src_img: String, val urutan: Int )  {
    constructor(): this("", "", 0, "", 0, "", 0)
}