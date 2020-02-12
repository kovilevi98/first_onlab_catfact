package com.example.egy.retrofil

data class CatFactsResult(
    var deleted: Boolean,
    var _id:String,
    var _v: Int,
    var text: String,
    var updateAt: String,
    var createdAt: String,
    var status: Status,
    var user:String
)

data class Status(

    var verified: Boolean,
    var sentCount: Int
)