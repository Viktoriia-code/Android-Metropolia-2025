package com.example.assignment4_retrofit_network

data class WikiResponse(
    val query: WikiQuery
)

data class WikiQuery(
    val searchinfo: WikiSearchInfo
)

data class WikiSearchInfo(
    val totalhits: Int
)