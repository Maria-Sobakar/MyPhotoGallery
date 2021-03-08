package com.marias.myphotogallery.data

data class PhotoItem(
    var id: String = "",
    var urls: Urls
){
    data class Urls(
        var regular:String  = "",
        var thumb: String = ""
    )
}
