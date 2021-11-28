package com.madcrew.testcardapp.models

    data class PageResponse(
        var status: Int? = null,
        var data: PostCards? = null
    )

    data class PostCards(
        var post_card: List<PhotoImage>? = null
    )

    data class PhotoImage(
        var id: Int? = null,
        var name: String? = null,
        var image: Image? = null,
    )

    data class Image(
        var preview: String? = null,
        var dimentions: Dimens? = null
    )

    data class Dimens(
        var width: Int? = null,
        var height: Int? = null
    )

